# Create training data depending on the number of players and make it model ready
# Create a file for each tile -> test

import pandas as pd
import re
from rich import print
from tqdm import tqdm

# Target Columns:
# tile, Current tiles in rack, (Current Board, Board-1)*times n, target (opponent, rest)


def one_hot_encoding_rack(input) -> list[int]:
    output = [0] * 53  # 53 tiles are in the board
    for tile in input:
        output[tile] = 1
    return output


def one_hot_encoding_board(board) -> list[int]:
    output = [0] * 679  # number of sets
    for set_in_board in board:
        output[ALL_SETS.index(set_in_board)] = 1
    return output


def encode_Tiles_2_number(input: str) -> str:
    if len(input) <= 0:
        return ""
    colors = {"red": 0, "blue": 1, "yellow": 2, "black": 3}
    for color in colors:
        input = re.sub(color, str(colors[color]), input)
    if re.search(";", input):  # if its a board
        return [
            [
                int(tile.split("|")[0]) * 13 + int(tile.split("|")[1])
                # to reach zero (index)
                for tile in re.findall(r"\d\|\d+", board_set)
            ]
            for board_set in input.split(";")
        ]
    input = [
        int(tile.split("|")[0]) * 13 + int(tile.split("|")[1])  # to reach zero (index)
        for tile in re.findall(r"\d\|\d+", input)
    ]
    return input


def create_training_data(src: str, dst: str) -> None:
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
    print(df)
    df["Board"] = df["Board"].apply(one_hot_encoding_board)

    # df.insert(2, "Player1", value=None)
    # df.insert(3, "Player2", value=None)
    print(df)
    df[["Player1", "Player2"]] = df["PlayersHands"].str.split(";", expand=True)
    df["Player1"] = df["Player1"].apply(encode_Tiles_2_number)
    df["Player2"] = df["Player2"].apply(encode_Tiles_2_number)

    df = df.drop("PlayersHands", axis=1)
    print(df)
    df["Player1"] = df["Player1"].apply(one_hot_encoding_rack)
    df["Player2"] = df["Player2"].apply(one_hot_encoding_rack)

    print("[green]One Hot df[/green]")
    print(df)

    df.to_csv(dst + f"training_data_TEST.csv", index=False)
    # Go through every tile to generate the training data for it
    for tile in tqdm(list(range(0, 53))):
        # get the first n lines for the first n boards (ordered by move number asc)
        # one line consists of player one rack + (board before current move (same line) + board of line before (difference represent the opportunities)) *times n
        tile_df = []
        for i in range(len(df)):  # go through every line in our data
            target = [0, 1]
            if df["Player2"].iloc[i][tile] == 1:
                target = [1, 0]
            line = {
                "target": target,
                "rack": df["Player1"].iloc[i],
                "board": df["Board"].iloc[i],
            }
            past_boards = 6
            for j in range(past_boards + 1):
                if i - j < 0:
                    line[f"board-{j}"] = [0] * 679
                else:
                    line[f"board-{j}"] = df["Board"].iloc[-j]
            tile_df.append(line)
        tile_df = pd.DataFrame(tile_df)  # convert list of dicts to df
        tile_df.to_csv(dst + f"training_data_tile_{tile+1}.csv", index=False)
    pass


if __name__ == "__main__":
    with open(r"data\allSets.txt", "r") as f:
        ALL_SETS = [
            [int(x) for x in re.sub(r"\[|\]|\n", "", i).split(", ")]
            for i in f.readlines()
        ]
    src = r"data\raw_data\Game0.csv"
    dst = r"data\training_data\\"
    print(encode_Tiles_2_number("black|13"))
    create_training_data(src=src, dst=dst)
