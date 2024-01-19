import os
import pandas as pd
from torch.utils.data import Dataset
import torch
import re
from ast import literal_eval

# Create Dataset class which inherits from the PyTorch dataset class
class TileDataset(Dataset):
    def __init__(self, annotations_file):
        self.df = pd.read_csv(annotations_file)
        for column in self.df.columns:
            self.df[column] = self.df[column].apply(literal_eval)


    # method for getting the length of the dataset
    def __len__(self):
        return len(self.df)

    # Method for getting the next item in the dataset
    def __getitem__(self, idx):
        features = torch.cat([torch.Tensor(series)
                              for series in self.df.iloc[idx, 1:].values])
        label = torch.Tensor(self.df.iloc[idx, 0])

        return features, label


if __name__ == "__main__":
    from torch.utils.data import DataLoader

    dataset = TileDataset(r"data\training_data\training_data_tile_53.csv")
    print(dataset.__getitem__(0))
