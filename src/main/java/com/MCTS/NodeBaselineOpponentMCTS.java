package com.MCTS;

import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Math;
import java.util.Random;

public class NodeBaselineOpponentMCTS {
    private boolean playerMelted;
    private GameState gameState;
    private NodeBaselineOpponentMCTS parent;
    private int visitCount;
    private ArrayList<NodeBaselineOpponentMCTS> childList;
    private ArrayList<Float> results; // results of the playouts of all childs
    private double uct;
    private boolean isLeaf; // if its an endstate
    private double c = 0.6; // factor for uct (see lecture 4 slide 20)
    private int currentPlayer;
    private NodeBaselineOpponentMCTS root;

    public NodeBaselineOpponentMCTS(GameState gameState, NodeBaselineOpponentMCTS parent, int currentPlayer, boolean isleaf, boolean playerMelted, NodeBaselineOpponentMCTS root){
        this.results = new ArrayList<Float>();
        this.childList = new ArrayList<NodeBaselineOpponentMCTS>();
        this.gameState = gameState;
        this.parent = parent;
        this.visitCount = 1; // visit count at generation is 1 (otherwise uct will not work)
        this.currentPlayer = currentPlayer;
        this.isLeaf = isleaf;
        this.root = root;
        this.uct = 0.0;
    }

    public double getUCT(){
        // returns -infinity as default as the highest UCT is selected -> see lecture 4 in RT
        return this.uct;
    }

    public void calculateUCT(){
        if (this.parent == null){
            return;
        }
        // calculates the current UCT and stores it in a variable to save computing time
        if(visitCount== 0){
            this.uct = Float.NEGATIVE_INFINITY;
            return;
        }
        double results_sum = 0;
        for (double result : this.results){
            results_sum += result;
        }
        this.uct = (results_sum / results.size()); // mean of the results at this node
        this.uct += this.c * Math.sqrt(Math.log(this.parent.getVisitCount())/this.getVisitCount());

    }
    /*
     * Returns the next node in the search tree that has to be expanded.
     * This is an recursive method that searches for the highest UCT value in the children nodes.
     */
    public NodeBaselineOpponentMCTS selectNode(){
        // BIAS towards the first child all have the same uct value
        // if this NodeBaselineOpponentMCTS has no children then we return this node (to execute play-out)
        this.visitCount += 1;
        if(this.childList.isEmpty()){
            return this;
        }
        return this.getBestChild(false).selectNode();
    }

    public NodeBaselineOpponentMCTS getBestChild(boolean leaf){
        /**
         * Boolean to either include leaf nodes in the search (if true) or not
         * gets the child with the highest UCT
         * Search for the highest UCT value in the list of children nodes
         **/

        // ARGMAX
        double highestUCT = Double.NEGATIVE_INFINITY;
        NodeBaselineOpponentMCTS nextNode = null;
        //System.err.println("STARTING UCT: "+ highestUCT);
        ///System.err.println("CHILD LIST:" + this.childList);
        for (NodeBaselineOpponentMCTS child: this.childList){
            if(child.getUCT()>highestUCT){
                if(!leaf && !child.getLeaf()){
                    highestUCT = child.getUCT();
                    nextNode = child;
                }
                else if(leaf){
                    highestUCT = child.getUCT();
                    nextNode = child;
                }
            }
        }
        return nextNode;
    }

    public boolean getLeaf(){
        return this.isLeaf;
    }

