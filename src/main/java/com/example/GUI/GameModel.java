package com.example.GUI;

import com.gameEngine.Board;
import com.gameEngine.Player;
import com.gameEngine.Tile;

public class GameModel {
    private static GameModel instance;
    private boolean startGame = false;
    private int numberOfPlayers = 0;
    private Player currentPlayer;
    private boolean nextTurn = false;

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public void setCurrentBoard(Board currentBoard) {
        this.currentBoard = currentBoard;
    }

    public Board getBoardToCheck() {
        return boardToCheck;
    }

    public void setBoardToCheck(Board boardToCheck) {
        this.boardToCheck = boardToCheck;
    }

    private Board currentBoard;
    private Board boardToCheck;
    public boolean isNextTurn() {
        return nextTurn;
    }

    public void setNextTurn(boolean nextTurn) {
        this.nextTurn = nextTurn;
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    private GameModel() {
    }

    public boolean isStartGame() {
        return startGame;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }

    public static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }

    public Tile getTileByImageUrl(String imageUrl) {
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        for (Tile tile : getCurrentPlayer().getDeckOfTiles()) {
            System.out.println("Checking tile: " + tile.getPicture());
            if (tile.getPicture().equals(fileName)) { // Using the filename for comparison
                System.out.println("Found matching tile for URL: " + imageUrl);
                return tile;
            }
        }
        System.out.println("No tile found for URL: " + imageUrl);
        return null;
    }


    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

}

