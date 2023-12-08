package com.MCTS;

import java.util.ArrayList;

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
        
        //this.childList.add(Node Node(TODO))
        return null;
    }

    public void backpropagate(){
        // propagate the result of the play-out back to the root of the tree

        // TODO do some stuff and adjust UCT

        if(this.parent != null){
            this.parent.backpropagate();
        }
    }
}
