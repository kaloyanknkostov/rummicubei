package com.MCTS;

import java.util.ArrayList;
import com.gameEngine.Board;
import com.gameEngine.Tile;

public class MCTS {
    private GameState gameState;
    private Node root;

    public MCTS(Board board, ArrayList<Tile> deck){
        // get game state

        // Get predictions of other players decks
        // We can decide here if we want to create multiple trees by sampling the tiles based on the predictions/ probabilities we got (advanced stuff)
        this.gameState = new GameState();

        this.root = new Node(gameState, null);
    }

    public void loopMCTS(int loops){
        for (int i = 0; i < loops; i++){
            this.root.selectNode().playOut().backpropagate();
        }
    }

    public void getDeckProbabilities(){
        // get probabilites of the tiles that can be in other players hands
    }

    public void getDeckPredictions(){
        // combine probabilities with ML output for each tile
    }
}
