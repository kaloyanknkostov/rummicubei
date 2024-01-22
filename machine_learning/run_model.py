import argparse
from ast import literal_eval
import re


def one_hot_encoding_rack(input) -> list[int]:
    output = [0] * 53  # 53 tiles are in the board
    for tile in input:
        output[tile - 1] = 1  # -1 as the index starts at 0
    return output


def one_hot_encoding_board(board) -> list[int]:
    output = [0] * 679  # number of sets
    for set_in_board in board:
        output[ALL_SETS.index(set_in_board)] = 1
    return output


if __name__ == "__main__":
    with open(r"data\allSets.txt", "r") as f:
        ALL_SETS = [
            [int(x) for x in re.sub(r"\[|\]|\n", "", i).split(", ")]
            for i in f.readlines()
        ]
    parser = argparse.ArgumentParser(
        prog="Execute TilePred Model", description="", epilog=""
    )
    parser.add_argument("-t", "--target", default=[])
    parser.add_argument("-r", "--rack", default=[])
    parser.add_argument("-b1", "--board1", default=[])  # Current board
    parser.add_argument("-b2", "--board2", default=[])
    parser.add_argument("-b3", "--board3", default=[])
    parser.add_argument("-b4", "--board4", default=[])
    parser.add_argument("-b5", "--board5", default=[])
    parser.add_argument("-b6", "--board6", default=[])
    parser.add_argument("-b7", "--board7", default=[])
    parser.add_argument("-b8", "--board8", default=[])

    args = parser.parse_args()
    print(literal_eval(args.target)[0])
    print(literal_eval(args.board1)[0])
    print(vars(args))
    for i in vars(args):
        print(i)
8
