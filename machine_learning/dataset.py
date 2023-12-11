import os
import pandas as pd
from torch.utils.data import Dataset


# Create Dataset class which inherits from the PyTorch dataset class
class TileDataset(Dataset):
    def __init__(self, annotations_file, img_dir, split: str):
        self.img_labels = pd.read_csv(annotations_file)
        self.img_dir = img_dir
        self.transform = T.ToTensor()
        self.split = split

    # method for getting the length of the dataset
    def __len__(self):
        return len(self.img_labels)

    # Method for getting the next item in the dataset
    def __getitem__(self, idx):
        img_path = os.path.join(self.img_dir, self.split, self.img_labels.iloc[idx, 0])

        # Open image with PIL and transform it to a tensor
        image = self.transform(Image.open(img_path))

        # Get the label corresponding to the image
        label = self.img_labels.iloc[idx, 1]
        return image, label


def tile_number2string(tile: int):
    tile_index = np.floor_divide(tile, 13)
    colors = {0: "blue", 1: "red", 2: "yellow", 3: "black"}
    # if colors
    pass


def tile_string2number(tile: str) -> int:
    if tile == "Joker":
        return 53
    else:
        colors = {"blue": 1, "red": 2, "yellow": 3, "black": 4}
        color, number = tile.split("|")
        return colors[color] * int(number)


if __name__ == "__main__":
    import numpy as np
    import re

    with open(r"machine_learning\utils\sets.txt", "r") as f:
        sets = [re.sub(r"\[|\s|\]\n", "", i).split(",") for i in f.readlines()]
    sets_strings = [str(i) for i in sets if len(i) == 5 and i.count(str(53)) == 2]

    print(len(np.unique(sets_strings)))
    # blue, red, yellow, black, 53 for joker
    print(np.floor_divide(15, 13))
    print("Index:", tile_string2number("red|13"))
