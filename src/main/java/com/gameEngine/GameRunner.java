package com.gameEngine;

public class GameRunner {
    public static void main(String[] args) {
        GameEngine game = new GameEngine(2,2);
        game.gameLoop();
    }
}
