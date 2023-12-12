package com.gameEngine;

public class test {

    public static void main(String[] args) {
        BaselineVsMcts model = new BaselineVsMcts();
        Board testBoard = new Board();
        model.runTimeComparition(testBoard, 0);
    }
}
