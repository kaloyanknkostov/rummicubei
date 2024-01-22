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
        if batch % 400 == 0:
            print(
                f"loss: {loss:>7f}, Acc: {(acc):>0.4f}, Recall: {(recall):>0.4f}, "
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
        f"[magenta1]Testing:\nloss: {test_loss:>7f}, Acc: {(acc):>0.4f},",
        f"[magenta1]Recall: {(recall):>0.4f}, Precision: {precision:>0.4f},",
        f"[magenta1]MCC: {mcc:>.4f}\n",
    )
    return test_loss


def run_training(
    train_path,
    test_path,
    epochs,
    learning_rate,
    model,
    batch_size=1,
    loss_fn=nn.MSELoss,
    optimizer=torch.optim.Adam,
    saving=True,
    saving_annotation="",
):
    train_dataset = TileDataset(annotations_file=train_path, device=device)
    test_dataset = TileDataset(annotations_file=test_path, device=device)

    print("Train:", len(train_dataset))
    print("test", len(test_dataset))

    train_dataloader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True)
    test_dataloader = DataLoader(test_dataset, batch_size=batch_size, shuffle=True)

    model_loss_fn = loss_fn()
    model_optimizer = optimizer(model.parameters(), lr=learning_rate)

    print("Learning rate:", learning_rate)
    print("Loss Function:", model_loss_fn._get_name())
    print("Optimizer:", model_optimizer.__class__.__name__)
    print("Epochs:", epochs)

    csv = [
        [
            "epoch",
            "split",
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
                    task_path="machine_learning/",
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
            task_path="",
            path_prefix="machine_learning/",
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
    train_path = r"data\training_data\train_baseline_vs_baseline_data.csv"
    test_path = r"data\training_data\test_baseline_vs_baseline_data.csv"

    device = "cuda" if torch.cuda.is_available() else "cpu"
    print(f"Using {device} device")

    model = TilePredNet(n_rounds=8, n_out=53, n_sets=679, dropout=0.2).to(device)
    # print(model)

    print("Model in use:", model._get_name())

    run_training(
        train_path=train_path,
        test_path=test_path,
        epochs=50,
        learning_rate=1e-5,
        model=model,
        batch_size=5,
        loss_fn=nn.CrossEntropyLoss,
        optimizer=torch.optim.AdamW,
        saving=True,
        saving_annotation="",
    )
