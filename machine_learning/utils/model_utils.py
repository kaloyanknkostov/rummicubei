import time
import pandas as pd
from os.path import join

import torch


def save_training_results(
    task_path: str,
    path_prefix: str,
    model,
    csv,
    csv_epoch,
    model_epoch,
    batch_size,
    learning_rate,
    loss_fn_name,
    opti_name,
    annotation,
    early_stop=False,
    early_stop_thresh=0,
):
    saving_annotation = annotation
    if early_stop:
        saving_annotation = saving_annotation + "_early_stop"  # _{early_stop_thresh}"

    TODAY = time.strftime(r"%Y%m%d-%H_%M_%S", time.gmtime())
    root_path = join(
        path_prefix,
        "model_files",
        # "segmentation",
        task_path,
    )
    csv_path = join(
        root_path,
        f"{TODAY}_metrics_epochs_{csv_epoch+1}_batchSize{batch_size}"
        + f"_l{learning_rate}_loss_{loss_fn_name}"
        + f"_opt_{opti_name}"
        + saving_annotation
        + ".csv",
    )
    model_path = join(
        root_path,
        f"{TODAY}_model_epochs_{model_epoch+1}_batch_size{batch_size}"
        + f"_l_{learning_rate}_loss_{loss_fn_name}"
        + f"_opt_{opti_name}"
        + saving_annotation
        + ".pt",
    )
    torch.save(model.state_dict(), model_path)
    if not early_stop:  # Same data will be saved afterwards
        pd.DataFrame(csv).to_csv(csv_path, index=False)
