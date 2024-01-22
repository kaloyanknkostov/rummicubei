package com.MCTS;
import  org.junit.Assert.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//import com.MCTS;


public class TestRandomMove {
    /*public static void main(String[] args){
        Test
        testRandomMove();
    }*/
        @Test
        public void testRandomMove(){// Example usage for testing
            ArrayList<ArrayList<Integer>> initialBoard = new ArrayList<>();
            initialBoard.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
            initialBoard.add(new ArrayList<>(Arrays.asList(29, 30, 31)));
            //initialBoard.add(new ArrayList<>(Arrays.asList(4, 5, 6)));
            //initialBoard.add(new ArrayList<>(Arrays.asList(7, 8, 9)));
            ArrayList<Integer> initialRack = new ArrayList<>(Arrays.asList(4,5,6,7,8,9,14,15,16));
            Random rnd = new Random(343);
            RandomMove randomMove = new RandomMove(initialBoard, initialRack,rnd);
            // Display the resulting random move
            System.out.println("Random Move:");
            ArrayList<ArrayList<Integer>> move = randomMove.getRandomMove();
            for (ArrayList<Integer> set : move) {
                System.out.println(set);
            }
            ArrayList<ArrayList<Integer>> expectedMove = new ArrayList<>();
            expectedMove.add(new ArrayList<>(Arrays.asList(1,2,3,4,5)));
            expectedMove.add(new ArrayList<>(Arrays.asList(29,30,31)));
            expectedMove.add(new ArrayList<>(Arrays.asList(7,8,9)));
            assertEquals(expectedMove, move);
            initialBoard.add(new ArrayList<>(Arrays.asList(47,48,49,50,51,52,53)));
             rnd = new Random(343);

            randomMove= new RandomMove(initialBoard, initialRack, rnd);
            move=randomMove.getRandomMove();
            expectedMove= new ArrayList<>();
            expectedMove.add(new ArrayList<>(Arrays.asList(5,6,7,53)));
            expectedMove.add(new ArrayList<>(Arrays.asList(47,48,49)));
            expectedMove.add(new ArrayList<>(Arrays.asList(50,51,52)));
            expectedMove.add(new ArrayList<>(Arrays.asList(29,30,31)));
            expectedMove.add(new ArrayList<>(Arrays.asList(1,2,3,4)));
            expectedMove.add(new ArrayList<>(Arrays.asList(14,15,16)));
            assertEquals(expectedMove, move);









    }
}
