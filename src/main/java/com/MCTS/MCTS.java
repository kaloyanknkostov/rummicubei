package com.MCTS;

import java.util.ArrayList;
import java.util.Arrays;

import com.gameEngine.Board;
import com.gameEngine.ComputerPlayer;
import com.gameEngine.Tile;




public class MCTS {
    private GameState gameState;
    private ComputerPlayer computerPlayer; // needs to probably been got from the gameState.
    private Node root;
    private ArrayList<ArrayList<Integer>> board;
    private ArrayList<Integer> deck;
    ArrayList<Integer> guessedOppononetDeck;

    public MCTS(ArrayList<ArrayList<Integer>> board, ArrayList<Integer> deck, int numberTilesOpponent){
        // get game state
        this.board = board;
        this.deck = deck;
        // Get predictions of other players decks
        // We can decide here if we want to create multiple trees by sampling the tiles based on the predictions/ probabilities we got (advanced stuff)
        this.guessedOppononetDeck = guessPlayer2Deck(getDeckProbabilities(deck), numberTilesOpponent);
        this.gameState = new GameState(this.deck, guessedOppononetDeck, this.board ,getPile());

        this.root = new Node(gameState, null, false);
    }

    public void loopMCTS(int loops){
        for (int i = 0; i < loops; i++){
            this.root.selectNode().playOut();
        }
    }

    public ArrayList<Integer> guessPlayer2Deck(double[][] probabilities, int opponentDeckSize){
        double cumulativeProbability = 0;
        double randomValue = Math.random();
        ArrayList<Integer> guessedDeck= new ArrayList<>();
        while(guessedDeck.size()< opponentDeckSize){
            for (int i = 0; i < probabilities.length; i++) {
                for(int k=0; k<probabilities[i].length; k++){
                    cumulativeProbability += probabilities[i][k];
                    if (randomValue <= cumulativeProbability){
                        guessedDeck.add((13*k)+i);
                        getDeckProbabilities(guessedDeck);
                    }
                }
            }
        }
        return guessedDeck;
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

    private ArrayList<Integer> decompose(ArrayList<ArrayList<Integer>> board){
        ArrayList<Integer> result = new ArrayList<>();
        for(ArrayList<Integer> row: board){
            for(Integer tile: row){
                result.add(tile);
            }
        }
        return result;
    }

    public void getDeckPredictions(){
        // combine probabilities with ML output for each tile
    }

    private ArrayList<Integer> getPile(){
        ArrayList<Integer> allTilesNotPile = decompose(this.board);
        allTilesNotPile.addAll(this.deck);
        allTilesNotPile.addAll(this.guessedOppononetDeck);
        ArrayList<Integer> allTiles = new ArrayList<>(Arrays.asList(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 
            21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 
            31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 
            51, 52, 53,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 
            21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 
            31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 
            51, 52, 53));
            customRemove(allTiles, allTilesNotPile);
            return allTiles;
    }

    private static void customRemove(ArrayList<Integer> list, ArrayList<Integer> elementsToRemove) {
        for (Integer element : elementsToRemove) {
            list.remove(element);
        }
    }
}
