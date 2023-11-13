from torchvision import transforms as T
import os
import pandas as pd
from torch.utils.data import Dataset
from PIL import Image


# Create Dataset class which inherits from the PyTorch dataset class
class FlowerDataset(Dataset):
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
