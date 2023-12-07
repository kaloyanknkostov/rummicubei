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
    
     private ArrayList<ArrayList<Integer>> getBestMove(ArrayList<ArrayList<ArrayList<Integer>>> actionspace ){
        int maxSize = 0; // the biggest set of tiles we have found. 
        int currentSize = 0; // the total size of the current arraylist that we are checking 
        int keepingTrack = 0; // the index we are currently checking 
        int bestMove = 0;  // the index of the best move in the actionspace 
        int currentNumerical = 0; // the values of the best move numerically (all the tiles added)
        ArrayList<ArrayList<Integer>> maxArrayList = null; // the bast move as the array list of array lists of integers 
        for (ArrayList<ArrayList<Integer>> move : actionspace) { // loop through each moves 
           currentSize = 0;   
            for (ArrayList<Integer> set : move) {  // check each set of the move and add the size of that set to the current size
                    currentSize += set.size(); 
                }
           
            if (currentSize > maxSize) {  //if the current size is bigger than max size we replace it and update all  the values
                    maxSize = currentSize; 
                    bestMove = keepingTrack; 
                    maxArrayList = actionspace.get(bestMove); 
            } else if (currentSize == maxSize){   // if the size is the same we decide the move that gets rid of the higher valued tiles. 
                int newPossibility= 0; 
               
                for (ArrayList<Integer> set : maxArrayList) {
                    for (Integer tile : set) {
                        if (tile.intValue() % 13==0) {
                            newPossibility += 13; 
                        } else {
                         newPossibility += tile.intValue() % 13; // adds the vlaues of the tiles to get the best move poitns wise
                          // we still need to account for joker. 
                        }
                    }  
                }
                if(newPossibility> currentNumerical){
                     bestMove = keepingTrack; 
                     currentNumerical = newPossibility; 
                }
        }
            keepingTrack++; 
        }
        return actionspace.get(bestMove); 
       
        }

}
