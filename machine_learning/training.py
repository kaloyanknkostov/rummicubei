from rich import print
import pandas as pd
import torch
from torch import nn
from torch.utils.data import DataLoader

from model import TilePredNet
from dataset import TileDataset
from early_stopper import EarlyStopper
from utils.metrics import get_metrics
from utils.model_utils import save_training_results


def train_loop(dataloader, model, loss_fn, optimizer, epoch: int, csv: list = None):
    size = len(dataloader.dataset)
    for batch, (X, y) in enumerate(dataloader):
        # Compute prediction and loss
        pred = model(X)
        loss = loss_fn(pred, y)

        # Backpropagation
        optimizer.zero_grad()  # set gradients to zero
        loss.backward()  # Backpropagation
        optimizer.step()  # update gradients

        # Get metrics
        metrics = get_metrics(pred, y)
        acc = metrics["acc"]
        mcc = metrics["mcc"]
        precision = metrics["precision"]
        recall = metrics["recall"]
        specificity = metrics["specificity"]
        f1 = metrics["f1"]

        # Output and save metrics
        loss, current = loss.item(), batch * len(X)
        if csv:
            csv.append(
                [
                    epoch,
                    "train",
                    acc,
                    loss,
                    current,
                    size,
                    mcc,
                    precision,
                    recall,
                    specificity,
                    f1,
                ]
            )
        #  TODO Get the metrics with sklearn!
        if batch % 3 == 0:
            print(
                f"loss: {loss:>7f}, Recall: {(recall):>0.4f}, "
                + f"Precision: {precision:>0.4f}, MCC: {mcc:>.4f}"
                + f" [{current:>5d}/{size:>5d}]"
            )


def test_loop(dataloader, model, loss_fn, epoch, csv=None):
    test_loss, acc, mcc, precision, recall, specificity, f1 = 0, 0, 0, 0, 0, 0, 0
    with torch.no_grad():
        for i, (X, y) in enumerate(dataloader):
            pred = model(X)
            test_loss += loss_fn(pred, y).item()

            # Get metrics
            metrics = get_metrics(pred, y)
            acc += metrics["acc"]
            mcc += metrics["mcc"]
            precision += metrics["precision"]
            recall += metrics["recall"]
            specificity += metrics["specificity"]
            f1 += metrics["f1"]

    num_batches = len(dataloader)
    test_loss /= num_batches
    acc /= num_batches
    mcc /= num_batches
    precision /= num_batches
    recall /= num_batches
    specificity /= num_batches
    f1 /= num_batches

    # matthews-correlation, f1, recall, etc.
    if csv:
        csv.append(
            [
                epoch,
                "test",
                acc,
                test_loss,
                None,
                num_batches,
                mcc,
                precision,
                recall,
                specificity,
                f1,
            ]
        )
    print(
        f"[magenta1]Testing:\nloss: {test_loss:>7f}, Recall: {(recall):>0.4f},",
        f"Precision: {precision:>0.4f}, MCC: {mcc:>.4f}\n[/magenta1]",
    )
    return test_loss


def run_training(
    dataset_path,
    epochs,
    learning_rate,
    model,
    batch_size=1,
    loss_fn=nn.MSELoss,
    optimizer=torch.optim.Adam,
    saving=True,
    saving_annotation="",
    annotations_df=None,
):
    # datasets
    if annotations_df is None:
        df = pd.read_csv(dataset_path + "/image_label_mapping.csv")
        train_root_dir = dataset_path + "/training"
        test_root_dir = dataset_path + "/validation"
    else:
        df = annotations_df
        train_root_dir = ""
        test_root_dir = ""

    train_data = None  # TileDataset(train_root_dir)
    test_data = None  # TileDataset()

    print("Train:", len(train_data))
    print("test", len(test_data))

    train_dataloader = DataLoader(train_data, batch_size=batch_size, shuffle=True)
    test_dataloader = DataLoader(test_data, batch_size=batch_size, shuffle=True)

    model_loss_fn = loss_fn()
    model_optimizer = optimizer(model.parameters(), lr=learning_rate)

    print("Learning rate:", learning_rate)
    print("Loss Function:", model_loss_fn._get_name())
    print("Optimizer:", model_optimizer.__class__.__name__)
    print("Epochs:", epochs)

    csv = [
        [
            "epoch",
            "state",
            "accuracy",
            "loss",
            "current",
            "batch_size",
            "BinaryMatthewsCorrCoef",
            "precision",
            "recall",
            "specificity",
            "F1Scores",
        ]
    ]

    early_stopping_thresh = 0.2
    early_stopper = EarlyStopper(delta_thresh=early_stopping_thresh, epoch_thresh=-1)
    saving_model = model
    saving_epoch = 0
    for t in range(epochs):
        print(f"Epoch {t+1}\n-------------------------------")
        train_loop(train_dataloader, model, model_loss_fn, model_optimizer, t, csv)
        test_loss = test_loop(test_dataloader, model, model_loss_fn, t, csv)
        if early_stopper.early_stop(model, validation_loss=test_loss, epoch=t):
            if saving:
                print("EARLY STOP SAVING ACTIVATED")
                save_training_results(
                    task_path="segmentation",
                    path_prefix="",
                    model=early_stopper.best_model,
                    csv=csv,
                    csv_epoch=-1,
                    model_epoch=early_stopper.best_epoch,
                    batch_size=1,
                    learning_rate=learning_rate,
                    loss_fn_name=loss_fn.__name__,
                    opti_name=optimizer.__class__.__name__,
                    annotation=saving_annotation,
                    early_stop=True,
                    early_stop_thresh=early_stopping_thresh,
                )
            break
        saving_epoch = t
    else:
        # if the training runs through we still want to save the best model
        print("Best Epoch:", early_stopper.best_epoch)
        saving_model = early_stopper.best_model
        saving_epoch = early_stopper.best_epoch

    if saving:
        print("NORMAL SAVING")
        save_training_results(
            task_path="segmentation",
            path_prefix="",
            model=saving_model,
            csv=csv,
            csv_epoch=t,
            model_epoch=saving_epoch,
            batch_size=1,
            learning_rate=learning_rate,
            loss_fn_name=loss_fn.__name__,
            opti_name=optimizer.__class__.__name__,
            annotation=saving_annotation,
        )

    print("Done!")


if __name__ == "__main__":
    print("Starting training...")
    dataset_path = ""

    device = "cuda" if torch.cuda.is_available() else "cpu"
    print(f"Using {device} device")

    model = TilePredNet(n_rounds=1, n_opponents=3, dropout=0.2).to(device)
    # print(model)

    print("Model in use:", model._get_name())

    run_training(
        dataset_path=dataset_path,
        epochs=30,
        learning_rate=5e-4,
        model=model,
        batch_size=1,
        loss_fn=nn.CrossEntropyLoss,
        optimizer=torch.optim.AdamW,
        saving=False,
        saving_annotation="",
    )
