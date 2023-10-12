package com.gameEngine;

import com.example.GUI.GameModel;
import com.example.GUI.GameModel;
import com.example.GUI.StartScreensApplication;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletableFuture;

public class GameRunner {

    public static CompletableFuture<Void> gameStartSignal = new CompletableFuture<>();
    public static GameEngine game;

    public static void main(String[] args) {
        // Launch the GUI in its own thread
        Thread guiThread = new Thread(() -> {
            StartScreensApplication.launch(StartScreensApplication.class);
        });

        // Wait for the game to be signaled to start
        gameStartSignal.join();
        GameModel model = GameModel.getInstance();
        // Start the game logic
        // Launch the GUI in its own thread#
        
        guiThread.start();
        game.gameLoop();
    }
}