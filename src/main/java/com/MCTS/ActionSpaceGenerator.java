package com.MCTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class ActionSpaceGenerator {

    private ArrayList<ArrayList<ArrayList<Integer>>> resultingBoards;
    private ArrayList<ArrayList<Integer>> resultingRacks;
    private ArrayList<Integer> startingBoard;
    private ArrayList<Integer> startingRack;
    private ArrayList<ArrayList<Integer>> allPossibleSets;

    public ActionSpaceGenerator(ArrayList<Integer> board, ArrayList<Integer> rack){
        System.out.println("IN Action");
        allPossibleSets=AllSetGenerator.generateAllSets();
        this.resultingBoards = new ArrayList<>();
        this.resultingRacks = new ArrayList<>();
        this.startingBoard = board;
        this.startingRack = rack;
    }
    
    
    public void createAllMoves(ArrayList<ArrayList<Integer>> currentBoard, ArrayList<Integer> availableTiles, ArrayList<Integer> currentRack, int lastCheckedSet){
        //add a base case for when it should return. either when currentrack is empty or when all sets have been checked
        //TODO make this better
        // it should only loop through the sets that can be formed so we have to make a method for this
        if(currentRack.size() <3 || lastCheckedSet == allPossibleSets.size()-1){
            return;
        }
        for(int i = lastCheckedSet; i < allPossibleSets.size();i++){
            if(canCreateSet(allPossibleSets.get(i), availableTiles)){
                // if yes then add this set to the current board that is getting built and remove it from the possibletiles
                currentBoard.add(allPossibleSets.get(i));
                //make sure it doesnt remove duplicates, cause if blue 2 is in the rack and board we need to remove it from tilesleft only once not twice
                //this will work but not efficient cause it will continue an impoosible road too far. work this out
                //probably have to make a custom function for this.
                //TODO do this
                availableTiles.removeAll(allPossibleSets.get(i));
                currentRack.removeAll(allPossibleSets.get(i));
                //now check if the board is valid
                if(validBoard(currentBoard)){
                    //add it to the action space but keeping going in the tree
                    resultingBoards.add(currentBoard);
                    resultingRacks.add(currentRack);
                }
                createAllMoves(currentBoard, availableTiles, currentRack, lastCheckedSet);
            }
        }
    }

    //need a function that checks if i can make a set, eg. an arraylist of ints from an arraylists of ints
    private boolean canCreateSet(ArrayList<Integer> array, ArrayList<Integer> set) {
        Set<Integer> arraySet = new HashSet<>(array);
        Set<Integer> setAsSet = new HashSet<>(set);
        return arraySet.containsAll(setAsSet);
    }

    //need a function that takes as an input an arraylist and removes all elements from it from the other input arraylist


    //function to check if the board is valid. thus if all tiles that are in startingBoard are also present in new board
    //cant use sets cause there can be duplicates in our board like 2 green 4's and a set would remove one of them
    //for optimization this can maybe be done in a faster way
    private boolean validBoard(ArrayList<ArrayList<Integer>> newBoard) {
        //small check
        if(countIntegers(newBoard) <= startingBoard.size()){
            return false;
        }
        for(ArrayList<Integer> row: newBoard){
            for (Integer tile : row) {
                if (!containsTile(startingBoard, tile)) {
                    // If a tile in the new board is not present in the starting board, the board is not valid
                    return false;
                }
            }
        }
        // All tiles in the new board are present in the starting board, so the board is valid
        return true;
    }

    private ArrayList<ArrayList<Integer>> possibleSets(ArrayList<Integer> tiles){
        ArrayList<ArrayList<Integer>> possibleSets = new ArrayList<>();
        for(ArrayList<Integer> set: allPossibleSets){
            if(canCreateSet(tiles, set)){
                possibleSets.add(set);
            }
        }
        return possibleSets;
    }


    // Helper function to check if a tile is present in a list
    private boolean containsTile(ArrayList<Integer> list, Integer tile) {
        for (Integer element : list) {

            if (element.equals(tile)) {
                return true;
            }
        }
        return false;
    }
    private int countIntegers(ArrayList<ArrayList<Integer>> listOfLists) {
        int count = 0;
        for (ArrayList<Integer> innerList : listOfLists) {
            count += innerList.size();
        }
        return count;
    }
}
