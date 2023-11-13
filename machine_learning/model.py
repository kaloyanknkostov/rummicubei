import torch
from torch import nn
from rich import print


class TilePredNet(nn.Module):
    def __init__(self, n_rounds=1, n_opponents=3, dropout=0.3):
        super(TilePredNet, self).__init__()

        # Multi use variables
        self.relu = nn.ReLU()
        self.dropout = nn.Dropout(p=dropout)
        self.batch_norm1 = nn.BatchNorm2d(num_features=20)
        # self.softmax = nn.Softmax()

        # linear layers
        self.fc_in = nn.Linear(
            in_features=106 + 1174 * n_opponents * n_rounds, out_features=1174
        )
        self.fc_hidden_1 = nn.Linear(in_features=1174, out_features=100)
        self.fc_out = nn.Linear(in_features=100, out_features=1 + n_opponents)

    # Define function which represents the model architecture (top-down)
    def forward(self, x):
        logits = self.relu(self.fc_in(x))

        # Drop random weights to reduce overfitting
        logits = self.dropout(logits)

        logits = self.relu(self.fc_hidden_1(logits))

        logits = self.relu(self.fc_out(logits))

        # No softmax, because the loss function already applies it
        return logits


if __name__ == "__main__":
    # Test model with input size --> no errors in fully connected layers
    n = 4
    n_opp = 3
    model = TilePredNet(n_rounds=n, n_opponents=n_opp)
    x = torch.randn((64, 106 + 1174 * n_opp * n))
    pred = model(x)
    print("Pred shape:", pred.shape)
    print("[green]SUCCESS![/green]")
