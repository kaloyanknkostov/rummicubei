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

public class TestCustomUtility {

    private ArrayList<ArrayList<Integer>> sampleBoard;
    private ArrayList<Integer> startingBoard;
    //private CustomUtility testCustomUtility;
    private ArrayList<ArrayList<Integer>> sampleBoard2;
    private ArrayList<ArrayList<Integer>> sampleBoard3;
    private ArrayList<ArrayList<Integer>> sampleBoard4;

    /*@BeforeEach
    public void setUp() {
         testCustomUtility = new CustomUtility();

        //sample board for testing
        sampleBoard = new ArrayList<>();
        sampleBoard.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
        sampleBoard.add(new ArrayList<>(Arrays.asList(4, 5, 6)));
        sampleBoard.add(new ArrayList<>(Arrays.asList(7, 8, 9)));

        //starting board for testing
        startingBoard = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    }*/

    @Test
    public void testCountIntegers() {
        sampleBoard = new ArrayList<>();
        sampleBoard.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
        sampleBoard.add(new ArrayList<>(Arrays.asList(4, 5, 6)));
        sampleBoard.add(new ArrayList<>(Arrays.asList(7, 8, 9)));
        assertEquals(9, CustomUtility.countIntegers(sampleBoard));

        ArrayList<ArrayList<Integer>> sampleBoard1 =sampleBoard;
        sampleBoard1.add(new ArrayList<>(Arrays.asList(4, 5, 6)));

        assertEquals(12,CustomUtility.countIntegers(sampleBoard1));
    }
    @Test
    public void testdecompose() {
        sampleBoard = new ArrayList<>();
        sampleBoard.add(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 6)));
        sampleBoard.add(new ArrayList<>(Arrays.asList( 5)));
        sampleBoard.add(new ArrayList<>(Arrays.asList(7, 8, 9)));
        startingBoard = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 6, 5, 7, 8, 9));

        assertEquals(startingBoard, CustomUtility.decompose(sampleBoard));
    }
    @Test
    public void testvalidBoard() {
                sampleBoard = new ArrayList<>();
        sampleBoard.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
        sampleBoard.add(new ArrayList<>(Arrays.asList(4, 5, 6)));
        sampleBoard.add(new ArrayList<>(Arrays.asList(7, 8, 9)));
        startingBoard = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        sampleBoard2 = new ArrayList<>();
        sampleBoard2.add(new ArrayList<>(Arrays.asList(1, 2, 3, 4)));
        sampleBoard2.add(new ArrayList<>(Arrays.asList(5, 6, 7, 8, 9)));
        
        sampleBoard4= new ArrayList<>();
        sampleBoard4.add(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)));
        sampleBoard4.add(new ArrayList<>(Arrays.asList( 6, 7, 8, 9)));
        sampleBoard3= new ArrayList<>();
        sampleBoard4.add(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)));
        sampleBoard4.add(new ArrayList<>(Arrays.asList( 5,6, 7, 8, 9)));
        
        assertTrue(CustomUtility.validBoard(sampleBoard,startingBoard));
        assertTrue(CustomUtility.validBoard(sampleBoard2,startingBoard));
        assertTrue(CustomUtility.validBoard(sampleBoard4, startingBoard));
        assertFalse(CustomUtility.validBoard(sampleBoard3,startingBoard));

    }
    @Test
    public void testgetAvailableTiles(){
         sampleBoard = new ArrayList<>();
        sampleBoard.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
        sampleBoard.add(new ArrayList<>(Arrays.asList(4, 5, 6)));
        sampleBoard.add(new ArrayList<>(Arrays.asList(7, 8, 9)));
        ArrayList<Integer> availableTilesStart = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        ArrayList<Integer> availableTiles = CustomUtility.getAvailableTiles(sampleBoard, availableTilesStart);
        assertEquals(Arrays.asList(10), availableTiles);
    }
    @Test 
    public void TestcanCreateSet(){
        ArrayList<Integer> emptyset= new ArrayList<Integer>();
        ArrayList<Integer> set= new ArrayList<Integer>();
        for(int i=1; i<3;i++){
            set.add(i);
        }
        
        ArrayList<Integer> array= new ArrayList<Integer>();
        for(int i=1; i<10;i++){
            array.add(i);
        }

        assertFalse(CustomUtility.canCreateSet(new ArrayList<Integer>(),set));
        assertFalse(CustomUtility.canCreateSet(array,emptyset));
        assertTrue(CustomUtility.canCreateSet(array,set));
        set.add(12);
        assertFalse(CustomUtility.canCreateSet(array,set));
    }
    @Test 
    public void TestgetDifference(){//INCLUDE check for empty when implemented in getdifferecnce function
    sampleBoard = new ArrayList<>();
        sampleBoard.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
        sampleBoard.add(new ArrayList<>(Arrays.asList(4, 5, 6)));
        sampleBoard.add(new ArrayList<>(Arrays.asList(7, 8, 9)));
       
        sampleBoard2 = new ArrayList<>();
        sampleBoard2.add(new ArrayList<>(Arrays.asList(1, 2, 3, 4)));
        sampleBoard2.add(new ArrayList<>(Arrays.asList(5, 6, 7, 8, 9,10,11)));
        ArrayList<Integer> a = new ArrayList<>();
        a.add(10);
        a.add(11);
        assertEquals(a,CustomUtility.getDifference(sampleBoard2, sampleBoard));
    

    }
    @Test 
    public void testpossibleSets(){
        ArrayList<Integer> startingRack = new ArrayList<>();
        startingRack.add(13);
        startingRack.add(26);
        startingRack.add(39);
        startingRack.add(52);
        startingRack.add(1);
        startingRack.add(2);
        startingRack.add(3);
        startingRack.add(17);
        ArrayList<Integer> startingboard = new ArrayList<>();
        startingboard.add(4);
        startingboard.add(5);
        startingboard.add(6);
        ArrayList<ArrayList<Integer>> allPossibleSets = new  ArrayList<>();
        allPossibleSets.add(new ArrayList<>(Arrays.asList(13,26,39,52)));
        allPossibleSets.add(new ArrayList<>(Arrays.asList(26,39,52)));
        allPossibleSets.add(new ArrayList<>(Arrays.asList(13,39,52)));
        allPossibleSets.add(new ArrayList<>(Arrays.asList(13,26,52)));
        allPossibleSets.add(new ArrayList<>(Arrays.asList(13,26,39)));
        allPossibleSets.add(new ArrayList<>(Arrays.asList(1,2,3,4,5,6)));
        allPossibleSets.add(new ArrayList<>(Arrays.asList(2,3,4,5,6)));
        allPossibleSets.add(new ArrayList<>(Arrays.asList(3,4,5,6)));
        allPossibleSets.add(new ArrayList<>(Arrays.asList(4,5,6)));
        allPossibleSets.add(new ArrayList<>(Arrays.asList(1,2,3,4,5)));
        allPossibleSets.add(new ArrayList<>(Arrays.asList(2,3,4,5)));
        allPossibleSets.add(new ArrayList<>(Arrays.asList(3,4,5)));
        allPossibleSets.add(new ArrayList<>(Arrays.asList(1,2,3,4)));
        allPossibleSets.add(new ArrayList<>(Arrays.asList(2,3,4)));
        allPossibleSets.add(new ArrayList<>(Arrays.asList(1,2,3)));
        allPossibleSets.add(new ArrayList<>(Arrays.asList(16,17,18)));

        ArrayList<ArrayList<Integer>> PossibleSets = new  ArrayList<>();
        PossibleSets.add(new ArrayList<>(Arrays.asList(13,26,39,52)));
        PossibleSets.add(new ArrayList<>(Arrays.asList(26,39,52)));
        PossibleSets.add(new ArrayList<>(Arrays.asList(13,39,52)));
        PossibleSets.add(new ArrayList<>(Arrays.asList(13,26,52)));
        PossibleSets.add(new ArrayList<>(Arrays.asList(13,26,39)));
        PossibleSets.add(new ArrayList<>(Arrays.asList(1,2,3,4,5,6)));
        PossibleSets.add(new ArrayList<>(Arrays.asList(2,3,4,5,6)));
        PossibleSets.add(new ArrayList<>(Arrays.asList(3,4,5,6)));
        PossibleSets.add(new ArrayList<>(Arrays.asList(4,5,6)));
        PossibleSets.add(new ArrayList<>(Arrays.asList(1,2,3,4,5)));
        PossibleSets.add(new ArrayList<>(Arrays.asList(2,3,4,5)));
        PossibleSets.add(new ArrayList<>(Arrays.asList(3,4,5)));
        PossibleSets.add(new ArrayList<>(Arrays.asList(1,2,3,4)));
        PossibleSets.add(new ArrayList<>(Arrays.asList(2,3,4)));
        PossibleSets.add(new ArrayList<>(Arrays.asList(1,2,3)));
        assertEquals(PossibleSets, CustomUtility.possibleSets(startingRack, startingboard, allPossibleSets));
    }
        @Test
        public void testdeepCopy(){
            ArrayList<ArrayList<Integer>> a= new ArrayList<>();
            assertEquals(a, CustomUtility.deepCopy(a));

             a= new ArrayList<>();
            for(int i=2; i<51;i++){
            a.add(new ArrayList<>(Arrays.asList(i-1,i,i+1)));
            }  
            assertEquals(a, CustomUtility.deepCopy(a));
            a.add(new ArrayList<>(Arrays.asList()));
            assertEquals(a, CustomUtility.deepCopy(a));


        }
    
}        
