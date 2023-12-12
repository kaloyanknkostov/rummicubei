package com.test;

import org.junit.Before;
import org.junit.Test;

import com.gameEngine.BoardUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class BoardUtilsTest {

    private BoardUtils boardUtils;

    @Before
    public void setUp() {
        ArrayList<ArrayList<Integer>> sampleBoard = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(1, 2, 3)),
                new ArrayList<>(Arrays.asList(4, 5, 6)),
                new ArrayList<>(Arrays.asList(7, 8, 9))
        ));
        boardUtils = new BoardUtils(sampleBoard);
    }

    @Test
    public void testGetDifference() {
        ArrayList<ArrayList<Integer>> newBoard = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(1, 2, 3)),
                new ArrayList<>(Arrays.asList(4, 5, 6, 10)), 
                new ArrayList<>(Arrays.asList(7, 8, 9))
        ));

        ArrayList<Integer> difference = boardUtils.getDifference(newBoard);
        assertEquals(1, difference.get(0).intValue());
        assertEquals(9, difference.get(8).intValue());
        assertFalse(difference.contains(10));
    }

    public void testGetDifference2() {
        ArrayList<ArrayList<Integer>> newBoard = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(1, 2, 0)),
                new ArrayList<>(Arrays.asList(10, 12, 6)), 
                new ArrayList<>(Arrays.asList(7, 56, 77))
        ));

        ArrayList<Integer> difference = boardUtils.getDifference(newBoard);
        assertTrue(difference.contains(1));
        assertTrue(difference.contains(2));
        assertTrue(difference.contains(6));
        assertTrue(difference.contains(7));
    }

    @Test
    public void testGetDifferenceSameArray() {
        ArrayList<ArrayList<Integer>> newBoard = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(1, 2, 3)),
                new ArrayList<>(Arrays.asList(4, 5, 6)), 
                new ArrayList<>(Arrays.asList(7, 8, 9))
        ));

        ArrayList<Integer> difference = boardUtils.getDifference(newBoard);
        assertEquals(1, difference.get(0).intValue());
        assertEquals(9, difference.get(8).intValue());
    }

    public void testGetDifferenceEmptyArray() {
        ArrayList<ArrayList<Integer>> newBoard = new ArrayList<>();

        ArrayList<Integer> difference = boardUtils.getDifference(newBoard);
        assertTrue(difference.isEmpty());
    }

    public void testGetDifferenceCompletelyDifferent() {
        ArrayList<ArrayList<Integer>> newBoard = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(10, 20, 30)),
                new ArrayList<>(Arrays.asList(40, 50, 60)), 
                new ArrayList<>(Arrays.asList(70, 80, 90))
        ));

        ArrayList<Integer> difference = boardUtils.getDifference(newBoard);
        assertTrue(difference.isEmpty());
    }

    @Test
    public void testCustomRemove() {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        ArrayList<Integer> elementsToRemove = new ArrayList<>(Arrays.asList(2, 4));

        BoardUtils.customRemove(list, elementsToRemove);

        assertEquals(2, list.size());
        assertFalse(list.contains(2));
        assertFalse(list.contains(4));
    }

    @Test
    public void testCustomRemoveWholeList() {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        ArrayList<Integer> elementsToRemove = new ArrayList<>(Arrays.asList(1, 2, 3, 4));

        BoardUtils.customRemove(list, elementsToRemove);

        assertTrue(list.isEmpty());
    }

    @Test
    public void testCustomRemoveEmptyInput() {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        ArrayList<Integer> elementsToRemove = new ArrayList<>();

        BoardUtils.customRemove(list, elementsToRemove);

        assertEquals(4, list.size());
        assertTrue(list.contains(4));
    }

    @Test
    public void testDeepCopy() {
        ArrayList<ArrayList<Integer>> original = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(1, 2, 3)),
                new ArrayList<>(Arrays.asList(4, 5, 6)),
                new ArrayList<>(Arrays.asList(7, 8, 9))
        ));

        ArrayList<ArrayList<Integer>> copy = boardUtils.deepCopy(original);

        original.get(0).set(0, 99);

        assertNotEquals(original.get(0).get(0), copy.get(0).get(0));
    }

    @Test
    public void testDecompose() {
        ArrayList<Integer> decomposed = boardUtils.decompose(sampleBoard);

        assertEquals(1, decomposed.get(0));
        assertEquals(9, decomposed.get(8));
    }

    @Test 
    public void testDecomposeEmptyInput() {
        ArrayList<ArrayList<Integer>> empty = new ArrayList<>();
        ArrayList<Integer> emptyDecomposed = decompose(empty);

        assertTrue(emptyDecomposed.isEmpty());
    }

    public void testCanCreateSet() {
        ArrayList<Integer> array1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        ArrayList<Integer> set1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        assertTrue(BoardUtils.canCreateSet(array1, set1));

        ArrayList<Integer> emptyArray = new ArrayList<>();
        ArrayList<Integer> nonEmptySet = new ArrayList<>(Arrays.asList(1, 2, 3));
        assertFalse(BoardUtils.canCreateSet(emptyArray, nonEmptySet));

        ArrayList<Integer> nonEmptyArray = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        ArrayList<Integer> emptySet = new ArrayList<>();
        assertTrue(BoardUtils.canCreateSet(nonEmptyArray, emptySet));

        assertTrue(BoardUtils.canCreateSet(emptyArray, emptySet));

        ArrayList<Integer> array2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        ArrayList<Integer> set2 = new ArrayList<>(Arrays.asList(6, 7, 8));
        assertFalse(BoardUtils.canCreateSet(array2, set2));
    }

    @Test
    public void testValidBoard() {
        //same board
        ArrayList<ArrayList<Integer>> newBoard1 = new ArrayList<>(boardUtils.getBoard());
        assertTrue(boardUtils.validBoard(newBoard1));

        //missing tiles
        ArrayList<ArrayList<Integer>> newBoard2 = new ArrayList<>();
        newBoard2.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
        newBoard2.add(new ArrayList<>(Arrays.asList(4, 5, 6)));
        assertFalse(boardUtils.validBoard(newBoard2));

        //extra tiles
        ArrayList<ArrayList<Integer>> newBoard3 = new ArrayList<>(boardUtils.getBoard());
        newBoard3.get(0).add(10);  
        assertTrue(boardUtils.validBoard(newBoard3));

        //different tile
        ArrayList<ArrayList<Integer>> newBoard4 = new ArrayList<>(boardUtils.getBoard());
        newBoard4.get(0).set(0, 99);  // Changing a tile
        assertFalse(boardUtils.validBoard(newBoard4));

        //duplicates
        ArrayList<ArrayList<Integer>> startingBoardWithDuplicates = new ArrayList<>();
        startingBoardWithDuplicates.add(new ArrayList<>(Arrays.asList(1, 2, 2)));
        startingBoardWithDuplicates.add(new ArrayList<>(Arrays.asList(3, 4, 4, 5)));
        assertTrue(boardUtils.validBoard(startingBoardWithDuplicates));
    }

    @Test
    public void testPossibleSets() {
        ArrayList<Integer> startingRack = new ArrayList<>(Arrays.asList(1, 2, 3));
        ArrayList<Integer> startingBoard = new ArrayList<>(Arrays.asList(4, 5, 6));

        ArrayList<ArrayList<Integer>> expectedSets = new ArrayList<>();
        expectedSets.add(new ArrayList<>(Arrays.asList(1, 2, 3, 4)));
        expectedSets.add(new ArrayList<>(Arrays.asList(2, 3, 4, 5)));
        expectedSets.add(new ArrayList<>(Arrays.asList(3, 4, 5, 6)));
        expectedSets.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
        expectedSets.add(new ArrayList<>(Arrays.asList(2, 3, 4)));
        expectedSets.add(new ArrayList<>(Arrays.asList(3, 4, 5)));
        expectedSets.add(new ArrayList<>(Arrays.asList(4, 5, 6)));

        ArrayList<ArrayList<Integer>> result = boardUtils.possibleSets(startingRack, startingBoard);
        assertEquals(expectedSets, result);
    }

    @Test
    public void testContainsTile() {
        assertTrue(boardUtils.containsTile(decompose(boardUtils.getBoard())), 1);
        assertFalse(boardUtils.containsTile(decompose(boardUtils.getBoard())), 10);
    }

    @Test
    public void testCountIntegers() {
        ArrayList<ArrayList<Integer>> listOfLists = new ArrayList<>();
        listOfLists.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
        listOfLists.add(new ArrayList<>(Arrays.asList(4, 5, 6)));
        listOfLists.add(new ArrayList<>(Arrays.asList(7, 8, 9)));

        int result = boardUtils.countIntegers(listOfLists);

        assertEquals(9, result);
    }

    @Test
    public void canCreateSetCustom() {
        ArrayList<Integer> first = new ArrayList<>(Arrays.asList(53, 40, 46, 4, 44, 15, 51, 33, 19, 47, 13, 42, 50, 47, 28));
        ArrayList<Integer> second = new ArrayList<>(Arrays.asList(40, 53,42,53,44));

        assertFalse(BoardUtils.canCreateSet(first, second));
    }
}
