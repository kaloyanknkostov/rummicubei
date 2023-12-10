# Create training data depending on the number of players and make it model ready
# Create a file for each tile -> test

import pandas as pd
import re
import numpy as np
from rich import print

# Target Columns:
# tile, Current tiles in rack, (Current Board, Board-1)*times n, target (opponent, rest)


def create_training_data(src: str):
    tiles = [
        color + "|" + str(n)
        for color in ["red", "blue", "yellow", "black"]
        for n in range(1, 14)
    ]
    tiles.append("Joker")
    print(tiles)

    df = pd.read_csv(src)
    print(df.columns)  # Be careful of blanks in the first line!
    df["Board"] = df["Board"].apply(lambda x: "" if x == "Empty Board" else x)
    df["Board"] = df["Board"].apply(
        lambda x: [rummi_set.split(" ") for rummi_set in x.split(";")]
    )
    print(df)
    # Go through every tile to generate the training data for it
    for tile in tiles:
        # get the first n lines for the first n boards (ordered by move number asc)

        quit()
    pass


if __name__ == "__main__":
    src = r"data\raw_data\Game0.csv"
    create_training_data(src=src)
