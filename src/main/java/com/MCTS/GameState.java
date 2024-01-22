package com.MCTS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameState {
    //we have to account for draws idk how

    // Represents the current state of the game
    // includes the board and all the hands of the players (predicted)
    // and tiles currently in the pile
    // includes current active player (who can do the next move)
    private ArrayList<Integer>[] racks = new ArrayList[2];
    private ArrayList<Integer> pile;
    private ArrayList<ArrayList<Integer>> board;
    private boolean[] couldntPlay = new boolean[2];
    private int winner;
    private Random random;
    private int scalar;

    public GameState(ArrayList<Integer> rackPlayer1, ArrayList<Integer> rackPlayer2, ArrayList<ArrayList<Integer>>board, ArrayList<Integer> pile, int scalar){
        this.racks[0] = rackPlayer1;
        this.racks[1] = rackPlayer2;
        this.board = board;
        this.pile = pile;
        this.random = new Random();
        this.couldntPlay[0] = false;
        this.couldntPlay[1] = false;
        this.winner = -1;
        this.scalar=scalar;
    }

    // kinda wonky code but if nothing happened returns a 0,
    // if output is 1 someone won the game
    // if output is 2 the game ended in a draw
    public int updateGameState(ArrayList<ArrayList<Integer>> newBoard, int playerIndex){
       // System.out.println("the board we want to update to:" +newBoard);
      //  System.out.println("Rack player 0 "+ racks[0]);
       // System.out.println("rack player 1 "+ racks[1]);
       // System.out.println("Current player" + playerIndex);
       // System.out.println("The rack before: "+this.racks[playerIndex]);

        this.racks[playerIndex].removeAll(CustomUtility.getDifference(newBoard, this.board));
       // System.out.println("Difference: "+ CustomUtility.getDifference(newBoard, this.board));
       // System.out.println("The rack after: "+this.racks[playerIndex]);
        //check if the player whos move it was now has an empty rack
        if(racks[playerIndex].isEmpty()){
            this.winner = playerIndex;
            this.board = newBoard;
            return 1;
        }
        else if (newBoard.get(0).get(0) == -1){
            //this means that this player could not play so we dont modify the board
            //then we check if there are tiles on the pile, if yes we can simply draw
            if(!this.pile.isEmpty()){
                drawCard(playerIndex);
                //if there is none on the pile however we set that this player coulnt play and dont anything
            } else {
                this.couldntPlay[playerIndex] = true;
                //now if the previous player also could not play the game ends in a draw
                if(this.couldntPlay[(playerIndex+1)%2]){
                    //TODO only works for two players
                    //this means that the current player could not play and the player before it also couldnt play thus its a draw
                    //check which player won
                    int boardDiff = CustomUtility.sumOfRack(this.racks[0])-CustomUtility.sumOfRack(this.racks[1]);
                   /*  if(boardDiff > 0){
                        this.winner = 0;
                    } else if (boardDiff < 0) {
                        this.winner = 1;
                    }*/
                    this.winner=2;
                    return 2;
                }
            }
        }
        else if (equals(CustomUtility.decompose(newBoard),CustomUtility.decompose(this.board))){
            if(!this.pile.isEmpty()){
                drawCard(playerIndex);
            }
            this.couldntPlay[playerIndex] = false;
            //if the game did not finish or one player did not just draw, then one player played a move and we have to update his
            //rack and the board
        } else {
            this.board = newBoard;
            this.couldntPlay[playerIndex] = false;
        }
        //System.out.println("the updateed board: " + this.board);
        return 0;
    }

    public GameState copy(){
        return new GameState(new ArrayList<>(this.racks[0]), new ArrayList<>(this.racks[1]), CustomUtility.deepCopy(this.board), new ArrayList<>(this.pile),scalar);
    }

    public ArrayList<Integer>[] getRacks(){
        return this.racks;
    }
    public ArrayList<ArrayList<Integer>> getBoard(){
        return this.board;
    }

    public void setOpponentRack(ArrayList<Integer> opponentRack){
        this.racks[1] = opponentRack;
    }

    // -1 if noone won yet
    public int getWinner(){
        return this.winner;
    }

    private void drawCard(int playerIndex){
        int x = this.random.nextInt(this.pile.size());
        Integer tile = Integer.valueOf(this.pile.get(x));
        this.pile.remove(tile);
        this.racks[playerIndex].add(tile);
    }

    public static boolean equals(ArrayList<Integer> one, ArrayList<Integer> two){
        if(one.size()!= two.size())return false;
        Collections.sort(one);
        Collections.sort(two);
        for (int i = 0; i <one.size() ; i++) {
            if(!Objects.equals(one.get(i), two.get(i))){
                return false;
            }
        }
        return true;
    }

}
