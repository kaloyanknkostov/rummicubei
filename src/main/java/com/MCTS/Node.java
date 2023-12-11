package com.MCTS;

import java.util.ArrayList;

import java.lang.Math;

public class Node {
    private GameState gameState;
    private Node parent;
    private int visitCount;
    private boolean endNode;
    private ArrayList<Node> childList;
    private ArrayList<Float> results; // results of the playouts of all childs
    private double uct;
    private double c = 0.6; // factor for uct (see lecture 4 slide 20)
    // i think node should iinclude maximizing player
    private int maximizingPlayer;

    public Node(GameState gameState, Node parent, int maximizingPlayer){
        this.gameState = gameState;
        this.parent = parent;
        this.visitCount = 1; // visit count at generation is 1 (otherwise uct will not work)
        this.maximizingPlayer = maximizingPlayer;

        // Check if current game state is a win/ loss/ draw
        if (false){ // TODO
            this.endNode = true;
        }
        else {
            this.endNode = false;
        }

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
        for (double result : results){
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
            if(child.getUCT()>highestUCT){
                nextNode = child;
            }
        }
        return nextNode.selectNode();
    }

    public void expand(){
        ActionSpaceGenerator actionSpace = new ActionSpaceGenerator(this.gameState.getBoard(), this.gameState.getRacks()[maximizingPlayer]);
        for(ArrayList<ArrayList<Integer>> board: actionSpace.getResultingBoards()){
            //for every action move it could make it copies the current gamestate and updates it based on the action
            GameState newState = this.gameState.copy();
            newState.updateGameState(board, maximizingPlayer);
            //only works for two players 
            this.childList.add(new Node(newState, this, (maximizingPlayer +1) %2));
        }
    }


    public void playOut(){

    }

    private int playoutHelper(GameState startingState, int startingPlayer){
        //this function actually runs untill we reach a gamestate
        int res = startingState.updateGameState((new RandomMove(startingState.getBoard(), startingState.getRacks()[startingPlayer])).getRandomMove(),startingPlayer);
        int player = startingPlayer;
        while(res == 0){
            //update so its the next players turn
            //TODO this is constrained for 2 players
            player = (player + 1) % 2;
            res = startingState.updateGameState((new RandomMove(startingState.getBoard(), startingState.getRacks()[player])).getRandomMove(),player);
            //if this loop terminates it means an endstate was reached, since startingstate is a reference it works in function before
        }
        return res;
    }

    public void backpropagate(float new_result){
        // propagate the result of the play-out back to the root of the tree
        results.add(new_result);
        this.calculateUCT(); // Calc new uct and save it to save on computation

        if(this.parent != null){
            this.parent.backpropagate(new_result);
        }
    }

    public int getVisitCount(){
        return this.visitCount;
    }
}
