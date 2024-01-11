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
    private HashMap<ArrayList<Integer>,HashSet<Integer>> conflicts;
    private ArrayList<ArrayList<Integer>> possibleSets;
    private HashMap<ArrayList<Integer>,HashSet<Integer>> possibleConflicts;
    private ArrayList<Integer> availableTilesStart;

    public ActionSpaceGenerator(ArrayList<ArrayList<Integer>> board, ArrayList<Integer> rack){
        ArrayList<ArrayList<Integer>> boardForActionSpace = CustomUtility.deepCopy(board);
        this.allPossibleSets = AllSetGenerator.generateAllSets();
        this.conflicts = ConflictingSets.getAllConflicts();
        this.possibleConflicts = new HashMap<>();
        this.resultingBoards = new ArrayList<>();
        this.startingBoard = CustomUtility.decompose(boardForActionSpace);
        this.startingRack = new ArrayList<>(rack);
        this.possibleSets = CustomUtility.possibleSets(this.startingRack,this.startingBoard, this.allPossibleSets);
        for(ArrayList<Integer> set: this.possibleSets){
            if(conflicts.containsKey(set)){
                this.possibleConflicts.put(set, conflicts.get(set));
            }
        }
        //now this.possibleConflicts contains only the conflicts for possiblesets
        this.availableTilesStart = new ArrayList<>();
        for(Integer tile: startingRack){
            this.availableTilesStart.add(tile);
        }
        for(Integer tile: startingBoard){
            this.availableTilesStart.add(tile);
        }
    }

    private void createAllMoves(){
        
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> getResultingBoards() {
        return resultingBoards;

    }
}
