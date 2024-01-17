# Create training data depending on the number of players and make it model ready
# Create a file for each tile -> test

import pandas as pd
import re
from rich import print

# Target Columns:
# tile, Current tiles in rack, (Current Board, Board-1)*times n, target (opponent, rest)


def encode_Tiles_one_hot(input: str) -> str:
    colors = {"red": 1, "blue": 2, "yellow": 3, "black": 4}
    for color in colors:
        input = re.sub(color, str(colors[color]), input)
    input = [
        int(tile.split("|")[0]) * int(tile.split("|")[1]) - 1  # to reach zero (index)
        for tile in re.findall(r"\d\|\d+", input)
    ]
    print(input)
    quit()
    return input


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
    # df.insert(2, "Player1", value=None)
    # df.insert(3, "Player2", value=None)
    print(df)
    df[["Player1", "Player2"]] = df["PlayersHands"].str.split(";", expand=True)
    df["Player1"] = df["Player1"].apply(encode_Tiles_one_hot)
    df["Player2"] = df["Player2"].apply(encode_Tiles_one_hot)

    df = df.drop("PlayersHands", axis=1)
    print(df)
    # Go through every tile to generate the training data for it
    for tile in tiles:
        # get the first n lines for the first n boards (ordered by move number asc)

        quit()
    return df


if __name__ == "__main__":
    src = r"data\raw_data\Game0.csv"
    dst = r"data\training_data\test.csv"
    print(encode_Tiles_one_hot("black|13"))
    create_training_data(src=src).to_csv(dst, index=False)
