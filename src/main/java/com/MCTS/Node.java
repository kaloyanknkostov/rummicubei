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

    public Node(GameState gameState, Node parent){
        this.gameState = gameState;
        this.parent = parent;
        this.visitCount = 1; // visit count at generation is 1 (otherwise uct will not work)

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

    public void playOut(){
        // Starts play-out at this node
        // when play-out reaches an end node (win, loss or draw) it backpropagates and adds the first node that was played to the childList
        // also add the first node from the playout to the tree

        //first we get the first random move from this players perspective and add it to the childlist
        //TODO add this for other players as well so gamestate is from our perspective again
        RandomMove randomMoveMe = new RandomMove(this.gameState.getBoard(), this.gameState.getRacks()[0]);
        ArrayList<ArrayList<Integer>> firstMoveMe = randomMoveMe.getRandomMove();
        //now update the gamestates and check if it resulted in something
        //copy the gamestate so as not to update the gamestate of this node
        GameState firstChildState = this.gameState.copy();
        int res = firstChildState.updateGameState(firstMoveMe, 0);
        if(res == 1){
            //we won the game
            backpropagate(1);
            return;
        } else if (res == 2){
            backpropagate(0.5f);
            return;
        }
        //now other player plays
        //TODO this only works for 2 players, have to fix this next phase
        RandomMove randomMoveOpponent = new RandomMove(firstChildState.getBoard(), firstChildState.getRacks()[1]);
        ArrayList<ArrayList<Integer>> firstMoveOpponent = randomMoveOpponent.getRandomMove();
        res = firstChildState.updateGameState(firstMoveOpponent, 1);
        //check draw or loss
        if(res == 1){
            //we lost
            backpropagate(0);
            return;
        } else if (res == 2){
            backpropagate(0.5f);
            return;
        }
        // now its our turn again

        //if nothing of this sort happened we proceed to random playout
        //TODO, rn I use selectnode since it doesnt have any children yet
        Node firstChild = new Node(firstChildState, this);
        this.childList.add(firstChild);
        GameState stateForPlayout = firstChildState.copy();
        int x = playoutHelper(stateForPlayout, 1);
        //now from the stateforplayout we can get what the result was and who won.
        //backprop
        if(x == 1){
            if(stateForPlayout.getWinner() == 0){
                backpropagate(1);
            } else {
                backpropagate(0);
            }
        } else {
            backpropagate(0.5f);
        }
        

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
