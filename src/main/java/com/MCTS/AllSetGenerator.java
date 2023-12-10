package com.MCTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;



public class AllSetGenerator {
    
    public static ArrayList<ArrayList<Integer>> generateAllSets() {
        ArrayList<ArrayList<Integer>> allSets = new ArrayList<>();
        ArrayList<ArrayList<Integer>> runs=generateRunsWithoutJokers();
        ArrayList<ArrayList<Integer>> group=generateGroupsWithoutJokers();
        allSets.addAll(runs);
        allSets.addAll(group);
        allSets.addAll(generateRunsWithOneJoker(runs));
        allSets.addAll(generateGroupsOf4WithOneJoker(group));
        allSets.addAll(generateGroupsOf3WithOneJoker(group));
        allSets.addAll(generateRunsWithTwoJokers(runs));
        allSets.addAll(generateGroupsWithTwoJokers(group));
        return allSets;
    }
    // this is one is tested and returns the correct amount of runs
    private static ArrayList<ArrayList<Integer>> generateRunsWithoutJokers() {
        ArrayList<ArrayList<Integer>> allRunsWithoutJokers = new ArrayList<>();

        // Same color 3 in a row
        for (int color = 1; color <= 4; color++) {
            for (int start = 1; start <= 11; start++) {
                ArrayList<Integer> set = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    set.add((color - 1) * 13 + start + i);
                }
                allRunsWithoutJokers.add(set);
            }
        }

