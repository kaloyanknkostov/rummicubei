import pandas as pd
import torch
from torch import nn
from torch.utils.data import DataLoader
from torchmetrics import classification as clf

from model import TilePredNet
from utils.model_utils import save_training_results
from early_stopper import EarlyStopper


def train_loop(dataloader, model, loss_fn, optimizer, epoch, csv=None):
    size = len(dataloader.dataset)
    for batch, (X, y) in enumerate(dataloader):
        # Compute prediction and loss
        # print("y shape:", y.shape)
        pred = model(X)  # ["out"]
        # print(pred)
        # quit()
        # print("Pred shape:", pred.shape)
        loss = loss_fn(pred, y)
        # Backpropagation
        optimizer.zero_grad()  # set gradients to zero
        loss.backward()  # Backpropagation
        optimizer.step()  # update gradients

        # Get metrics
        metrics = get_metrics(pred, y)
        acc = metrics["acc"]
        mcc = metrics["mcc"]
        jaccard = metrics["jaccard"]
        precision = metrics["precision"]
        recall = metrics["recall"]
        specificity = metrics["specificity"]
        f1 = metrics["f1"]
        dice = metrics["dice"]

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
                    jaccard,
                    precision,
                    recall,
                    specificity,
                    f1,
                    dice,
                ]
            )
        #  TODO Get the metrics with sklearn!
        if batch % 3 == 0:
            print(
                f"loss: {loss:>7f}, Recall: {(recall):>0.4f}, "
                + f"Precision: {precision:>0.4f}, MCC: {mcc:>.4f}, "
                + f"Jaccard: {jaccard:>.4f}, "
                + f"DICE: {dice:>.4f} [{current:>5d}/{size:>5d}]"
            )


def test_loop(dataloader, model, loss_fn, epoch, csv=None):
    num_batches = len(dataloader)
    test_loss = 0
    acc, mcc, jaccard, precision, recall, specificity, f1, dice = 0, 0, 0, 0, 0, 0, 0, 0
    with torch.no_grad():
        for i, (X, y) in enumerate(dataloader):
            pred = model(X)  # ["out"]
            test_loss += loss_fn(pred, y).item()
            # Get metrics
            metrics = get_metrics(pred, y)
            acc += metrics["acc"]
            mcc += metrics["mcc"]
            jaccard += metrics["jaccard"]
            precision += metrics["precision"]
            recall += metrics["recall"]
            specificity += metrics["specificity"]
            f1 += metrics["f1"]
            dice += metrics["dice"]

    test_loss /= num_batches
    acc /= num_batches
    mcc /= num_batches
    jaccard /= num_batches
    precision /= num_batches
    recall /= num_batches
    specificity /= num_batches
    f1 /= num_batches
    dice /= num_batches

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
                jaccard,
                precision,
                recall,
                specificity,
                f1,
                dice,
            ]
        )
    print(
        f"Testing:\nloss: {test_loss:>7f}, Recall: {(recall):>0.4f},",
        f"Precision: {precision:>0.4f}, MCC: {mcc:>.4f}, Jaccard: {jaccard:>.4f}",
        f", DICE: {dice:>.4f}\n",
    )
    return test_loss


def get_metrics(pred, target, thresh=0.5):
    """
    Input:
        pred and target as pytorch.tensor
    Output:
        Accuracy, MatthewsCorrCoef, JaccardIndex in dict
    """
    # Remove the background layer so metrics are only for the leaves
    pred_tensor = pred[0, 1]
    target_tensor = target[0, 1]
    metrics = dict()
    metrics["acc"] = clf.BinaryAccuracy(threshold=thresh)(
        pred_tensor, target_tensor
    ).item()
    metrics["mcc"] = clf.BinaryMatthewsCorrCoef(threshold=thresh)(
        pred_tensor, target_tensor
    ).item()
    metrics["jaccard"] = clf.BinaryJaccardIndex(threshold=thresh)(
        pred_tensor, target_tensor
    ).item()
    # How many retrieved items are relevant?
    metrics["precision"] = clf.BinaryPrecision(threshold=thresh)(
        pred_tensor, target_tensor
    ).item()
    # How many relevant items are retrieved? - True positive rate
    metrics["recall"] = clf.BinaryRecall(threshold=thresh)(
        pred_tensor, target_tensor
    ).item()
    # True negative rate
    metrics["specificity"] = clf.BinarySpecificity(threshold=thresh)(
        pred_tensor, target_tensor
    ).item()
    metrics["f1"] = clf.BinaryF1Score(threshold=thresh)(
        pred_tensor, target_tensor
    ).item()
    metrics["dice"] = clf.Dice(threshold=thresh)(
        pred_tensor, target_tensor.type(torch.int)
    ).item()
    # print(clf.Dice(threshold=thresh)(pred, target.type(torch.int)).item())

    return metrics


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
        valid_root_dir = dataset_path + "/validation"
    else:
        df = annotations_df
        train_root_dir = ""
        valid_root_dir = ""

    train_data = None
    test_data = None

    print("Train:", len(train_data))
    print("test", len(test_data))

    train_dataloader = DataLoader(train_data, batch_size=batch_size, shuffle=True)
    test_dataloader = DataLoader(test_data, batch_size=batch_size, shuffle=True)

    model_loss_fn = loss_fn()
    model_optimizer = optimizer(model.parameters(), lr=learning_rate)

    print("learning rate:", learning_rate)
    print("Loss Function:", model_loss_fn._get_name())
    print("optimizer:", model_optimizer.__class__.__name__)
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
            "JaccardIndex",
            "precision",
            "recall",
            "specificity",
            "F1Scores",
            "DiceCoefficient",
        ]
    ]
    thresh = 0.2
    early_stopper = EarlyStopper(delta_thresh=thresh, epoch_thresh=-1)
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
                    early_stop_thresh=thresh,
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
