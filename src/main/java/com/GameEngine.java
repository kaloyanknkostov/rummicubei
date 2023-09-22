package com;
import java.util.ArrayList;

public class GameEngine {
    // private ArrayList<Tile> potOfTiles = new ArrayList<Tile>();
    public GameEngine(){
        // input number of real players and number of bots
        // add other game options
    }

    public void gameLoop(){
        // Starts the game loop which runs until a game ending event (quit button, or win, etc.)
        gameTurn();
        isGameEnding();
    }

    private void gameTurn(){
        // Simulate the turn of one player (either bot or normal)
        // waiting for the new board that we get from the gui#
        // Check if it is valid -> if not wait for new board
        // if valid then end turn
    }
    private boolean isGameEnding(){ // check game ending conditions
        // Quit button was pressed
        // someone won
        // pot of Tiles is empty
        return false;
    }

    private boolean isTurnValid(){
        return false;
    }

    private void generateTiles(){
        // Generate tiles at the start of the game and put them into the pot of Tiles
    }

}
