package com.gameEngine;

import com.example.GUI.StartScreensApplication;

public class GameRunner {
    static StartScreensApplication startScreensApplication = new StartScreensApplication();
    public static void main(String[] args) {
        startScreensApplication.main(args);
        GameEngine game = new GameEngine(2,2);
        game.gameLoop();
    }
}
