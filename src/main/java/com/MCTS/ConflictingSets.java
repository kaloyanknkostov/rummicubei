package com.MCTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ConflictingSets {
    private static ConflictingSets instance;
    private static HashMap<ArrayList<Integer>,HashSet<ArrayList>> ConflictingSets;

    public static ConflictingSets getInstance() {
        if (instance == null) {
            instance = new ConflictingSets();
        }
        return instance;
    }

    private ConflictingSets() {
        ConflictingSets = new HashMap<>();
        generateAllConflicts();
    }
    
    public static HashMap<ArrayList<Integer>,HashSet<ArrayList>> getAllConflicts() {
        return ConflictingSets;
    }

    private HashMap<ArrayList<Integer>,HashSet<ArrayList>> generateAllConflicts(){
        ArrayList<ArrayList<Integer>> allPossibleSets = AllSetGenerator.generateAllSets();
        for(int i  = 0; i < allPossibleSets.size()-1; i++){
            ConflictingSets.put(allPossibleSets.get(i), new HashSet<>());
            for(int j = i+1; j < allPossibleSets.size(); j ++){
                if(hasCommonTile(allPossibleSets.get(i), allPossibleSets.get(j))){
                    ConflictingSets.get(allPossibleSets.get(i)).add(allPossibleSets.get(j));
                }
            }
        }
        return ConflictingSets;
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
