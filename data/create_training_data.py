# Create training data depending on the number of players and make it model ready
# Create a file for each tile -> test

import pandas as pd
import re
from rich import print

# Target Columns:
# tile, Current tiles in rack, (Current Board, Board-1)*times n, target (opponent, rest)

def one_hot_encoding_rack(input):
    output = [0] * 53 # 53 tiles are in the board
    print(input)
    for tile in input:
        output[tile] = 1
    return output

def encode_Tiles_2_number(input: str) -> str:
    if len(input) <= 0:
        return ""
    colors = {"red": 1, "blue": 2, "yellow": 3, "black": 4}
    for color in colors:
        print("Input:", input, len(input))
        input = re.sub(color, str(colors[color]), input)
    if re.search(";", input): # if its a board
        return [[
                int(tile.split("|")[0]) * int(tile.split("|")[1]) - 1
                # to reach zero (index)
                for tile in re.findall(r"\d\|\d+", board_set)]
                for board_set in input.split(";")]
    input = [
        int(tile.split("|")[0]) * int(tile.split("|")[1]) - 1  # to reach zero (index)
        for tile in re.findall(r"\d\|\d+", input)
    ]
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
    df["Board"] = df["Board"].apply(encode_Tiles_2_number)
    # df.insert(2, "Player1", value=None)
    # df.insert(3, "Player2", value=None)
    print(df)
    df[["Player1", "Player2"]] = df["PlayersHands"].str.split(";", expand=True)
    df["Player1"] = df["Player1"].apply(encode_Tiles_2_number)
    df["Player2"] = df["Player2"].apply(encode_Tiles_2_number)

    df = df.drop("PlayersHands", axis=1)

    df["Player1"] = df["Player1"].apply(one_hot_encoding_rack)
    df["Player2"] = df["Player2"].apply(one_hot_encoding_rack)

    print("[green]One Hot df[/green]")
    print(df)

    # Go through every tile to generate the training data for it
    for tile in tiles:
        # get the first n lines for the first n boards (ordered by move number asc)

        pass
    return df


if __name__ == "__main__":
    src = r"data\raw_data\Game0.csv"
    dst = r"data\training_data\test.csv"
    print(encode_Tiles_2_number("black|13"))
    create_training_data(src=src).to_csv(dst, index=False)
