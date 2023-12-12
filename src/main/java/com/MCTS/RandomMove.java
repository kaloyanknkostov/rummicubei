package com.MCTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomMove {
    private ArrayList<ArrayList<ArrayList<Integer>>> resultingBoards;
    private ArrayList<ArrayList<Integer>> startingBoard;
    private ArrayList<Integer> startingRack;
    private ArrayList<ArrayList<Integer>> allPossibleSets;
    private ArrayList<ArrayList<Integer>> possibleSets;
    private ArrayList<Integer> availableTilesStart;
    private ArrayList<ArrayList<Integer>> randomMove;
    private Random rand;
    private boolean hasFinished;

    //TODO randomMove doesn't handel not being able to play yet

    public RandomMove(ArrayList<ArrayList<Integer>> board, ArrayList<Integer> rack){
        System.out.println("IN Action");
        //get all possible sets
        //Here we are getting all the sets
        AllSetGenerator generator = AllSetGenerator.getInstance();
        allPossibleSets = generator.getAllSets();
        hasFinished = false;
        rand = new Random();
        this.resultingBoards = new ArrayList<>();
        this.startingBoard = board;
        this.startingRack = rack;
        this.possibleSets = CustomUtility.possibleSets(this.startingRack,CustomUtility.decompose(board),this.allPossibleSets);
        // now the possiblesets are shuffled so it tries to add sets in a random order
        Collections.shuffle(this.possibleSets);
        //probably put this somewhere else
        availableTilesStart = new ArrayList<>();
        for(Integer tile: startingRack){
            availableTilesStart.add(tile);
        }
        for(Integer tile: CustomUtility.decompose(board)){
            availableTilesStart.add(tile);
        }
        ArrayList<ArrayList<Integer>> beginningBoard = new ArrayList<>();
        createRandomPlayouts(beginningBoard, availableTilesStart, 0);
        calculateRandomMove();
        //now the resulting random move can just be accessed from the getMethod
    }

    public ArrayList<ArrayList<Integer>> getRandomMove(){
        return this.randomMove;
    }

    private void calculateRandomMove(){
        int x = this.resultingBoards.size()-1;
        //include the prob of not playing anything at all
        int y = this.rand.nextInt(x);
        this.randomMove = CustomUtility.deepCopy(this.resultingBoards.get(y));
        if(CustomUtility.decompose(this.resultingBoards.get(y)).equals(CustomUtility.decompose(this.startingBoard)) && this.resultingBoards.size() == 1){
            //meaning the only possible move was not doing anything
            this.randomMove.add(0,new ArrayList<>(Arrays.asList(-1)));
            //add a -1 as the first set of the resulting board
        }

    }

    private void createRandomPlayouts(ArrayList<ArrayList<Integer>> currentBoard, ArrayList<Integer> availableTiles, int lastCheckedSet){
        // if all sets have been checked return
        if(lastCheckedSet == this.possibleSets.size() && CustomUtility.validBoard(currentBoard, CustomUtility.decompose(this.startingBoard))){
            hasFinished = true;
            return;
        } else if(lastCheckedSet == this.possibleSets.size()){
            return;
        }

        for(int i = lastCheckedSet ; i < this.possibleSets.size();i++){
            // If cannot create set with tiles go to next one or if it has finsihed
            if(hasFinished ||!CustomUtility.canCreateSet(availableTiles, possibleSets.get(i))){
                //cause then it wil not run the for loop and reach the return of all the active calls without doing anything
                continue;
            }
            // Create copy of board so changes in this iteration of the loop are unique to the next iteration
            // Pass this copy as a reference in the recursion
            ArrayList<ArrayList<Integer>> currentBoardCopy = CustomUtility.deepCopy(currentBoard);
            currentBoardCopy.add(this.possibleSets.get(i));
            ArrayList<Integer> currentAvailableTiles = new ArrayList<>(availableTiles);
            CustomUtility.customRemove(currentAvailableTiles, this.possibleSets.get(i));

            // Remove tiles in the set in our rack and available tiles list
            //now check if the board is valid
            if(CustomUtility.validBoard(currentBoardCopy, CustomUtility.decompose(this.startingBoard))){
                // Add board and racks to the results as board is valid
                resultingBoards.add(currentBoardCopy);

            }
            createRandomPlayouts(currentBoardCopy, currentAvailableTiles,i);       
        }
    }
}
