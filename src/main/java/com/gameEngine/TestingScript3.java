package com.gameEngine;

import java.util.concurrent.*;

public class TestingScript3 {
    public static void main(String[] args) {
        int numGenerations = 200;
        long maxTimeMillis = 60 * 2000; // 2 minutes in milliseconds

        // Create a thread pool with a fixed number of threads
        ExecutorService executor = Executors.newFixedThreadPool(numGenerations);

        for (int i = 0; i < numGenerations; i++) {
            System.out.println("Generation " + i);

            // Wrap the game loop in a Callable to execute it in a separate thread
            int finalI = i;
            Callable<Void> gameTask = () -> {
                GameEngine engine = new GameEngine();
                engine.numberOfRealPlayers = 0;
                engine.numberOfBots = 2;
                engine.board = new Board();
                engine.generateTiles();

                long startTime = System.currentTimeMillis();
                engine.gameLoop();
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;

                if (elapsedTime > maxTimeMillis) {
                    System.out.println("Generation " + finalI + " took more than 2 minutes, terminating.");
                }
                return null;
            };

            // Submit the game task to the thread pool and get a Future representing its execution
            Future<Void> gameFuture = executor.submit(gameTask);

            try {
                // Wait for the game task to finish or timeout
                gameFuture.get(maxTimeMillis, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // Handle interruption
            } catch (ExecutionException | TimeoutException e) {
                // Handle exceptions or timeout
                System.out.println("Generation " + i + " encountered an exception or timed out.");
                gameFuture.cancel(true); // Terminate the game loop thread
            }
        }

        // Shutdown the thread pool when done
        executor.shutdown();
    }
}
