import torch
import datetime
import pandas as pd
from torch import nn
from torch.utils.data import DataLoader

# Import Alexnet with its weights
from torchvision.models import alexnet
from torchvision.models import AlexNet_Weights

# import code from relative files
from model import NeuralNetwork  # self made architecture
from dataset import FlowerDataset


# Get cpu or gpu device where the training should take place
device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
print(f"Using {device} device")


def train_loop(dataloader, model, loss_fn, optimizer, epoch):
    size = len(dataloader.dataset)
    stats = []

    # Start Training loop
    for batch, (X, y) in enumerate(dataloader):
        # Move data to device, e.g. to gpu if training on it otherwise cpu
        inputs = X.to(device)
        target = y.to(device)

        # Compute prediction and loss
        pred = model(inputs)
        loss = loss_fn(pred, target)

        # Sets the gradients of all optimized torch.Tensor s to zero/ None
        # Otherwise the older gradients would accumulate (helpful for RNNs)
        optimizer.zero_grad()

        # Backpropagation to calculate losses
        loss.backward()

        # Adjust the weights of the model
        optimizer.step()

        # Get value of the loss
        loss = loss.item()

        # calculate number of processed images
        current = (batch + 1) * len(inputs)  # batch starts with 0

        # every 50 batches print out the current loss
        if batch % 50 == 0:
            print(
                f"EPOCH {epoch} | loss: {loss:>7f}  [{current:5d}/{size:5d}]"
                + f" | batch [{batch:>3d}/{len(dataloader):>3d}]"
            )

        # Save current loss to list
        stats.append([batch, "train", loss, None])
    return stats


def test_loop(dataloader, model, loss_fn):
    size = len(dataloader.dataset)
    num_batches = len(dataloader)
    test_loss, correct = 0, 0

    with torch.no_grad():  # disable gradient calculation --> not needed for testing
        # Start training loop
        for X, y in dataloader:
            # Move data to device, e.g. to gpu if training on it otherwise cpu
            inputs = X.to(device)
            target = y.to(device)

            pred = model(inputs)  # get prediction for the batch X

            # Calculate loss and number of correct preds for the batch
            test_loss += loss_fn(pred, target).item()
            correct += (pred.argmax(1) == target).type(torch.float).sum().item()

    # Calculate average loss and avg accuracy for the test dataset
    test_loss /= num_batches
    correct /= size

    print(
        f"Test Error: \n Accuracy: {(100*correct):>0.1f}%, Avg loss: {test_loss:>8f} \n"
    )
    return test_loss, correct


# --------------------------------------------------------------------------------------

# Set Hyperparameter
batch_size = 64
learning_rate = 5e-4
epochs = 50
weight_decay = 5e-3
dropout = 0.5  # AlexNet 0.7

# Create iterators for each data split
train_dataloader = DataLoader(
    FlowerDataset(
        annotations_file=r"datasets\augmented_flowers\train.csv",
        img_dir=r"datasets\augmented_flowers",
        split="train",
    ),
    batch_size=batch_size,
    shuffle=True,
)
test_dataloader = DataLoader(
    FlowerDataset(
        annotations_file=r"datasets\augmented_flowers\test.csv",
        img_dir=r"datasets\augmented_flowers",
        split="test",
    ),
    batch_size=batch_size,
    shuffle=True,
)

# Set Model, deciding between self made and AlexNet
# model = alexnet(weights=AlexNet_Weights.IMAGENET1K_V1, dropout=dropout) # best for 0.7
# # If the model is AlexNet: Change to only 102 output classes (from default 1000)
# model.classifier[6] = nn.Linear(4096, 102)
model = NeuralNetwork(dropout=dropout)  # set self made architecture as model

# Move model to device (gpu/ cpu)
model = model.to(device)


# Set Loss Function and Optimizer for the model
loss_fn = nn.CrossEntropyLoss()
optimizer = torch.optim.Adam(
    model.parameters(), lr=learning_rate, weight_decay=weight_decay
)

# Set pathname for saving the model file
path_name = (
    "datalab-flowers/model_files/"
    + f"{datetime.datetime.now():%Y%m%d}_batch_size_{batch_size}"
    + f"_lr{learning_rate}"
    + f"_epochs{epochs}"
    # + "_AlexNet_pretrained"
    + "self_made"
    + f"dropout{dropout}"
    + f"_weight_decay{weight_decay}"
)


# --------------------------------------------------------------------------------------

# Training loop
csv_array = []
for t in range(epochs):
    print(f"Epoch {t+1}\n-------------------------------")

    # Get training metrics by executing training loop
    train_stats = train_loop(train_dataloader, model, loss_fn, optimizer, t)

    # Get test metrics by executing test loop
    avg_loss, avg_accuracy = test_loop(test_dataloader, model, loss_fn)

    # Append metrics for this epoch to list
    csv_array = csv_array + [[t] + stats for stats in train_stats]
    csv_array.append([t, None, "valid", avg_loss, avg_accuracy])

    # Save model_halfway_checkpoint
    if t == int(epochs / 2):
        torch.save(model.state_dict(), path_name + f"_current_{t}.pt")
        df = pd.DataFrame(
            data=csv_array,
            columns=["epoch", "batch", "type", "loss", "accuracy"],
        )
        df.to_csv(path_name + ".csv", index=False)


# --------------------------------------------------------------------------------------

print("Training finished... saving results!")

# Saving model and learning stats
torch.save(model.state_dict(), path_name + ".pt")
df = pd.DataFrame(
    data=csv_array,
    columns=["epoch", "batch", "type", "loss", "accuracy"],
)
df.to_csv(path_name + ".csv", index=False)
