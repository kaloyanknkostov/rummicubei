from torchmetrics import classification as clf
import torch


def get_metrics(pred, target, thresh=0.5):
    """
    Input:
        pred and target as pytorch.tensor
    Output:
        Accuracy, MatthewsCorrCoef, JaccardIndex in dict
    """
    device = "cuda" if torch.cuda.is_available() else "cpu"

    # Remove the background layer so metrics are only for the leaves
    pred_tensor = pred
    target_tensor = target
    metrics = dict()
    metrics["acc"] = (
        clf.BinaryAccuracy(threshold=thresh)
        .to(device)(pred_tensor, target_tensor)
        .item()
    )
    metrics["mcc"] = (
        clf.BinaryMatthewsCorrCoef(threshold=thresh)
        .to(device)(pred_tensor, target_tensor)
        .item()
    )
    # How many retrieved items are relevant?
    metrics["precision"] = (
        clf.BinaryPrecision(threshold=thresh)
        .to(device)(pred_tensor, target_tensor)
        .item()
    )
    # How many relevant items are retrieved? - True positive rate
    metrics["recall"] = (
        clf.BinaryRecall(threshold=thresh).to(device)(pred_tensor, target_tensor).item()
    )
    # True negative rate
    metrics["specificity"] = (
        clf.BinarySpecificity(threshold=thresh)
        .to(device)(pred_tensor, target_tensor)
        .item()
    )
    metrics["f1"] = (
        clf.BinaryF1Score(threshold=thresh)
        .to(device)(pred_tensor, target_tensor)
        .item()
    )
    metrics["dice"] = (
        clf.Dice(threshold=thresh)
        .to(device)(pred_tensor, target_tensor.type(torch.int))
        .item()
    )
    # print(clf.Dice(threshold=thresh)(pred, target.type(torch.int)).item())

    return metrics
