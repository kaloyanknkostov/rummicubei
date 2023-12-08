package com.MCTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState {
    //we have to account for draws idk how

    // Represents the current state of the game
    // includes the board and all the hands of the players (predicted)
    // and tiles currently in the pile
    // includes current active player (who can do the next move)
    private ArrayList<Integer>[] racks;
    private ArrayList<Integer> pile;
    private ArrayList<ArrayList<Integer>> board;
    private boolean hasFinished;
    private boolean isDraw;
    private int winner;
    private Random random;

    public GameState(ArrayList<Integer> rackPlayer1, ArrayList<Integer> rackPlayer2, ArrayList<ArrayList<Integer>>board, ArrayList<Integer> pile){
        this.racks[0] = rackPlayer1;
        this.racks[1] = rackPlayer2;
        this.board = board;
        this.pile = pile;
        this.hasFinished = false;
        this.isDraw = false;
        this.random = new Random();
    }

    //we need a method that takes in a move a player makes and the player hows move it was and updates the entire game state
    //first check if the game hasfinished
    //first check if the board was the same, and if so draw one random mo
    public void updateGameState(ArrayList<ArrayList<Integer>> newBoard, int playerIndex){
        //check if the player whos move it was now has an empty rack
        if(racks[playerIndex].isEmpty())
            hasFinished = true;
            winner = playerIndex;
        } else if (newBoard.equals(this.board)){
            drawCard(playerIndex);
        //if the game did not finish or one player did not just draw, then one player played a move and we have to update his
        //rack and the board 
        } else {
            customRemove(this.racks[playerIndex], getDifference(newBoard)); 
        }
    }

    private void drawCard(int playerIndex){
        int x = this.random.nextInt(this.pile.size());
        Integer tile = Integer.valueOf(this.pile.get(x));
        this.pile.remove(tile);
        this.racks[playerIndex].add(tile);
    }

    //this function takes in a board and gets the difference in tiles from the old one
    private ArrayList<Integer> getDifference(ArrayList<ArrayList<Integer>> newBoard){
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> decomposedOld = decompose(this.board);
        ArrayList<Integer> decomposedNew = decompose(newBoard);
        for(Integer tile: decomposedNew){
            if(!this.board.contains(tile)){
                result.add(tile);
            }
        }
        return result;
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

    private static void customRemove(List<Integer> list, ArrayList<Integer> elementsToRemove) {
        for (Integer element : elementsToRemove) {
            list.remove(element);
        }
    }

}
