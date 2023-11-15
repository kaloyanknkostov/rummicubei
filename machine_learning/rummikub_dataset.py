import torch

# There are 4 colors and 13 numbers, plus a joker
NUM_COLORS = 4
NUM_NUMBERS = 13
NUM_TILES = 106


class rummikub_dataset:

    def encode_board(self, board):
        max_sets = 20  # Maximum number of sets on the board
        max_tiles_per_set = 13
        encoded_board = torch.zeros(max_sets, max_tiles_per_set, NUM_TILES)
        for i, set in enumerate(board):
            for j, tile in enumerate(set):
                encoded_board[i, j] = self.tile_encoder(tile)
        return encoded_board

    def encode_hand(self, hand):
        max_hand_size = 20  # Maximum number of tiles in a hand
        encoded_hand = torch.zeros(max_hand_size, NUM_TILES)

        for i, tile in enumerate(hand):
            encoded_hand[i] = self.tile_encoder(tile)

        return encoded_hand


def tile_encoder(tile):
    encoded_tile = torch.zeros(NUM_TILES)

    if tile.isJoker:
        encoded_tile[-1] = 1  # Last position for the joker
    else:
        color_index = get_color_index(tile.get_color)
        number_index = tile.get_number  #- 1 Im not sure if it should start at 0 or just map the tiles to their value
        index = color_index * NUM_NUMBERS + number_index
        encoded_tile[index] = 1

    return encoded_tile


# Map color to an index
def get_color_index(color):
    color_mapping = {'red': 0, 'blue': 1, 'yellow': 2, 'black': 3}
    return color_mapping[color]

