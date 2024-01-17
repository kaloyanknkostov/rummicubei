package com.MCTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomMove {
    private ArrayList<ArrayList<ArrayList<Integer>>> resultingBoards;
    private ArrayList<Integer> startingBoard;
    private ArrayList<Integer> startingRack;
    private ArrayList<ArrayList<Integer>> allPossibleSets;
    private ArrayList<ArrayList<Integer>> possibleSets;
    private ArrayList<ArrayList<Integer>> randomMove;
    private HashMap<ArrayList<Integer>,HashSet<ArrayList<Integer>>> conflicts;
    private Random rand;
    private boolean hasFinished;
    private double notPlaying;
    ArrayList<ArrayList<Integer>> boardForRandomMove;

    public RandomMove(ArrayList<ArrayList<Integer>> board, ArrayList<Integer> rack){
        this.boardForRandomMove = CustomUtility.deepCopy(board);
        this.allPossibleSets = AllSetGenerator.getInstance().getAllSets();
        this.conflicts = ConflictingSets.getInstance().getAllConflicts();
        this.hasFinished = false;
        this.rand = new Random();
        this.resultingBoards = new ArrayList<>();
        this.startingBoard = CustomUtility.decompose(boardForRandomMove);
        this.startingRack = new ArrayList<>(rack);
        this.possibleSets = CustomUtility.possibleSets(this.startingRack,this.startingBoard,this.allPossibleSets);
        // now the possiblesets are shuffled so it tries to add sets in a random order
        Collections.shuffle(this.possibleSets);
        //probably put this somewhere else
        ArrayList<ArrayList<Integer>> beginningBoard = new ArrayList<>();
        createRandomMoves(beginningBoard, this.possibleSets);
        this.notPlaying = 0;
        calculateRandomMove();
        //now the resulting random move can just be accessed from the getMethod
    }

    public ArrayList<ArrayList<Integer>> getRandomMove(){
        return this.randomMove;
    }

    private void calculateRandomMove(){
        if(this.resultingBoards.isEmpty()){
            this.randomMove = new ArrayList<>();
            this.randomMove.add(0,new ArrayList<>(Arrays.asList(-1)));
        } else {
            int x = this.resultingBoards.size();
            //include the prob of not playing anything at all
            int y = this.rand.nextInt(x); //because of zero based indexing
            //System.out.println("Rand number: "+y);
            double didNotPlay = this.rand.nextDouble();
            if(didNotPlay < this.notPlaying){
                this.randomMove = this.boardForRandomMove;
            } else {
                this.randomMove = CustomUtility.deepCopy(this.resultingBoards.get(y));
            }
        }
    }

    private void createRandomMoves(ArrayList<ArrayList<Integer>> currentBoard ,ArrayList<ArrayList<Integer>> setsNoConflicts){
        if(this.hasFinished) {
            return;
        }
        if(setsNoConflicts.isEmpty() && CustomUtility.validBoard(currentBoard, this.startingBoard)){
            this.hasFinished = true;
            return;
        }
        if(setsNoConflicts.isEmpty()){
            return;
        }
        ArrayList<ArrayList<Integer>> conflichtNext = CustomUtility.deepCopy(setsNoConflicts);
        for(ArrayList<Integer> rummikubSet: setsNoConflicts){
            if(hasFinished){
                continue;
            }
            ArrayList<ArrayList<Integer>> newBoard = CustomUtility.deepCopy(currentBoard);
            ArrayList<ArrayList<Integer>> newConflicts = CustomUtility.deepCopy(conflichtNext);
            conflichtNext.remove(rummikubSet);
            newBoard.add(rummikubSet);
            newConflicts.remove(rummikubSet);
            newConflicts.removeIf(this.conflicts.get(rummikubSet)::contains);
            if(!forwardCheck(newBoard, newConflicts)){
                continue;
            }
            if(CustomUtility.validBoard(newBoard, this.startingBoard) && !isEqual(newBoard, this.boardForRandomMove)){
                //System.out.println("FOUND");
                this.resultingBoards.add(newBoard);
            }
            createRandomMoves(newBoard, newConflicts);
        }
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

    public ArrayList<ArrayList<ArrayList<Integer>>> getResultingBoards(){
        return this.resultingBoards;
    }

    public boolean isEqual(ArrayList<ArrayList<Integer>> board1, ArrayList<ArrayList<Integer>> board2){
        ArrayList<Integer> x = CustomUtility.decompose(board1);
        ArrayList<Integer> y = CustomUtility.decompose(board2);
        if(x.size() != y.size()){
            return false;
        }
        Set<Integer> xSet = new HashSet<>(x);
        Set<Integer> ySet = new HashSet<>(y);
        if(xSet.containsAll(ySet)){
            return true;
        }
        return false;
    }

}