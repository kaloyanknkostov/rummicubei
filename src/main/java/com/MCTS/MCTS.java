package com.MCTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;





public class MCTS {
    private GameState gameState;
    private Node root;
    private ArrayList<ArrayList<Integer>> board;
    private ArrayList<Integer> deck;
    ArrayList<Integer> guessedOppononetDeck;

    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> board = new ArrayList<>();
        board.add(new ArrayList<>(Arrays.asList(1,2,3)));
        //board.add(new ArrayList<>(Arrays.asList(5,6,7)));
        ArrayList<Integer> deck =  new ArrayList<>(Arrays.asList(9,10,11));

        MCTS mcts = new MCTS(board, deck, 3);
        mcts.loopMCTS(1);
    }


    public MCTS(ArrayList<ArrayList<Integer>> board, ArrayList<Integer> deck, int numberTilesOpponent){
        // get game state
        this.board = board;
        this.deck = deck;
        // Get predictions of other players decks
        // We can decide here if we want to create multiple trees by sampling the tiles based on the predictions/ probabilities we got (advanced stuff)
        this.guessedOppononetDeck = guessPlayer2Deck(getDeckProbabilities(null), numberTilesOpponent);
        this.gameState = new GameState(this.deck, guessedOppononetDeck, this.board ,getPile());
        this.root = new Node(this.gameState, null, 0, false);
    }

    public void loopMCTS(int loops){
        // Should loop n* player count times
        for (int i = 0; i < loops*2; i++){//TODO only works for 2 players
            System.err.println("loop: " + i);
            Node selected_node = this.root.selectNode();
            selected_node.expand();
            // Get a child from the selected node to start Play-Out (first child node)
            selected_node = selected_node.selectNode();
            selected_node.playOut();
        }
    }

    public  ArrayList<Integer> guessPlayer2Deck(ArrayList<Double> probabilities, int opponentDeckSize){
        ArrayList<Integer> guessedDeck = new ArrayList<>();
        while(guessedDeck.size() < opponentDeckSize){
            double cumulativeProbability = 0;

            double randomValue = Math.random();
            for (int i = 0; i < probabilities.size(); i++) {
                cumulativeProbability += probabilities.get(i);
                if (randomValue <= cumulativeProbability){
                    guessedDeck.add(i+1);
                    //System.out.println(guessedDeck);
                    //ArrayList<Double>temp=probabilities;

                    probabilities= getDeckProbabilities(guessedDeck);
                    /*ArrayList<Double> a= new ArrayList<>();
                    for(int k=0;k<probabilities.size();k++){
                        a.add(temp.get(k)-probabilities.get(k));
                    }
                    System.out.println(a);*/
                    break;
                }
            }
        }
        return guessedDeck;
    }
    //output has size 53
    private  ArrayList<Double> getDeckProbabilities(ArrayList<Integer> additionalKnownTiles){
        ArrayList<Integer> tilesBoard = CustomUtility.decompose(this.board);
         ArrayList<Integer> tilesHand = this.deck;
         ArrayList<Integer> arrayNumber = new ArrayList<>(Collections.nCopies(53, 2));
         ArrayList<Double> arrayProb = new ArrayList<Double>();
         int numberOfUnkownTiles = 106;
         if(additionalKnownTiles!=null){
             for (int i = 0; i < additionalKnownTiles.size(); i++) {

                 arrayNumber.set( additionalKnownTiles.get(i)-1, arrayNumber.get(additionalKnownTiles.get(i)-1) -1);
                 numberOfUnkownTiles--;
         }}
         for (int i = 0; i < tilesHand.size(); i++) {

             arrayNumber.set( tilesHand.get(i)-1  , arrayNumber.get(tilesHand.get(i)-1) -1);
             numberOfUnkownTiles--;
         }
         for (int i = 0; i < tilesBoard.size(); i++) {

             arrayNumber.set(tilesBoard.get(i)-1, arrayNumber.get(tilesBoard.get(i)-1) -1);
             numberOfUnkownTiles--;
         }
         for (int i = 0; i < arrayNumber.size(); i++) {
             arrayProb.add((arrayNumber.get(i)).doubleValue()/numberOfUnkownTiles);
         }
         return arrayProb;
    }

    private ArrayList<Integer> getPile(){
        ArrayList<Integer> allTilesNotPile = CustomUtility.decompose(this.board);
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

    public Node getRoot(){
        return this.root;
    }
}