        // Same color 4 in a row
        for (int color = 1; color <= 4; color++) {
            for (int start = 1; start <= 10; start++) {
                ArrayList<Integer> set = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    set.add((color - 1) * 13 + start + i);
                }
                allRunsWithoutJokers.add(set);
            }
        }

        // Same color 5 in a row
        for (int color = 1; color <= 4; color++) {
            for (int start = 1; start <= 9; start++) {
                ArrayList<Integer> set = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    set.add((color - 1) * 13 + start + i);
                }
                allRunsWithoutJokers.add(set);
            }
        }

        return allRunsWithoutJokers;
    }

    //tested and returns the correct amount of groups, now really works
    private static ArrayList<ArrayList<Integer>> generateGroupsWithoutJokers() {
        ArrayList<ArrayList<Integer>> allGroupsWithoutJokers = new ArrayList<>();

    
        // Same number 4
        for (int num = 1; num <= 13; num++) {
            ArrayList<Integer> set = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                set.add(num + i * 13);
            }
            allGroupsWithoutJokers.add(set);
        }

        // same number 3, this is easy just take all sets of same number 4 and add 4 ones one per number colour
        ArrayList<ArrayList<Integer>> currentGroups = new ArrayList<>(allGroupsWithoutJokers);
        for(ArrayList<Integer> group: currentGroups){
            for(int i = 0; i < group.size(); i ++){
                ArrayList<Integer> newSet = new ArrayList<>(group);
                newSet.remove(i);
                allGroupsWithoutJokers.add(newSet);
            }
        }

        return allGroupsWithoutJokers;
    }

    //this works and returns correct elements
    private static ArrayList<ArrayList<Integer>> generateRunsWithOneJoker(ArrayList<ArrayList<Integer>> baseList){
        ArrayList<ArrayList<Integer>> allRunsWithOneJoker = new ArrayList<>();
        //create all variants of normal runs by putting the joker at positions 1 to n
        //this creates all runs like 
        for (ArrayList<Integer> baseSet : baseList) {
            int startingPoint;
            //this if only activates for the last run in a colour cause then it needs to add all of them
            if(baseSet.get(baseSet.size()-1) % 13 == 0){
                startingPoint = 0;
            } else {
                startingPoint = 1;
            }
            for (int jokerPos = startingPoint; jokerPos < baseSet.size(); jokerPos++) {
                ArrayList<Integer> setWithOneJoker = new ArrayList<>(baseSet);
                setWithOneJoker.set(jokerPos, 53); // Joker
                allRunsWithOneJoker.add(setWithOneJoker);
            }
        }
        return allRunsWithOneJoker;
    }

    //tested and works, returns the correct output
    private static ArrayList<ArrayList<Integer>> generateGroupsOf4WithOneJoker(ArrayList<ArrayList<Integer>> baseList){
        ArrayList<ArrayList<Integer>> allGroupsWithOneJoker = new ArrayList<>();
        //create all variants of all groups by putting the joker at positions 0 to n
        for (ArrayList<Integer> baseSet : baseList) {
            if(baseSet.size() == 3){
                continue;
            }
            for (int jokerPos = 0; jokerPos < baseSet.size(); jokerPos++) {
                ArrayList<Integer> setWithOneJoker = new ArrayList<>(baseSet);
                setWithOneJoker.set(jokerPos, 53); // Joker
                if(!checkIfExists(allGroupsWithOneJoker, setWithOneJoker)){
                    allGroupsWithOneJoker.add(setWithOneJoker);
                }
            }
        }
        return allGroupsWithOneJoker;

    }
    private static ArrayList<ArrayList<Integer>> generateGroupsOf3WithOneJoker(ArrayList<ArrayList<Integer>> baseList){
        ArrayList<ArrayList<Integer>> allGroupsWithOneJoker = new ArrayList<>();
        //create all variants of all groups by putting the joker at positions 0 to n
        for (ArrayList<Integer> baseSet : baseList) {
            if(baseSet.size() == 4){
                continue;
            }
            for (int jokerPos = 0; jokerPos < baseSet.size(); jokerPos++) {
                ArrayList<Integer> setWithOneJoker = new ArrayList<>(baseSet);
                setWithOneJoker.set(jokerPos, 53); // Joker
                if(!checkIfExists(allGroupsWithOneJoker, setWithOneJoker)){
                    allGroupsWithOneJoker.add(setWithOneJoker);
                }
            }
        }
        
        return allGroupsWithOneJoker;
    }



    private static boolean checkIfExists(ArrayList<ArrayList<Integer>> list, ArrayList<Integer> set) {
        // Create sorted copies of the sets for comparison
        ArrayList<Integer> sortedSet = new ArrayList<>(set);
        Collections.sort(sortedSet);
    
        for (ArrayList<Integer> existingSet : list) {
            // Create a sorted copy of the existing set for comparison
            ArrayList<Integer> sortedExistingSet = new ArrayList<>(existingSet);
            Collections.sort(sortedExistingSet);
    
            // Check if the sets are equal after sorting
            if (sortedExistingSet.size() == sortedSet.size() && sortedExistingSet.equals(sortedSet)) {
                return true; // Sets are equal
            }
        }
        return false; // Set not found in the list
    }

    //creates runs of 3 correctly
    //creates runs of 4 correctly
    //there is one missing of 5 idk which one but for the rest it works

    private static ArrayList<ArrayList<Integer>> generateRunsWithTwoJokers(ArrayList<ArrayList<Integer>> baseList) {
        ArrayList<ArrayList<Integer>> allRunsWithTwoJokers = new ArrayList<>();
    
        for (ArrayList<Integer> baseSet : baseList) {
            for (int jokerPos1 = 0; jokerPos1 < baseSet.size()-1; jokerPos1++) {
                for (int jokerPos2 = jokerPos1 + 1; jokerPos2 < baseSet.size(); jokerPos2++) {
                    ArrayList<Integer> setWithTwoJokers = new ArrayList<>(baseSet);
                    setWithTwoJokers.set(jokerPos1, 53); // First Joker
                    setWithTwoJokers.set(jokerPos2, 53); // Second Joker
    
                    if (!checkIfExists(allRunsWithTwoJokers, setWithTwoJokers)) {
                        allRunsWithTwoJokers.add(setWithTwoJokers);
                       // System.out.println(setWithTwoJokers);
                    }
                }
            }
        }
        return allRunsWithTwoJokers;
    }

    //creates groups of 4 with 2 jokers
    private static ArrayList<ArrayList<Integer>> generateGroupsWithTwoJokers(ArrayList<ArrayList<Integer>> baseList) {
        ArrayList<ArrayList<Integer>> allGroupsWithTwoJokers = new ArrayList<>();
    
        for (ArrayList<Integer> baseSet : baseList) {
            if(baseSet.size() == 3){
                continue;
            }
            for (int jokerPos1 = 0; jokerPos1 < baseSet.size()-1; jokerPos1++) {
                for (int jokerPos2 = jokerPos1 + 1; jokerPos2 < baseSet.size(); jokerPos2++) {
                    ArrayList<Integer> setWithTwoJokers = new ArrayList<>(baseSet);
                    setWithTwoJokers.set(jokerPos1, 53); // First Joker
                    setWithTwoJokers.set(jokerPos2, 53); // Second Joker
    
                    if (!checkIfExists(allGroupsWithTwoJokers, setWithTwoJokers)) {
                        allGroupsWithTwoJokers.add(setWithTwoJokers);
                       // System.out.println(setWithTwoJokers);
                    }
                }
            }
        }
        return allGroupsWithTwoJokers;
    }

}
