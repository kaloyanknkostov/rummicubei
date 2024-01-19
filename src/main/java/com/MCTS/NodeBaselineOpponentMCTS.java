package com.MCTS;

import java.util.ArrayList;

import java.lang.Math;
import java.util.Random;

public class NodeBaselineOpponentMCTS {
    /*
     * this class is the same as normal MCTS but here every node is from out perspective so we assume our opponent plays baseline
     */
    private GameState gameState;
    private NodeBaselineOpponentMCTS parent;
    private int visitCount;
    private ArrayList<NodeBaselineOpponentMCTS> childList;
    private ArrayList<Float> results; // results of the playouts of all childs
    private double uct;
    private boolean isLeaf; // if its an endstate
    private double c = 0.6; // factor for uct (see lecture 4 slide 20)
    //currentplayer is always zero since we are never from out opponents perspective

    public NodeBaselineOpponentMCTS (GameState gameState, NodeBaselineOpponentMCTS parent, boolean isleaf){
        this.results = new ArrayList<Float>();
        this.childList = new ArrayList<NodeBaselineOpponentMCTS>();
        this.gameState = gameState;
        this.parent = parent;
        this.visitCount = 1; // visit count at generation is 1 (otherwise uct will not work)
        this.isLeaf = isleaf;
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
        // if this Node has no children then we return this node (to execute play-out)
        this.visitCount += 1;
        if(this.childList.isEmpty()){
            return this;
        }
        // Search for the highest UCT value in the list of children nodes
        // ARGMAX
        double highestUCT = Double.NEGATIVE_INFINITY;
        NodeBaselineOpponentMCTS nextNode = null;
        //System.err.println("STARTING UCT: "+ highestUCT);
        ///System.err.println("CHILD LIST:" + this.childList);
        for (NodeBaselineOpponentMCTS child: this.childList){
            if(child.getUCT()>highestUCT && !child.getLeaf()){
                highestUCT = child.getUCT();
                nextNode = child;
            }
        }
        return nextNode.selectNode();
    }

    private boolean getLeaf(){
        return this.isLeaf;
    }

    public void expand(){
        ActionSpaceGenerator actionSpace = new ActionSpaceGenerator(this.gameState.getBoard(), this.gameState.getRacks()[0]);
        for(ArrayList<ArrayList<Integer>> board: actionSpace.getResultingBoards()){
            //for every action move it could make it copies the current gamestate and updates it based on the action
            GameState newState = this.gameState.copy();
            int res = newState.updateGameState(board, 0);
            if(res == 2){
                //its a draw
                NodeBaselineOpponentMCTS child = new NodeBaselineOpponentMCTS(newState, this, true);
                this.childList.add(child);
                child.backpropagate(0.5f);
            } else if(res == 1){
                NodeBaselineOpponentMCTS child = new NodeBaselineOpponentMCTS(newState, this, true);
                this.childList.add(child);
                child.backpropagate(1f);
            } else {
                //in this case our opponent gets to play now
                ArrayList<ArrayList<Integer>> opponentBoard = BaselineAgent.getBestMove(board, newState.getRacks()[1]); // this is the baseline best move our opponent can make
                int opponentRes = newState.updateGameState(opponentBoard,1);
                //now we do have to check for our opponent again what his move resulted in
                if(opponentRes == 2){
                    NodeBaselineOpponentMCTS child = new NodeBaselineOpponentMCTS(newState, this, true);
                    this.childList.add(child);
                    child.backpropagate(0.5f);
                } else if(opponentRes == 1) {
                    NodeBaselineOpponentMCTS child = new NodeBaselineOpponentMCTS(newState, this, true);
                    this.childList.add(child);
                    child.backpropagate(0f);
                } 
                NodeBaselineOpponentMCTS child = new NodeBaselineOpponentMCTS(newState, this, false);
                this.childList.add(child);
            }
            //only works for two players
        }

    }


    public void playOut(){
        GameState stateForPlayout = this.gameState.copy();
        RandomMove newMove = new RandomMove(stateForPlayout.getBoard(),stateForPlayout.getRacks()[0]);
        ArrayList<ArrayList<Integer>> newBoard = newMove.getRandomMove();
        int player = 0;
        int res = stateForPlayout.updateGameState(newBoard,0);
        while(res == 0){
            //update so its the next players turn
            //TODO this is constrained for 2 players
            player = (player + 1) % 2;
            res = stateForPlayout.updateGameState((new RandomMove(stateForPlayout.getBoard(),stateForPlayout.getRacks()[player])).getRandomMove(),player);
            //if this loop terminates it means an endstate was reached, since startingstate is a reference it works in function before
        }
        if(res == 2){
            //its a draw
            backpropagate(0.5f);
        } else if (stateForPlayout.getWinner() == 0){
            //one of the players won, we have to check which one
            backpropagate(1f);
        } else { 
            backpropagate(0f);
        }
    }

    public void backpropagate(Float result){
        if (this.parent == null){
            return;
        }
        this.results.add(result);
        this.calculateUCT(); // Calc new uct and save it to save on computation

        if(this.parent != null){
            this.parent.backpropagate(result);
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
}
