package com.gameEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardUtilsTest {

    private BoardUtils boardUtils;

    public BoardUtilsTest(BoardUtils boardUtils) {
        this.boardUtils = boardUtils;
        ArrayList<Integer> firstRow = new ArrayList<>();
        ArrayList<Integer> secondRow = new ArrayList<>();
        ArrayList<Integer> thirdRow = new ArrayList<>();
        firstRow.add(1);
        firstRow.add(2);
        firstRow.add(3);
        thirdRow.add(3);
        thirdRow.add(4);
        thirdRow.add(5);
        boardUtils.getBoard().add(firstRow);
        boardUtils.getBoard().add(secondRow);
        boardUtils.getBoard().add(thirdRow);     
    }

    public ArrayList<ArrayList<Integer>> setUpTestBoard() {
        ArrayList<Integer> firstTestRow = new ArrayList<>();
        ArrayList<Integer> secondTestRow = new ArrayList<>();
        ArrayList<Integer> thirdTestRow = new ArrayList<>();
        firstTestRow.add(1);
        firstTestRow.add(2);
        firstTestRow.add(3);
        secondTestRow.add(10);
        secondTestRow.add(11);
        secondTestRow.add(12);
        thirdTestRow.add(3);
        thirdTestRow.add(4);
        thirdTestRow.add(5);

        ArrayList<ArrayList<Integer>> testBoard = new ArrayList<>();
        testBoard.add(firstTestRow);
        testBoard.add(secondTestRow);
        testBoard.add(thirdTestRow);
        return testBoard;
    }

    public void testDecompose() {
        ArrayList<Integer> result = boardUtils.decompose(boardUtils.getBoard());
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        expected.add(3);
        expected.add(3);
        expected.add(4);
        expected.add(5);

        if(result.equals(expected)) {
            System.out.println("Test passed for decompose");
        } else {
            System.out.println("Test Failed for decompose");
        }
    }

    public void testDecomposeEmptyInput() {
        ArrayList<ArrayList<Integer>> empty = new ArrayList<>();
        ArrayList<Integer> result = boardUtils.decompose(empty);
        ArrayList<Integer> expected = new ArrayList<>();

        if(result.equals(expected)) {
            System.out.println("Test passed for decompose with an empty input");
        } else {
            System.out.println("Test Failed for decompose with an empty input");
        }
    }

    public void testDeepCopy() {
        ArrayList<ArrayList<Integer>> result = boardUtils.deepCopy(boardUtils.getBoard());
        ArrayList<ArrayList<Integer>> expected = new ArrayList<>();
        ArrayList<Integer> firstRow = new ArrayList<>();
        ArrayList<Integer> secondRow = new ArrayList<>();
        ArrayList<Integer> thirdRow = new ArrayList<>();
        firstRow.add(1);
        firstRow.add(2);
        firstRow.add(3);
        thirdRow.add(3);
        thirdRow.add(4);
        thirdRow.add(5);
        expected.add(firstRow);
        expected.add(secondRow);
        expected.add(thirdRow);

        if(result.equals(expected)) {
            System.out.println("Test passed for deepCopy");
        } else {
            System.out.println("Test Failed for deepCopy");
        }
    }

    public void testDeepCopyEmptyInput () {
        ArrayList<ArrayList<Integer>> empty = new ArrayList<>();
        ArrayList<ArrayList<Integer>> result = boardUtils.deepCopy(empty);
        ArrayList<ArrayList<Integer>> expected = new ArrayList<>();

        if(result.equals(expected)) {
            System.out.println("Test passed for deepCopy with an empty input");
        } else {
            System.out.println("Test Failed for deepCopy with an empty input");
        }
    }

    public void testGetDifference() {
        ArrayList<ArrayList<Integer>> testBoard = setUpTestBoard();
        ArrayList<Integer> result = boardUtils.getDifference(testBoard);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        expected.add(3);
        expected.add(3);
        expected.add(4);
        expected.add(5);

        if(result.equals(expected)) {
            System.out.println("Test passed for getDifference");
        } else {
            System.out.println("Test Failed for getDifference");
        }
    }

    public void testGetDifferenceEmptyInput() {
        ArrayList<ArrayList<Integer>> empty = new ArrayList<>();
        ArrayList<Integer> result = boardUtils.getDifference(empty);
        ArrayList<ArrayList<Integer>> expected = new ArrayList<>();

        if(result.equals(expected)) {
            System.out.println("Test passed for getDifference with an empty input");
        } else {
            System.out.println("Test Failed for getDifference with an empty input");
        }
    }

    public void testGetDifferenceSameInput() {
        ArrayList<ArrayList<Integer>> copy = boardUtils.deepCopy(boardUtils.getBoard());
        ArrayList<Integer> result = boardUtils.getDifference(copy);
        ArrayList<ArrayList<Integer>> expected = boardUtils.deepCopy(boardUtils.getBoard());
        ArrayList<Integer> expectedDecomposed = boardUtils.decompose(expected);

        if(result.equals(expectedDecomposed)) {
            System.out.println("Test passed for getDifference with the same input");
        } else {
            System.out.println("Test Failed for getDifference with the same input");
        }
    }

    public void testCustomRemove() {
        ArrayList<Integer> list = boardUtils.decompose(setUpTestBoard());
        ArrayList<Integer> elementsToRemove = boardUtils.decompose(boardUtils.getBoard());
        BoardUtils.customRemove(list, elementsToRemove);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(10);
        expected.add(11);
        expected.add(12);

        if(list.equals(expected)) {
            System.out.println("Test passed for customRemove");
        } else {
            System.out.println("Test Failed for customRemove");
        }
    }

    public void testCustomRemoveEmptyInput() {
        ArrayList<Integer> list = boardUtils.decompose(setUpTestBoard());
        ArrayList<Integer> elementsToRemove = new ArrayList<>();
        BoardUtils.customRemove(list, elementsToRemove);
        ArrayList<Integer> expected = boardUtils.decompose(setUpTestBoard());

        if(list.equals(expected)) {
            System.out.println("Test passed for customRemove with an empty input");
        } else {
            System.out.println("Test Failed for customRemove with an empty input");
        }
    }

    public void testCanCreateSet() {
        ArrayList<Integer> array = new ArrayList<>();
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(4);
        array.add(5);
        ArrayList<Integer> array2 = new ArrayList<>();
        ArrayList<Integer> set1 = new ArrayList<>();
        set1.add(1);
        set1.add(2);
        set1.add(3);
        ArrayList<Integer> set2 = new ArrayList<>();
        set2.add(4);
        set2.add(5);
        set2.add(6);
        ArrayList<Integer> set3 = new ArrayList<>();
        set3.add(3);
        set3.add(4);
        set3.add(5);
        boolean firstTest = BoardUtils.canCreateSet(array, set1);
        boolean secondTest = BoardUtils.canCreateSet(array, set2);
        boolean thirdTest = BoardUtils.canCreateSet(array, set3);
        boolean forthtest = BoardUtils.canCreateSet(array2, set1);

        if(firstTest && !secondTest && thirdTest && !forthtest) {
            System.out.println("Test passed for canCreateSet");
        } else {
            System.out.println("Test Failed for canCreateSet");
        }
    }

    public void testCountIntegers() {
        ArrayList<ArrayList<Integer>> firstTest = setUpTestBoard();
        ArrayList<ArrayList<Integer>> secondTest = boardUtils.getBoard();
        ArrayList<ArrayList<Integer>> thirdTest = new ArrayList<>();
        int firstCount = boardUtils.countIntegers(firstTest);
        int secondCount = boardUtils.countIntegers(secondTest);
        int thirdCount = boardUtils.countIntegers(thirdTest);

        if(firstCount==9 && secondCount==6 && thirdCount==0) {
            System.out.println("Test passed for countIntegers");
        } else {
            System.out.println("Test Failed for countIntegers");
        }
    }

    public void testContainsTile() {
        ArrayList<Integer> board = boardUtils.decompose(setUpTestBoard());
        int i = 1;
        int j = 8;

        boolean firstTest = boardUtils.containsTile(board, i);
        boolean secondTest = boardUtils.containsTile(board, j);

        if(firstTest && !secondTest) {
            System.out.println("Test passed for containsTile");
        } else {
            System.out.println("Test Failed for containsTile");
        }
    }

    public void testPossibleSets() {
        ArrayList<Integer> startingBoard = new ArrayList<>();
        startingBoard.add(1);
        startingBoard.add(2);
        startingBoard.add(3);
        ArrayList<Integer> playersDeck = new ArrayList<>();
        playersDeck.add(4);
        playersDeck.add(6);
        playersDeck.add(7);
        playersDeck.add(8);

        ArrayList<ArrayList<Integer>> possibleSets = boardUtils.possibleSets(playersDeck, startingBoard);
        ArrayList<ArrayList<Integer>> expected = new ArrayList<>();
        ArrayList<Integer> first = new ArrayList<>();
        first.add(1);
        first.add(2);
        first.add(3);
        ArrayList<Integer> second = new ArrayList<>();
        second.add(1);
        second.add(2);
        second.add(3);
        second.add(4);
        ArrayList<Integer> third = new ArrayList<>();
        third.add(2);
        third.add(3);
        third.add(4);
        ArrayList<Integer> forth = new ArrayList<>();
        forth.add(6);
        forth.add(7);
        forth.add(8);
        expected.add(first);
        expected.add(second);
        expected.add(third);
        expected.add(forth);
        boolean possible = true;
        for(ArrayList<Integer> set: possibleSets) {
            if(!expected.contains(set)) {
                System.out.println("Test Failed for possibleSets");
                possible = false;
            } 
        }
        for(ArrayList<Integer> set: expected) {
            if(!possibleSets.contains(set)) {
                System.out.println("Test Failed for possibleSets");
                possible = false;
            } 
        }
        if(possible) {
            System.out.println("Test passed for possibleSets");
        }
    }

    // public void testValidBoard() {
    //     ArrayList<ArrayList<Integer>> newBoard = setUpTestBoard();
    //     boolean firstTest = boardUtils.validBoard(newBoard);
    //     newBoard.get(0).add(13);
    //     boolean secondTest = boardUtils.validBoard(newBoard);

    //     if(firstTest && !secondTest) {
    //         System.out.println("Test passed for validBoard");
    //     } else {
    //         System.out.println("Test Failed for validBoard");
    //     }
    // }


    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> b = new ArrayList<>();
        BoardUtils utils = new BoardUtils(b);
        BoardUtilsTest testInstance = new BoardUtilsTest(utils);
        testInstance.testDecompose();
        testInstance.testDecomposeEmptyInput();
        testInstance.testDeepCopy();
        testInstance.testDeepCopyEmptyInput();
        testInstance.testGetDifference();
        testInstance.testGetDifferenceEmptyInput();
        testInstance.testGetDifferenceSameInput();
        testInstance.testCustomRemove();
        testInstance.testCustomRemoveEmptyInput();
        testInstance.testCanCreateSet();
        testInstance.testCountIntegers();
        testInstance.testContainsTile();
        testInstance.testPossibleSets();
        //testInstance.testValidBoard();
    }
}

