package com.MCTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javafx.css.SimpleStyleableDoubleProperty;



public class ActionSpaceGenerator {

    private ArrayList<ArrayList<ArrayList<Integer>>> resultingBoards;
    private ArrayList<Integer> startingBoard;
    private ArrayList<Integer> startingRack;
    private ArrayList<ArrayList<Integer>> allPossibleSets;
    private HashMap<ArrayList<Integer>,HashSet<ArrayList>> conflicts;
    private ArrayList<ArrayList<Integer>> possibleSets;
    private HashMap<ArrayList<Integer>,HashSet<ArrayList>> possibleConflicts;
    private ArrayList<Integer> availableTilesStart;

    public ActionSpaceGenerator(ArrayList<ArrayList<Integer>> board, ArrayList<Integer> rack){
        ArrayList<ArrayList<Integer>> boardForActionSpace = CustomUtility.deepCopy(board);
        this.allPossibleSets = AllSetGenerator.generateAllSets();
        this.conflicts = ConflictingSets.getAllConflicts();
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
        ArrayList<ArrayList<Integer>> exampleBoard = new ArrayList<>();
        exampleBoard.add(new ArrayList<>(Arrays.asList(1,2,3)));
        // Populate exampleBoard with your data
        
        ArrayList<Integer> exampleRack = new ArrayList<>();
        exampleBoard.add(new ArrayList<>(Arrays.asList(5,6,4,7)));
        // Populate exampleRack with your data
        
        // Create an instance of ActionSpaceGenerator
        ActionSpaceGenerator actionSpaceGenerator = new ActionSpaceGenerator(exampleBoard, exampleRack);

        // Get the resulting boards
        ArrayList<ArrayList<ArrayList<Integer>>> resultingBoards = actionSpaceGenerator.getResultingBoards();

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
        for(ArrayList<Integer> rummukubSet: setsNoConflicts){
            ArrayList<ArrayList<Integer>> newBoard = CustomUtility.deepCopy(currentBoard);
            ArrayList<ArrayList<Integer>> newConflicts = CustomUtility.deepCopy(setsNoConflicts);
            newBoard.add(rummukubSet);
            newConflicts.removeAll(this.conflicts.get(rummukubSet));
            //if(!forwardCheck(newBoard, newConflicts)){
            //    return;
            //}
            if(CustomUtility.validBoard(newBoard, this.startingBoard)){
                this.resultingBoards.add(newBoard);
            }
            createAllMoves(newBoard, newConflicts);
        }
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> getResultingBoards() {
        return resultingBoards;
    }
}
