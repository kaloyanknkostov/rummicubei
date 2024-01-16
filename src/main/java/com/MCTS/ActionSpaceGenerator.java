package com.MCTS;

import java.util.*;

import javafx.css.SimpleStyleableDoubleProperty;



public class ActionSpaceGenerator {

    private ArrayList<ArrayList<ArrayList<Integer>>> resultingBoards;
    private ArrayList<Integer> startingBoard;
    private ArrayList<Integer> startingRack;
    private ArrayList<ArrayList<Integer>> allPossibleSets;
    private HashMap<ArrayList<Integer>,HashSet<ArrayList<Integer>>> conflicts;
    private ArrayList<ArrayList<Integer>> possibleSets;
    private ArrayList<Integer> availableTilesStart;



    public ActionSpaceGenerator(ArrayList<ArrayList<Integer>> board, ArrayList<Integer> rack){
        ArrayList<ArrayList<Integer>> boardForActionSpace = CustomUtility.deepCopy(board);
        this.allPossibleSets = AllSetGenerator.getInstance().getAllSets();
        this.conflicts = ConflictingSets.getInstance().getAllConflicts();
        this.resultingBoards = new ArrayList<>();
        this.startingBoard = CustomUtility.decompose(boardForActionSpace);
        this.startingRack = new ArrayList<>(rack);
        this.possibleSets = CustomUtility.possibleSets(this.startingRack,this.startingBoard, this.allPossibleSets);
        //now this.possibleConflicts contains only the conflicts for possiblesets
        this.availableTilesStart = new ArrayList<>();
        for(Integer tile: startingRack){
            this.availableTilesStart.add(tile);
        }
        for(Integer tile: startingBoard){
            this.availableTilesStart.add(tile);
        }
        createAllMoves(new ArrayList<>(), this.possibleSets);
    }

    public static void main(String[] args) {
        // Example board and rack for testing
        ArrayList<ArrayList<Integer>> exampleBoard = new ArrayList<>(new ArrayList(Arrays.asList(1,2,3,4,5)));

        // Populate exampleBoard with your data

        ArrayList<Integer> exampleRack = new ArrayList<>();
        for(Integer i = 8; i < 16; i ++){
            exampleRack.add(i);
        }
        // Populate exampleRack with your data
        // Create an instance of ActionSpaceGenerator
        ActionSpaceGenerator actionSpaceGenerator = new ActionSpaceGenerator(exampleBoard, exampleRack);

        // Get the resulting boards
        ArrayList<ArrayList<ArrayList<Integer>>> resultingBoards = actionSpaceGenerator.getResultingBoards();
        System.out.println("resulted in: " + resultingBoards.size() + " boards");
        // Print the resulting boards for testing purposes
        for (ArrayList<ArrayList<Integer>> board : resultingBoards) {
            System.out.println("Board:");
            for (ArrayList<Integer> set : board) {
                System.out.println("\t" + set);
            }
            System.out.println("--------");
        }
    }

    private void createAllMoves(ArrayList<ArrayList<Integer>> currentBoard ,ArrayList<ArrayList<Integer>> setsNoConflicts){
        if(setsNoConflicts.isEmpty()){
            return;
        }
        for(ArrayList<Integer> rummikubSet: setsNoConflicts){
            ArrayList<ArrayList<Integer>> newBoard = CustomUtility.deepCopy(currentBoard);
            ArrayList<ArrayList<Integer>> newConflicts = CustomUtility.deepCopy(setsNoConflicts);
            newBoard.add(rummikubSet);
            newConflicts.removeIf(this.conflicts.get(rummikubSet)::contains);
            if(!forwardCheck(newBoard, newConflicts)){
                continue;
            }
            if(CustomUtility.validBoard(newBoard, this.startingBoard)){
                this.resultingBoards.add(newBoard);
            }
            createAllMoves(newBoard, newConflicts);
        }
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> getResultingBoards() {
        return resultingBoards;
    }

    public ArrayList<ArrayList<Integer>> sortPossibleSets(ArrayList<ArrayList<Integer>> possibleSets, ArrayList<Integer> board) {
        // First checks how many tiles from the possible sets are already on the board, then creates a ArrayList that
        // Maps the index in possibleSet to the amount of tiles that it has on the board.
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        HashMap<Integer, Stack<Integer>> amountOfTilesMap = new HashMap<>(); // Maps K: amount of the tiles on the board - > V: set index
        for (int setIndex = 0; setIndex < possibleSets.size(); setIndex++) {
            amountOfTilesMap.put(setIndex, new Stack<>());
        }
        for (int setIndex = 0; setIndex < possibleSets.size(); setIndex++) { // Check each set
            ArrayList<Integer> currentSet = possibleSets.get(setIndex);
            int tilesOnTheBoard = 0;
            for (int tileIndex = 0; tileIndex < currentSet.size(); tileIndex++) { // Check each tile in each set
                int currentTile = currentSet.get(tileIndex);
                if (board.contains(currentTile)) { // Check if the tile is on the board
                    tilesOnTheBoard += 1;
                }
            }
            amountOfTilesMap.get(tilesOnTheBoard).push(setIndex); // Map K: amount of the tiles on the board - > V: set
        }
        List<Integer> keysList = new ArrayList<>(amountOfTilesMap.keySet()); // convert the keys of the dict to a list

        for (int i = keysList.size() - 1; i >= 0; i--) { // Get each entry in the map in reversed order
            int key = keysList.get(i);
            Stack<Integer> currentDictEntry = amountOfTilesMap.get(key);
            while (!currentDictEntry.isEmpty()) { // For each entry add it to the result
                result.add(possibleSets.get(currentDictEntry.pop()) );
            }
        }
        return result;
    }

    public ArrayList<ArrayList<Integer>> getPossibleSets() {
        return possibleSets;
    }
    /**
    * Performs forward checking to determine if the placement of pieces on the new board
    * is consistent with the constraints represented by the new conflicts.
    *
    * @param newBoard     The new board configuration containing placements of pieces.
    * @param newConflicts Represents the tiles which are still able to be added
    * @return True if the placement is consistent and does not violate any constraints,
    *         false otherwise.
    */
    public boolean forwardCheck(ArrayList<ArrayList<Integer>> newBoard,ArrayList<ArrayList<Integer>> newConflicts){
        ArrayList<Integer> allPieces =new ArrayList<>();
        for (ArrayList<Integer> set:newBoard){
            allPieces.addAll(set);
        }
        for (ArrayList<Integer> set:newConflicts){
            allPieces.addAll(set);
        }
        ArrayList<Integer> copy=new ArrayList<>(this.startingBoard);
        copy.removeAll(allPieces);
        return copy.isEmpty();
    }
}
