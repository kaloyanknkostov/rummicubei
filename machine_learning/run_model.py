import argparse
from ast import literal_eval
import re
import os

# from rich import print
import torch
from model import TilePredNet


def one_hot_encoding_rack(input) -> list[int]:
    output = [0] * 53  # 53 tiles are in the board
    for tile in input:
        output[int(tile) - 1] = 1  # -1 as the index starts at 0
    return output


def one_hot_encoding_board(board) -> list[int]:
    output = [0] * 679  # number of sets
    for set_in_board in board:
        output[ALL_SETS.index(set_in_board)] = 1
    return output


if __name__ == "__main__":
    device = "cuda" if torch.cuda.is_available() else "cpu"
    # print(f"Using {device} device")
    MODEL_PATH = os.path.join(
        r"machine_learning",
        "model_files",
        "20240122-09_05_14_model_epochs_39_batch_size1_l_0."
        + "0005_loss_CrossEntropyLoss_opt_type.pt",
    )

    with open(r"data\allSets.txt", "r") as f:
        ALL_SETS = [
            [int(x) for x in re.sub(r"\[|\]|\n", "", i).split(", ")]
            for i in f.readlines()
        ]
    parser = argparse.ArgumentParser(
        prog="Execute TilePred Model", description="", epilog=""
    )
    parser.add_argument("-r", "--rack", default="[]")
    parser.add_argument("-b1", "--board1", default="[]")  # Current board
    parser.add_argument("-b2", "--board2", default="[]")
    parser.add_argument("-b3", "--board3", default="[]")
    parser.add_argument("-b4", "--board4", default="[]")
    parser.add_argument("-b5", "--board5", default="[]")
    parser.add_argument("-b6", "--board6", default="[]")
    parser.add_argument("-b7", "--board7", default="[]")
    parser.add_argument("-b8", "--board8", default="[]")

    args = parser.parse_args()
    args_dict = vars(args)
    args_dict["rack"] = one_hot_encoding_rack(literal_eval(args_dict["rack"]))
    for board in args_dict:
        if board != "rack":
            args_dict[board] = one_hot_encoding_board(literal_eval(args_dict[board]))

    X = torch.cat([torch.Tensor(args_dict[key]) for key in args_dict]).to(device)

    model = TilePredNet(n_rounds=8, n_out=53, n_sets=679, dropout=0.2)
    model.load_state_dict(torch.load(MODEL_PATH, map_location=torch.device(device)))
    model.to(device)

    preds = model(X)
    results = preds.round(decimals=3).tolist()
    print(results)
