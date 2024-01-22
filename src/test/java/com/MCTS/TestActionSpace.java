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
        ArrayList<ArrayList<Integer>> result = generator.sortPossibleSets(generator.getPossibleSets(), boardAsList);
        ArrayList<ArrayList<Integer>> expected_result = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(6, 7, 8, 9, 10)),
                new ArrayList<>(Arrays.asList(5, 6, 7, 8, 9)),
                new ArrayList<>(Arrays.asList(4, 5, 6, 7, 8)),
                new ArrayList<>(Arrays.asList(3, 4, 5, 6, 7)),
                new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6)),
                new ArrayList<>(Arrays.asList(6, 7, 8, 9)),
                new ArrayList<>(Arrays.asList(7, 8, 9, 10, 11)),
                new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)),
                new ArrayList<>(Arrays.asList(7, 8, 9, 10)),
                new ArrayList<>(Arrays.asList(5, 6, 7, 8)),
                new ArrayList<>(Arrays.asList(4, 5, 6, 7)),
                new ArrayList<>(Arrays.asList(3, 4, 5, 6)),
                new ArrayList<>(Arrays.asList(2, 3, 4, 5)),
                new ArrayList<>(Arrays.asList(1, 2, 3, 4)),
                new ArrayList<>(Arrays.asList(7, 8, 9)),
                new ArrayList<>(Arrays.asList(6, 7, 8)),
                new ArrayList<>(Arrays.asList(2, 3, 4)),
                new ArrayList<>(Arrays.asList(8, 9, 10, 11, 12)),
                new ArrayList<>(Arrays.asList(8, 9, 10, 11)),
                new ArrayList<>(Arrays.asList(8, 9, 10)),
                new ArrayList<>(Arrays.asList(5, 6, 7)),
                new ArrayList<>(Arrays.asList(4, 5, 6)),
                new ArrayList<>(Arrays.asList(3, 4, 5)),
                new ArrayList<>(Arrays.asList(1, 2, 3)),
                new ArrayList<>(Arrays.asList(9, 10, 11, 12)),
                new ArrayList<>(Arrays.asList(9, 10, 11)),
                new ArrayList<>(Arrays.asList(10, 11, 12))
        ));
        //System.out.println(result);
        assertEquals(result, expected_result);
    }
}
