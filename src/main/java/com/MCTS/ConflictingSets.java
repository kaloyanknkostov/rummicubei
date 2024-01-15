package com.MCTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

//to acces this class class write ConflictingSets.getInstance().getAllConflicts();

public class ConflictingSets {
    private static ConflictingSets instance;
    private static HashMap<ArrayList<Integer>,HashSet<ArrayList>> conflictingSets;

    public static ConflictingSets getInstance() {
        if (instance == null) {
            instance = new ConflictingSets();
        }
        return instance;
    }

    private ConflictingSets() {
        conflictingSets = generateAllConflicts();
    }
    
    public HashMap<ArrayList<Integer>,HashSet<ArrayList>> getAllConflicts() {
        return conflictingSets;
    }

    public HashMap<ArrayList<Integer>,HashSet<ArrayList>> generateAllConflicts(){
        ArrayList<ArrayList<Integer>> allPossibleSets = AllSetGenerator.getInstance().getAllSets();
        for(int i  = 0; i < allPossibleSets.size(); i++){
            conflictingSets.put(allPossibleSets.get(i), new HashSet<>());
            for(int j = 0; j < allPossibleSets.size(); j ++){
                if(hasCommonTile(allPossibleSets.get(i), allPossibleSets.get(j))){
                    conflictingSets.get(allPossibleSets.get(i)).add(allPossibleSets.get(j));
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
