package com.MCTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class ActionSpaceGenerator {

    private ArrayList<ArrayList<ArrayList<Integer>>> resultingBoards;
    private ArrayList<ArrayList<Integer>> resultingRacks;
    private ArrayList<Integer> startingBoard;
    private ArrayList<Integer> startingRack;
    private ArrayList<ArrayList<Integer>> allPossibleSets;
    private ArrayList<ArrayList<Integer>> possibleSets;
    private  ArrayList<Integer> availableTilesStart;


    

    public ActionSpaceGenerator(ArrayList<Integer> board, ArrayList<Integer> rack){
        System.out.println("IN Action");
        allPossibleSets= AllSetGenerator.generateAllSets();

        this.resultingBoards = new ArrayList<>();
        this.resultingRacks = new ArrayList<>();
        this.startingBoard = board;
        this.startingRack = rack;
        this.possibleSets = possibleSets(this.startingRack,this.startingBoard);
        //probably put this somewhere else
        availableTilesStart = new ArrayList<>();
        for(Integer tile: startingRack){
            availableTilesStart.add(tile);
        }
        for(Integer tile: startingBoard){
            availableTilesStart.add(tile);
        }
    }

    public static void main(String[] args) {
        ArrayList<Integer> rack = new ArrayList<>(Arrays.asList(1, 2, 3, 14, 15, 16));
        ArrayList<Integer> startingBoard = new ArrayList<>(Arrays.asList(5, 6, 7));
        ArrayList<ArrayList<Integer>> board = new ArrayList<>();
        ActionSpaceGenerator myGenerator = new ActionSpaceGenerator(startingBoard,rack);
        //System.out.println(myGenerator.getPossibleSets());
        myGenerator.createAllMoves(board, rack, 0);
        System.out.println(myGenerator.getResultingBoards());

    }
    
    
    public void createAllMoves(ArrayList<ArrayList<Integer>> currentBoard, ArrayList<Integer> currentRack, int lastCheckedSet){
        ArrayList<Integer> availableTiles = getAvailableTiles(currentBoard);

        //System.out.println(availableTiles);
        //System.out.println(currentBoard);
        //TODO make this better
        if(lastCheckedSet == this.possibleSets.size()-1){
            return;
        }
        for(int i = lastCheckedSet ; i < this.possibleSets.size();i++){
            if(canCreateSet(availableTiles, possibleSets.get(i))){
                System.out.println("tiles available: "+availableTiles);
                // if yes then add this set to the current board that is getting built and remove it from the possibletiles
                currentBoard.add(this.possibleSets.get(i));
                //System.out.println("removes tiles: " + possibleSets.get(i));

                customRemove(availableTiles, this.possibleSets.get(i));
                customRemove(currentRack, this.possibleSets.get(i));
                //System.out.println("tiles after removing: "+availableTiles);
                //now check if the board is valid
                if(validBoard(currentBoard)){
                    ArrayList<ArrayList<Integer>> currentValidBoard = deepCopy(currentBoard); //cause otherwise its pass by reference
                    ArrayList<Integer> currentValidRack = new ArrayList<>(currentRack);
                    System.out.println(currentBoard);
                    //add it to the action space but keeping going in the tree
                    resultingBoards.add(currentValidBoard);
                    resultingRacks.add(currentValidRack);
                    i = i - 1; //if it could add it try the same set again
                }
                createAllMoves(currentBoard, currentRack, i+1);
            }
        }
    }

    // this method keeps track of the available tiles by removing all the tiles in the board from the tiles available at the start.
    private ArrayList<Integer> getAvailableTiles(ArrayList<ArrayList<Integer>> board){
        ArrayList<Integer> result = new ArrayList<>(this.availableTilesStart);
        for(ArrayList<Integer> row: board){
            customRemove(result, row);
        }
        return result;
    }

    /**
    * Creates a deep copy of a 2D ArrayList of integers.
    *
    * @param original The 2D ArrayList to be copied.
    * @return A deep copy of the input 2D ArrayList.
     */
    private ArrayList<ArrayList<Integer>> deepCopy(ArrayList<ArrayList<Integer>> original) {
        ArrayList<ArrayList<Integer>> copy = new ArrayList<>();

        for (ArrayList<Integer> innerList : original) {
            // Create a new ArrayList for each inner list
            ArrayList<Integer> innerCopy = new ArrayList<>(innerList);
            copy.add(innerCopy);
        }

        return copy;
    }

    private static void customRemove(List<Integer> list, ArrayList<Integer> elementsToRemove) {
        for (Integer element : elementsToRemove) {
            list.remove(element);
        }
    }
    /**
    * Checks whether a set can be created from a given array.
    *
    * This method compares two ArrayLists, 'array' and 'set', and determines if
    * the elements of 'set' can form a valid set (i.e., all elements of 'set'
    * are present in 'array' at least once).
    *
    * @param array The ArrayList of integers from which the set is checked.
    * @param set   The ArrayList of integers representing the set to be checked.
    * @return      {@code true} if the set can be created from the array,
    *              {@code false} otherwise.
    */
    private static boolean canCreateSet(ArrayList<Integer> array, ArrayList<Integer> set) {
        if(array.isEmpty()){
            return false;
        }
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
        if(countIntegers(newBoard) < startingBoard.size()){
            return false;
        }
        ArrayList<Integer> decomposedBoard = decompose(newBoard);
        for(Integer tile: this.startingBoard){
            if(!decomposedBoard.contains(tile)){
                return false;
            }
        }
        // All tiles in the new board are present in the starting board, so the board is valid
        return true;
    }

    /**
    * Decomposes a 2D ArrayList representing a board into a 1D ArrayList of integers.
    *
    * This method takes a 2D ArrayList, 'board', and flattens it into a 1D ArrayList, 'result',
   * by iterating through each row and each element in the board and adding them to the result list.
    *
    * @param board The 2D ArrayList representing the board to be decomposed.
    * @return      A 1D ArrayList containing all the integers from the input board.
    */
    private ArrayList<Integer> decompose(ArrayList<ArrayList<Integer>> board){
        ArrayList<Integer> result = new ArrayList<>();
        for(ArrayList<Integer> row: board){
            for(Integer tile: row){
                result.add(tile);
            }
        }
        return result;
    }

    private ArrayList<ArrayList<Integer>> possibleSets(ArrayList<Integer> startingRack, ArrayList<Integer> startingBoard){
        ArrayList<Integer> allTiles = new ArrayList<>();
        for(Integer tile: startingRack){
            allTiles.add(tile);
        }
        for(Integer tile: startingBoard){
            allTiles.add(tile);
        }
        ArrayList<ArrayList<Integer>> possibleSets = new ArrayList<>();
        for(ArrayList<Integer> set: allPossibleSets){
            if(canCreateSet(allTiles, set)){
                possibleSets.add(set);
            }
        }
        return possibleSets;
    }

    public ArrayList<ArrayList<Integer>> getPossibleSets(){
        return this.possibleSets;
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

    public ArrayList<ArrayList<ArrayList<Integer>>> getResultingBoards() {
        return resultingBoards;
    }
}
