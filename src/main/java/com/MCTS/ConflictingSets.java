package com.MCTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

//to acces this class class write ConflictingSets.getInstance().getAllConflicts();

public class ConflictingSets {
    private static ConflictingSets instance;
    private static HashMap<ArrayList<Integer>,HashSet<ArrayList<Integer>>> conflictingSets;

    public static ConflictingSets getInstance() {
        if (instance == null) {
            instance = new ConflictingSets();
        }
        return instance;
    }

    private ConflictingSets() {
        conflictingSets = generateAllConflicts();
    }
    
    public HashMap<ArrayList<Integer>,HashSet<ArrayList<Integer>>> getAllConflicts() {
        return conflictingSets;
    }

    private HashMap<ArrayList<Integer>,HashSet<ArrayList<Integer>>> generateAllConflicts(){
        ArrayList<ArrayList<Integer>> allPossibleSets = AllSetGenerator.getInstance().getAllSets();
        for(int i  = 0; i < allPossibleSets.size(); i++){
            conflictingSets.put(allPossibleSets.get(i), new HashSet<ArrayList<Integer>>());
            for(int j = 0; j < allPossibleSets.size(); j ++){
                if(i != j && hasCommonTile(allPossibleSets.get(i), allPossibleSets.get(j))){
                    conflictingSets.get(allPossibleSets.get(i).add(allPossibleSets.get(j)));
                }
            }
        }
        return conflictingSets;
    }

    private boolean hasCommonTile(ArrayList<Integer> list1, ArrayList<Integer> list2){
        Set<Integer> set1 = new HashSet<>(list1);
        Set<Integer> set2 = new HashSet<>(list2);
        set1.removeAll(set2);
        if(set1.size() == list1.size()){
            return true;
        }
        return false;
    }
}
