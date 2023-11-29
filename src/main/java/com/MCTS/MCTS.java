package com.MCTS;

import java.util.ArrayList;
import com.gameEngine.Board;
import com.gameEngine.ComputerPlayer;
import com.gameEngine.Tile;




public class MCTS {
    private GameState gameState;
    private ComputerPlayer computerPlayer; // needs to probably been got from the gameState. 
    private Node root;
    private Board board; 

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
        ArrayList<Tile> tilesBoard = board.getTilesInBoard(); 
        ArrayList<Tile> tilesHand = computerPlayer.getDeckOfTiles(); 
        int numberOfUnkownTiles = 106; 
        int[][] array = {
            {2, 2, 2, 2},
            {2, 2, 2, 2},
            {2, 2, 2, 2},
            {2, 2, 2, 2},
            {2, 2, 2, 2},
            {2, 2, 2, 2},
            {2, 2, 2, 2},
            {2, 2, 2, 2},
            {2, 2, 2, 2},
            {2, 2, 2, 2},
            {2, 2, 2, 2},
            {2, 2, 2, 2},
            {2, 2, 2, 2},
            {2} 
        };
        double[][] probabiltiyArray = {
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0} 
        };
    
        for (Tile t : tilesBoard) {
            if(t.isJoker()){
                array[13][0] -= 1; 
                numberOfUnkownTiles--; 
            } else {
            array[t.getNumber()][colorToNumber(t.getColor())] -= 1;   
                numberOfUnkownTiles--; 
            }
        }
        for (Tile t: tilesHand) {
           if(t.isJoker()){
                array[13][0] -= 1; 
                numberOfUnkownTiles--; 
            } else {
            array[t.getNumber()][colorToNumber(t.getColor())] -= 1;   
                numberOfUnkownTiles--; 
            }
        }
        // probabilty for a given tile: 
        for (int i = 0; i < probabiltiyArray.length; i++) {
            for (int j = 0; j < probabiltiyArray[0].length; j++) {
                if(i == 13){
                    probabiltiyArray [13][0] = array[13][0]/numberOfUnkownTiles; 
                }
                probabiltiyArray[i][j] = array[i][j]/numberOfUnkownTiles; 
            }
        }

        // get probabilites of the tiles that can be in other players hands
    }
    public int colorToNumber(String color){
        switch (color.toLowerCase()) {
            case "red":
                return 1;
            case "black":
                return 2;
            case "blue":
                return 3;
            case "yellow":
                return 0;}
    return -1; //represnting a joker

    }

    public void getDeckPredictions(){
        // combine probabilities with ML output for each tile
    }
}
