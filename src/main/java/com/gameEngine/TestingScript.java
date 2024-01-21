package com.gameEngine;

public class TestingScript {
    public static void main(String[] args) {
        long maxTimeMillis = 60 * 2000; // 1 minute in milliseconds
        for (int i = 0; i < 200; i++) {
            System.out.println("Generation " + i);
            GameEngine engine = new GameEngine();
            engine.numberOfRealPlayers = 0;
            engine.numberOfBots = 2;
            engine.board = new Board();
            engine.generateTiles();

            Thread gameThread = new Thread(() -> {
                engine.gameLoop();
            });

            long startTime = System.currentTimeMillis();
            gameThread.start();

            try {
                gameThread.join(maxTimeMillis); // Wait for the game loop thread to finish or timeout
            } catch (InterruptedException e) {
                // Handle interruption
            }

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            if (elapsedTime > maxTimeMillis) {
                System.out.println("Generation " + i + " took more than 2 minutes, moving to the next one.");
                gameThread.interrupt(); // Forcefully interrupt the game loop thread
            }
        }
    }
}