    public void expand(){
        ArrayList<ArrayList<ArrayList<Integer>>> resultingBoards;
        if(this.root.getCurrentPlayer() == this.currentPlayer){
            if(playerMelted){
                ActionSpaceGenerator actionSpace = new ActionSpaceGenerator(this.gameState.getBoard(), this.gameState.getRacks()[currentPlayer]);
                resultingBoards = actionSpace.getResultingBoards();
            } else {
                // if player has not melted yet, create actions without tiles on the board
                // then filter actions to only include boards with value of at least 30
                ActionSpaceGenerator actionSpace = new ActionSpaceGenerator(new ArrayList<ArrayList<Integer>>(), this.gameState.getRacks()[currentPlayer]);
                resultingBoards = actionSpace.getResultingBoards();
                Iterator<ArrayList<ArrayList<Integer>>> boardIterator = resultingBoards.iterator();
                while(boardIterator.hasNext()){
                    ArrayList<ArrayList<Integer>> current = boardIterator.next();
                    if(CustomUtility.sumOfBoard(current)<30){
                        boardIterator.remove();
                    } else {
                        // Add the original tiles of the board back to all results
                        for(ArrayList<Integer> set: this.gameState.getBoard()){
                            current.add(set);
                        }
                    }
                }
            }
            // Create do nothing board
            resultingBoards.add(this.gameState.getBoard());
            for(ArrayList<ArrayList<Integer>> board: resultingBoards){
                //for every action move it could make it copies the current gamestate and updates it based on the action
                GameState newState = this.gameState.copy();
                int res = newState.updateGameState(board, currentPlayer);
                if(res == 1 || res == 2){
                    //one of the players won, we have to check which one
                    NodeBaselineOpponentMCTS child = new NodeBaselineOpponentMCTS(newState, this, (currentPlayer +1) %2, true, true, root);
                    this.childList.add(child);
                    child.backpropagate(newState.getWinner());
                } else {
                    NodeBaselineOpponentMCTS child = new NodeBaselineOpponentMCTS(newState, this, (currentPlayer +1) %2, false, true, root);
                    this.childList.add(child);
                }
                //only works for two players
            }
        } else {
            //only expand the best move
            if(playerMelted){
                ArrayList<ArrayList<Integer>> bestMove = BaselineAgent.getBestMove(this.gameState.getBoard(), this.gameState.getRacks()[currentPlayer]);
                resultingBoards = new ArrayList<>();
                resultingBoards.add(bestMove);
            } else {
                // if player has not melted yet, create actions without tiles on the board
                // then filter actions to only include boards with value of at least 30
                ActionSpaceGenerator actionSpace = new ActionSpaceGenerator(new ArrayList<ArrayList<Integer>>(), this.gameState.getRacks()[currentPlayer]);
                resultingBoards = actionSpace.getResultingBoards();
                Iterator<ArrayList<ArrayList<Integer>>> boardIterator = resultingBoards.iterator();
                while(boardIterator.hasNext()){
                    ArrayList<ArrayList<Integer>> current = boardIterator.next();
                    if(CustomUtility.sumOfBoard(current)<30){
                        boardIterator.remove();
                    } else {
                        // Add the original tiles of the board back to all results
                        for(ArrayList<Integer> set: this.gameState.getBoard()){
                            current.add(set);
                        }
                    }
                }
            }
            // Create do nothing board
            resultingBoards.add(this.gameState.getBoard());
            for(ArrayList<ArrayList<Integer>> board: resultingBoards){
                //for every action move it could make it copies the current gamestate and updates it based on the action
                GameState newState = this.gameState.copy();
                int res = newState.updateGameState(board, currentPlayer);
                if(res == 1 || res == 2){
                    //one of the players won, we have to check which one
                    NodeBaselineOpponentMCTS child = new NodeBaselineOpponentMCTS(newState, this, (currentPlayer +1) %2, true, true, root);
                    this.childList.add(child);
                    child.backpropagate(newState.getWinner());
                } else {
                    NodeBaselineOpponentMCTS child = new NodeBaselineOpponentMCTS(newState, this, (currentPlayer +1) %2, false, true, root);
                    this.childList.add(child);
                }
                //only works for two players
            }
        }

    }


    public void playOut(){
        GameState stateForPlayout = this.gameState.copy();
        int playoutMaxer = this.currentPlayer;
        RandomMove newMove = new RandomMove(stateForPlayout.getBoard(),stateForPlayout.getRacks()[playoutMaxer]);
        ArrayList<ArrayList<Integer>> newBoard = newMove.getRandomMove();
        int res = stateForPlayout.updateGameState(newBoard,playoutMaxer);
        while(res == 0){
            //update so its the next players turn
            //TODO this is constrained for 2 players
            playoutMaxer = (playoutMaxer + 1) % 2;
            res = stateForPlayout.updateGameState((new RandomMove(stateForPlayout.getBoard(),stateForPlayout.getRacks()[playoutMaxer])).getRandomMove(),playoutMaxer);
            //if this loop terminates it means an endstate was reached, since startingstate is a reference it works in function before
        }
        //one of the players won, we have to check which one
        System.out.println(stateForPlayout.getWinner());
        backpropagate(stateForPlayout.getWinner());
        
    }

    public void backpropagate(float winner){
        if (this.parent == null){
            return;
        }
        // at every node check if the winner is equal to the parent
        if(this.parent.currentPlayer == winner){
            this.results.add(1f);
        } else {
            this.results.add(0f);
        }
        this.calculateUCT(); // Calc new uct and save it to save on computation

        if(this.parent != null){
            this.parent.backpropagate(winner);
        }
    }

    public int getVisitCount(){
        return this.visitCount;
    }

    public GameState getGameState(){
        return this.gameState;
    }

    public ArrayList<NodeBaselineOpponentMCTS> getChildList(){
        return this.childList;
    }
    public int getCurrentPlayer(){
        return this.currentPlayer;
    }
}
