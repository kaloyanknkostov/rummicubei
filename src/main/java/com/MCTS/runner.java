package com.MCTS;

import com.gameEngine.Board;

import java.util.ArrayList;

public class runner {
    Board board=new Board();

    public runner() {
    }


    public void start(ArrayList<ArrayList<Integer>> board){
        System.out.println("INNER");
        ArrayList<Integer> players=new ArrayList<>();
        ArrayList<Integer> newBoard=new ArrayList<>();
        ArrayList<ArrayList<Integer>> emt=new ArrayList<>();

        for(ArrayList<Integer> set:board){
            newBoard.addAll(set);
        }
        ActionSpaceGenerator actionSpaceGenerator=new ActionSpaceGenerator(newBoard,players);
        actionSpaceGenerator.createAllMoves(emt,newBoard,players,0);
        ArrayList<ArrayList<ArrayList<Integer>>> actionspace=actionSpaceGenerator.getResultingBoards();
        for(ArrayList<ArrayList<Integer>>boards:actionspace){
            System.out.println(boards.toString());
        }
    }

}
