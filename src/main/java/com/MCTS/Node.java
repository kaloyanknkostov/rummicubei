package com.MCTS;

import java.util.ArrayList;

import java.lang.Math;

public class Node {
    private GameState gameState;
    private Node parent;
    private int visitCount;
    private ArrayList<Node> childList;
    private ArrayList<Float> results; // results of the playouts of all childs
    private double uct;
    private boolean isLeaf; // if its an endstate
    private double c = 0.6; // factor for uct (see lecture 4 slide 20)
    private int currentPlayer;

    public Node(GameState gameState, Node parent, int currentPlayer, boolean isleaf){
        this.gameState = gameState;
        this.parent = parent;
        this.visitCount = 1; // visit count at generation is 1 (otherwise uct will not work)
        this.currentPlayer = currentPlayer;
        this.isLeaf = isleaf;
    }

    public double getUCT(){
        // returns -infinity as default as the highest UCT is selected -> see lecture 4 in RT
        return this.uct;
    }

    public void calculateUCT(){
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
    public Node selectNode(){
        // if this Node has no children then we return this node (to execute play-out)
        this.visitCount += 1;
        if(childList.isEmpty()){
            return this;
        }

        // Search for the highest UCT value in the list of children nodes
        // ARGMAX
        float highestUCT = 0;
        Node nextNode = null;
        for (Node child: childList){
            if(child.getUCT()>highestUCT && !child.getLeaf()){
                nextNode = child;
            }
        }
        return nextNode.selectNode();
    }

    private boolean getLeaf(){
        return this.isLeaf;
    }


    public void expand(){
        ActionSpaceGenerator actionSpace = new ActionSpaceGenerator(CustomUtility.decompose(this.gameState.getBoard()), this.gameState.getRacks()[currentPlayer]);
        for(ArrayList<ArrayList<Integer>> board: actionSpace.getResultingBoards()){
            //for every action move it could make it copies the current gamestate and updates it based on the action
            GameState newState = this.gameState.copy();
            int res = newState.updateGameState(board, currentPlayer);
            if(res == 2){
                //its a draw
                backpropagate(0.5f);
                Node child = new Node(newState, this, (currentPlayer +1) %2, true);
                this.childList.add(child);
            } else if(res == 1){
                //one of the players won, we have to check which one
                backpropagate(newState.getWinner());
                Node child = new Node(newState, this, (currentPlayer +1) %2, true);
                this.childList.add(child);
            } else {
                Node child = new Node(newState, this, (currentPlayer +1) %2, false);
                this.childList.add(child);
            }
            //only works for two players 
        }
    }


    public void playOut(){
        GameState stateForPlayout = this.gameState.copy();
        int playoutMaxer = this.currentPlayer;

        int res = stateForPlayout.updateGameState((new RandomMove(stateForPlayout.getBoard(),stateForPlayout.getRacks()[playoutMaxer])).getRandomMove(),playoutMaxer);
        while(res == 0){
            //update so its the next players turn
            //TODO this is constrained for 2 players
            playoutMaxer = (playoutMaxer + 1) % 2;
            res = stateForPlayout.updateGameState((new RandomMove(stateForPlayout.getBoard(),stateForPlayout.getRacks()[playoutMaxer])).getRandomMove(),playoutMaxer);
            //if this loop terminates it means an endstate was reached, since startingstate is a reference it works in function before
        }
        if(res == 2){
            //its a draw
            backpropagate(0.5f);
        } else {
            //one of the players won, we have to check which one
            backpropagate(stateForPlayout.getWinner());
        }
    }

    public void backpropagate(float winner){
        // propagate the result of the play-out back to the root of the tree
        if(winner == 0.5){
            //propagate the same result to everyone since its a draw
            this.results.add(winner);
        } else {
            // at every node check if the winner is equal to the parent
            if(this.parent.currentPlayer == winner){
                this.results.add(1f);
            } else {
                this.results.add(0f);
            }
        }
        this.calculateUCT(); // Calc new uct and save it to save on computation

        if(this.parent != null){
            this.parent.backpropagate(winner);
        }
    }

    public int getVisitCount(){
        return this.visitCount;
    }
}
