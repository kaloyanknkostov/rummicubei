package com.MCTS;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TestActionSpace {
    @Test
    public void testSortPossibleSets() {
        ArrayList<Integer> boardSetOne = new ArrayList<>(Arrays.asList(2, 3, 4));
        ArrayList<Integer> boardSetTwo = new ArrayList<>(Arrays.asList(6, 7, 8, 9));
        ArrayList<Integer> hand = new ArrayList<>(Arrays.asList(1, 5, 10, 11, 12));
        ArrayList<ArrayList<Integer>> board = new ArrayList<>();
        board.add(boardSetOne);
        board.add(boardSetTwo);
        ArrayList<Integer> boardAsList = new ArrayList<>(Arrays.asList(2, 3, 4, 6, 7, 8, 9));
        ActionSpaceGenerator generator = new ActionSpaceGenerator(board, hand);
        System.out.println(generator.sortPossibleSets(generator.getPossibleSets(), boardAsList) );
    }
}
