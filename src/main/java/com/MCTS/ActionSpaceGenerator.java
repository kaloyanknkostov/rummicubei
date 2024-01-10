package com.MCTS;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.css.SimpleStyleableDoubleProperty;



public class ActionSpaceGenerator {

    private ArrayList<ArrayList<ArrayList<Integer>>> resultingBoards;
    private ArrayList<Integer> startingBoard;
    private ArrayList<Integer> startingRack;
    private ArrayList<ArrayList<Integer>> allPossibleSets;
    private ArrayList<ArrayList<Integer>> possibleSets;
    private  ArrayList<Integer> availableTilesStart;

    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> board = new ArrayList<>();
        board.add(new ArrayList<>(Arrays.asList(1,2,3)));
        board.add(new ArrayList<>(Arrays.asList(7,8,9)));
        ArrayList<Integer> rack = new ArrayList<>(Arrays.asList(14,15,16));
        ActionSpaceGenerator myGenerator = new ActionSpaceGenerator(board, rack);
        System.out.println(myGenerator.getResultingBoards());
    }

    public ActionSpaceGenerator(ArrayList<ArrayList<Integer>> board, ArrayList<Integer> rack){
        // System.out.println("IN Action");
        ArrayList<ArrayList<Integer>> boardForActionSpace = CustomUtility.deepCopy(board);
        allPossibleSets = AllSetGenerator.generateAllSets();
        this.resultingBoards = new ArrayList<>();
        this.startingBoard = CustomUtility.decompose(boardForActionSpace);
        this.startingRack = new ArrayList<>(rack);
        this.possibleSets = CustomUtility.possibleSets(this.startingRack,this.startingBoard, this.allPossibleSets);
        //probably put this somewhere else
        this.availableTilesStart = new ArrayList<>();
        for(Integer tile: startingRack){
            this.availableTilesStart.add(tile);
        }
        for(Integer tile: startingBoard){
            this.availableTilesStart.add(tile);
        }
        createAllMoves(new ArrayList<>(), this.availableTilesStart, 0);
    }

    public void createAllMoves(ArrayList<ArrayList<Integer>> currentBoard, ArrayList<Integer> availableTiles, int lastCheckedSet){
        // if all sets have been checked return
        if(lastCheckedSet == this.possibleSets.size()){
            return;
        }
        for(int i = lastCheckedSet ; i < this.possibleSets.size(); i++){
            // If cannot create set with tiles go to next one
            if(!CustomUtility.canCreateSet(availableTiles, possibleSets.get(i))){
                continue;
            }
            // Create copy of board so changes in this iteration of the loop are unique to the next iteration
            // Pass this copy as a reference in the recursion
            ArrayList<ArrayList<Integer>> currentBoardCopy = CustomUtility.deepCopy(currentBoard);
            currentBoardCopy.add(this.possibleSets.get(i));
            ArrayList<Integer> currentAvailableTiles = new ArrayList<>(availableTiles);
            currentAvailableTiles.removeAll(this.possibleSets.get(i));

            // Remove tiles in the set in our rack and available tiles list
            //now check if the board is valid
            if(CustomUtility.validBoard(currentBoardCopy,this.startingBoard)){
                // Add board and racks to the results as board is valid
                resultingBoards.add(currentBoardCopy);
            }
            createAllMoves(currentBoardCopy, currentAvailableTiles, i+1);
            //this can be i+1 since there is only 1 instance we can never check the same board twice
            //TODO, THE MOVING EVERYTHING TO SETS IS DONE, NOW CHECK HOW TO RECALCULATE POSSIBLESETS AT EVERY TURN!!!!!!!
        }
    }
    public ArrayList<ArrayList<Integer>> getPossibleSets(){
        return this.possibleSets;
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> getResultingBoards() {
        return resultingBoards;

    }
}
