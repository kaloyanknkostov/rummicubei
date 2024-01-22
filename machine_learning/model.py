import torch
from torch import nn
from rich import print


class TilePredNet(nn.Module):
    def __init__(self, n_rounds=1, n_out=53, n_hidden=1000, n_sets=1174, dropout=0.3):
        super(TilePredNet, self).__init__()

        # Multi use variables
        self.relu = nn.ReLU()
        self.dropout = nn.Dropout(p=dropout)
        self.batch_norm1 = nn.BatchNorm2d(num_features=20)
        self.sigmoid = nn.Sigmoid()

        # linear layers
        self.fc_in = nn.Linear(in_features=53 + n_sets * n_rounds, out_features=n_sets)
        self.fc_hidden_1 = nn.Linear(in_features=n_sets, out_features=n_hidden)
        self.fc_out = nn.Linear(in_features=n_hidden, out_features=n_out)

    # Define function which represents the model architecture (top-down)
    def forward(self, x):
        logits = self.relu(self.fc_in(x))

        # Drop random weights to reduce overfitting
        logits = self.dropout(logits)

        logits = self.relu(self.fc_hidden_1(logits))

        logits = self.fc_out(logits)

        return self.sigmoid(logits)

    def load(self, path: str):
        """Loads the state_dict that is at the given path.

        Args:
            path (str): Path to the state_dict

        Returns:
            Model: model with the new state_dict
        """
        self.load_state_dict(torch.load(path))
        return self

    def save(self, path: str):
        """Saves the model state_dict to the given path.

        Args:
            path (str): Path to the save location
        """
        torch.save(self.state_dict(), path)


# ==============================================================================


if __name__ == "__main__":
    # Test model with input size --> no errors in fully connected layers
    n = 4
    n_opp = 3
    model = TilePredNet(n_rounds=n, n_out=n_opp)
    x = torch.randn((64, 53 + 679 * n))
    pred = model(x)
    print("Pred shape:", pred.shape)
    print("[green]SUCCESS![/green]")
