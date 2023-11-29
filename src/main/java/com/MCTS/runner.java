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
        players.add(1);
        players.add(2);
        players.add(3);
        ArrayList<Integer> allTiles=new ArrayList<>();
        allTiles.add(players.get(0));
        allTiles.add(players.get(1));
        allTiles.add(players.get(2));
        ArrayList<ArrayList<Integer>> emt=new ArrayList<>();


        for(ArrayList<Integer> set:board){
            allTiles.addAll(set);
        }
        ActionSpaceGenerator actionSpaceGenerator=new ActionSpaceGenerator(allTiles,players);
        actionSpaceGenerator.createAllMoves(emt,allTiles,players,0);
        System.out.println("finished");
        ArrayList<ArrayList<ArrayList<Integer>>> actionspace=actionSpaceGenerator.getResultingBoards();
        for(ArrayList<ArrayList<Integer>>boards:actionspace){
            System.out.println("yiker");
            System.out.println(boards.toString());
        }
    }

}
