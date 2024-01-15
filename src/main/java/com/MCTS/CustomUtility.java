package com.MCTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomUtility {


    /**
     * Counts the total number of integers in a list of lists.
     *
     * This method iterates through each inner list in the provided list of lists and
     * calculates the total count of integers across all inner lists.
     *
     * @param listOfLists A list of lists containing integers.
     * @return The total count of integers in all the inner lists combined.
     */
    public static int countIntegers(ArrayList<ArrayList<Integer>> listOfLists) {
        int count = 0;
        for (ArrayList<Integer> innerList : listOfLists) {
            count += innerList.size();
        }
        return count;
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
    public static ArrayList<Integer> decompose(ArrayList<ArrayList<Integer>> board){
        ArrayList<Integer> result = new ArrayList<>();
        for(ArrayList<Integer> row: board){
            for(Integer tile: row){
                result.add(tile);
            }
        }
        return result;
    }



    //function to check if the board is valid. thus if all tiles that are in startingBoard are also present in new board
    public static boolean validBoard(ArrayList<ArrayList<Integer>> newBoard,ArrayList<Integer> startingBoard) {
        // Convert both starting board and new board to sets
        Set<Integer> startingBoardSet = new HashSet<>(startingBoard);
        Set<Integer> newBoardSet = new HashSet<>();

        // Iterate through each row in the new board and add its elements to the new board set
        for (ArrayList<Integer> row : newBoard) {
            newBoardSet.addAll(row);
        }

        // Remove new board elements from starting board
        startingBoardSet.removeAll(newBoardSet);

        // Check if starting board is empty after removal
        return startingBoardSet.isEmpty();        
    }


    // this method keeps track of the available tiles by removing all the tiles in the board from the tiles available at the start.
    public static ArrayList<Integer> getAvailableTiles(ArrayList<ArrayList<Integer>> board, ArrayList<Integer> availableTilesStart){
        // Convert both board and availableTilesStart to sets
        Set<Integer> boardSet = new HashSet<>();
        Set<Integer> availableTilesSet = new HashSet<>(availableTilesStart);

        // Iterate through each row in the board and add its elements to the board set
        for (ArrayList<Integer> row : board) {
            boardSet.addAll(row);
        }

        // Remove board elements from availableTilesSet
        availableTilesSet.removeAll(boardSet);

        // Convert the result back to ArrayList
        return new ArrayList<>(availableTilesSet);        
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
     **/
    public static boolean canCreateSet(ArrayList<Integer> array, ArrayList<Integer> set) {
        if (array.isEmpty() || set.isEmpty()) {
            return false;
        }
        Set<Integer> set1 = new HashSet<>(array);
        Set<Integer> set2 = new HashSet<>(set);
        return set1.containsAll(set2);
    }

    //this function takes in a board and gets the difference in tiles from the old one
    //check for empty
    public static ArrayList<Integer> getDifference(ArrayList<ArrayList<Integer>> newBoard, ArrayList<ArrayList<Integer>> oldBoard){

        ArrayList<Integer> newBoardDecomposed = decompose(newBoard);
        ArrayList<Integer> oldBoardDecomposed = decompose(oldBoard);
        Set<Integer> setNew = new HashSet<>(newBoardDecomposed);
        Set<Integer> setOld = new HashSet<>(oldBoardDecomposed);
        setNew.removeAll(setOld); // this is now a set containing only ones that are in new but not in old
        return new ArrayList<>(setNew);
    }

    /**
    * Removes sets from the provided list that are found in the conflicts set.
    * 
    * @param setsLeft  The list of sets from which conflicts will be removed.
    * @param conflicts The set of sets representing conflicts to be removed from setsLeft.
    */
    public static void removeConflicts(ArrayList<ArrayList<Integer>> setsLeft, Set<ArrayList<Integer>> conflicts) {
        /*
         * Iterate through the sets in setsLeft.
         * If a set is found in the conflicts set, remove it from setsLeft.
         */
        for (ArrayList<Integer> set : setsLeft) {
            if (conflicts.contains(set)) {
                setsLeft.remove(set);
            }
        }
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
    public static ArrayList<ArrayList<Integer>> possibleSets(ArrayList<Integer> startingRack, ArrayList<Integer> startingBoard, ArrayList<ArrayList<Integer>> allPossibleSets){
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

    /**
     * Creates a deep copy of a 2D ArrayList of integers.
     *
     * @param original The 2D ArrayList to be copied.
     * @return A deep copy of the input 2D ArrayList.
     */
    public static ArrayList<ArrayList<Integer>> deepCopy(ArrayList<ArrayList<Integer>> original) {
        ArrayList<ArrayList<Integer>> copy = new ArrayList<>();

        for (ArrayList<Integer> innerList : original) {
            // Create a new ArrayList for each inner list
            ArrayList<Integer> innerCopy = new ArrayList<>(innerList);
            copy.add(innerCopy);
        }

        return copy;
    }

}