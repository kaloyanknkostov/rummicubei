package com.MCTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class ActionSpaceGenerator {

    private ArrayList<ArrayList<ArrayList<Integer>>> resultingBoards;
    private ArrayList<ArrayList<Integer>> startingBoard;
    private ArrayList<Integer> startingRack;
    private ArrayList<ArrayList<Integer>> allPossibleSets;
    private ArrayList<ArrayList<Integer>> possibleSets;
    private  ArrayList<Integer> availableTilesStart;
    private static ActionSpaceGenerator instance;


    public ActionSpaceGenerator(ArrayList<ArrayList<Integer>> board, ArrayList<Integer> rack){
        System.out.println("IN Action");
        //allPossibleSets= AllSetGenerator.generateAllSets();
        // get all possible sets here
        this.resultingBoards = new ArrayList<>();
        this.startingBoard = board;
        this.startingRack = rack;
        this.possibleSets = possibleSets(this.startingRack,decompose(board));
        //probably put this somewhere else
        availableTilesStart = new ArrayList<>();
        for(Integer tile: startingRack){
            availableTilesStart.add(tile);
        }
        for(Integer tile: decompose(startingBoard)){
            availableTilesStart.add(tile);
        }
        ArrayList<ArrayList<Integer>> beginningBoard = new ArrayList<>();
        createAllMoves(beginningBoard, availableTilesStart, 0);
        //now all moves are generated and the result can just be accesed through the get functions
    }

    
    private void createAllMoves(ArrayList<ArrayList<Integer>> currentBoard, ArrayList<Integer> availableTiles, int lastCheckedSet){
        // if all sets have been checked return
        if(lastCheckedSet == this.possibleSets.size()){
            return;
        }

        for(int i = lastCheckedSet ; i < this.possibleSets.size();i++){
            // If cannot create set with tiles go to next one
            if(!canCreateSet(availableTiles, possibleSets.get(i))){
                continue;
            }
            // Create copy of board so changes in this iteration of the loop are unique to the next iteration
            // Pass this copy as a reference in the recursion
            ArrayList<ArrayList<Integer>> currentBoardCopy = deepCopy(currentBoard);
            currentBoardCopy.add(this.possibleSets.get(i));
            ArrayList<Integer> currentAvailableTiles = new ArrayList<>(availableTiles);
            customRemove(currentAvailableTiles, this.possibleSets.get(i));

            //now check if the board is valid
            if(validBoard(currentBoardCopy)){

                // Add board and racks to the results as board is valid
                resultingBoards.add(currentBoardCopy);

            }
            createAllMoves(currentBoardCopy, currentAvailableTiles, i);
        }
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> getResultingBoards() {
        //checks if the only element in resultingboard is the startingboard, thus that it could not play any move
        if(this.getResultingBoards().size() == 1 || decompose(this.resultingBoards.get(0)).equals(decompose(this.startingBoard))){
            // when it couldnt play anything add a set containing only -1 at the front of the only resulting board
            this.resultingBoards.get(0).add(0, new ArrayList<>(Arrays.asList(-1)));
        }
        return this.resultingBoards;
    }

    public ArrayList<ArrayList<Integer>> getPossibleSets(){
        return this.possibleSets;
    }

    // this method keeps track of the available tiles by removing all the tiles in the board from the tiles available at the start.
    private ArrayList<Integer> getAvailableTiles(ArrayList<ArrayList<Integer>> board){
        ArrayList<Integer> result = new ArrayList<>(this.availableTilesStart);
        for(ArrayList<Integer> row: board){
            customRemove(result, row);
        }
        return result;
    }


    //TODO everything below here

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



    //function to check if the board is valid. thus if all tiles that are in startingBoard are also present in new board
    //cant use sets cause there can be duplicates in our board like 2 green 4's and a set would remove one of them
    //for optimization this can maybe be done in a faster way
    private boolean validBoard(ArrayList<ArrayList<Integer>> newBoard) {
        //small check
        if(countIntegers(newBoard) < startingBoard.size()){
            return false;
        }
        ArrayList<Integer> decomposedBoard = decompose(newBoard);
        for(Integer tile: decompose(this.startingBoard)){
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

    /**
    * Generates a list of possible sets based on a combination of tiles from the starting rack and starting board.
    *
    * This method combines the tiles from the starting rack and starting board to form a list of all available tiles.
    * It then iterates through a pre-existing list of all possible sets and checks if each set can be created using
    * the combined tiles. Sets that can be created are added to the list of possible sets.
    *
    * @param startingRack   The initial set of tiles in the player's rack.
    * @param startingBoard  The initial set of tiles on the game board.
    * @return A list of possible sets that can be created using tiles from the starting rack and starting board.
    */
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


    // Helper function to check if a tile is present in a list
    private boolean containsTile(ArrayList<Integer> list, Integer tile) {
        for (Integer element : list) {

            if (element.equals(tile)) {
                return true;
            }
        }
        return false;
    }

    /**
    * Counts the total number of integers in a list of lists.
    *
    * This method iterates through each inner list in the provided list of lists and
    * calculates the total count of integers across all inner lists.
    *
    * @param listOfLists A list of lists containing integers.
    * @return The total count of integers in all the inner lists combined.
    */
    private int countIntegers(ArrayList<ArrayList<Integer>> listOfLists) {
        int count = 0;
        for (ArrayList<Integer> innerList : listOfLists) {
            count += innerList.size();
        }
        return count;
    }


}
