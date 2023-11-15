# From https://stackoverflow.com/questions/71998978/early-stopping-in-pytorch
# https://link.springer.com/chapter/10.1007/3-540-49430-8_3
import numpy as np
from rich import print
import copy


class EarlyStopper:
    def __init__(
        self, delta_thresh: float = 0.3, epoch_thresh: int = -1, max_epochs: int = 10
    ):
        self.delta_thresh = delta_thresh
        self.epoch_thresh = epoch_thresh
        self.min_validation_loss = np.inf
        self.best_epoch = 0
        self.best_model = None
        self.max_epochs = max_epochs
        self.countdown = self.max_epochs

    def early_stop(self, model, validation_loss: float, epoch: int) -> bool:
        """Returns True if validation_loss exceeds the threshold and epoch is bigger
        than epoch_thresh

        Args:
            validation_loss (float): Validation loss of the training
            epoch (int): current epoch

        Returns:
            bool: True if the training should stop
        """

        if validation_loss < self.min_validation_loss:
            self.min_validation_loss = validation_loss
            self.best_epoch = epoch
            self.best_model = copy.deepcopy(model)
            self.countdown = self.max_epochs
        else:
            self.countdown -= 1
            # print("[red]higher validation score detected[/red]")
            # print(f"Minimal loss: {self.min_validation_loss:6f}")
            # print(f"Current loss: {validation_loss:6f}")

        # generalization_loss:
        # relative increase of the validation error over the minimum-so-far
        gen_loss = validation_loss / self.min_validation_loss - 1

        # if the percentage change is bigger than a certain threshold end training
        # TODO Custom implementation possible
        # TODO Only stop after the first n epochs
        if self.countdown <= 0 or (
            # epoch > self.epoch_thresh and gen_loss > self.delta_thresh
        ):
            print("[red bold]\n----- Training Stopped! -----[/red bold]")
            print(f"Minimal loss:        {self.min_validation_loss:6f}")
            print(f"Current loss:        {validation_loss:6f}")
            print(f"Generalization_loss: {gen_loss:6f}")
            return True
        return False
