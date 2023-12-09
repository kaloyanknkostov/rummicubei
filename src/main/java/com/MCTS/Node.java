package com.MCTS;

import java.util.ArrayList;

import javax.xml.stream.events.StartDocument;

public class Node {
    private GameState gameState;
    private Node parent;
    private int visitCount;
    private boolean endNode;
    private ArrayList<Node> childList;

    public Node(GameState gameState, Node parent){
        this.gameState = gameState;
        this.parent = parent;
        this.visitCount = 0;

        // Check if current game state is a win/ loss/ draw
        if (false){ // TODO
            this.endNode = true;
        }
        else {
            this.endNode = false;
        }

    }

    public float getUCT(){
        // returns -infinity as default as the highest UCT is selected -> see lecture 4 in RT
        if(visitCount== 0){
            return Float.NEGATIVE_INFINITY;
        }
        return Float.NaN;
    }

    /*
     * Returns the next node in the search tree that has to be expanded.
     * This is an recursive method that searches for the highest UCT value in the children nodes.
     */
    public Node selectNode(){
        // if this Node has no children then we return this node (to execute play-out)
        if(childList.isEmpty()){
            return this;
        }

        // Search for the highest UCT value in the list of children nodes
        float highestUCT = 0;
        Node nextNode = null;
        for (Node child: childList){
            if(child.getUCT()>highestUCT){
                nextNode = child;
            }
        }
        return nextNode.selectNode();
    }

    public Node playOut(){
        // Starts play-out at this node
        // when play-out reaches an end node (win, loss or draw) it backpropagates and adds the first node that was played to the childList
        // also add the first node from the playout to the tree

        //first we get the first random move from this players persepctive and add it to the childlist
        RandomMove randomMove = new RandomMove(this.gameState.getBoard(), this.gameState.getRacks()[0]);
        ArrayList<ArrayList<Integer>> firstMove = randomMove.getRandomMove();
        //now update the gamestates and check if it resulted in something
        //copy the gamestate so as not to update the gamestate of this node
        GameState firstChildState = this.gameState.copy();
        int res = firstChildState.updateGameState(firstMove, 0);
        if(res == 1){
            //we won the game
        } else if (res == 2){
            //the game ended in a draw
        } 
        //if nothing of this sort happened we proceed to random playout
        //itself idk how to do this ask freddy TODO, rn I use selectnode since it doesnt have any children yet
        Node firstChild = new Node(firstChildState, selectNode());
        this.childList.add(firstChild);
        GameState stateForPlayout = firstChildState.copy();
        playoutHelper(stateForPlayout, 1);
        //now from the stateforplayout we can get what the result was and who won.
        return null;
    }

    private void playoutHelper(GameState startingState, int startingPlayer){
        //this function actually runs untill we reach a gamestate 
        int res = startingState.updateGameState((new RandomMove(startingState.getBoard(), startingState.getRacks()[startingPlayer])).getRandomMove(),startingPlayer);
        int player = startingPlayer;
        while(res == 0){
            //update so its the next players turn 
            player = (player + 1) % 2;
            res = startingState.updateGameState((new RandomMove(startingState.getBoard(), startingState.getRacks()[player])).getRandomMove(),player);
            //if this loop terminates it means an endstate was reached, since startingstate is a reference it works in function before
        }
    }

    public void backpropagate(){
        // propagate the result of the play-out back to the root of the tree

        // TODO do some stuff and adjust UCT

        if(this.parent != null){
            this.parent.backpropagate();
        }
    }
}
