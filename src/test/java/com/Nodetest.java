package com;

import org.junit.jupiter.api.Test;

import com.MCTS.GameState;
import com.MCTS.Node;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
public class Nodetest {

    Node setup(int loops){
        ArrayList<Integer> player1 = new ArrayList<>(Arrays.asList(2, 3, 4));
        ArrayList<Integer> player2 = new ArrayList<>(Arrays.asList(17, 18, 19,20));
        ArrayList<ArrayList<Integer>> tempboard = new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(17, 18, 19, 20))));      
        ArrayList<Integer> pile = new ArrayList<>();
        for(int k=0; k<54;k++){
            pile.add(k);
        }
        for(int i=0; i<54;i++){
            pile.add(i);
        }
        for(int j=0; j<54;j++){
            for (int k=0; k<tempboard.size();k++){
                if(tempboard.get(k).contains(j)){
                    pile.remove(j);
                }
            }
        }
        GameState gamestate= new GameState(player1, player2, tempboard, pile);
        Node root = new Node(gamestate,null);
        for (int i = 0; i < loops; i++){
           root.selectNode().playOut();
        }
        return root;
        
    }

    @Test
    void testGetUCT() {
        // Create a test scenario and assert the expected result
        // Example:
        Node node = new Node(/* provide necessary parameters */);
        double uct = node.getUCT();
        assertEquals(/* expected value */, uct, 0.001); // Adjust the expected value and delta
    }

    @Test
    void testbackpropagate() {
        // Create a test scenario and assert the expected result
        // Example:
        Node node = new Node(/* provide necessary parameters */);
        node.calculateUCT();
        assertEquals(/* expected value */, node.getUCT()); // Adjust the expected value and delta
    }

    // Add more test methods for other functionalities in the Node class
}

