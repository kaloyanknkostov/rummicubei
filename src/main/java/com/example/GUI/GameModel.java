package com.example.GUI;

import com.gameEngine.Board;
import com.gameEngine.Player;
import com.gameEngine.Tile;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class GameModel {
    private static GameModel instance;
    private boolean startGame = false;
    private int numberOfPlayers = 0;
    private Player currentPlayer;
    private boolean nextTurn = false;
    private int numberOfBots = 0;
    private ArrayList<ArrayList<Image>> transferBoardViaImages;
    public ArrayList<String> playerNames = new ArrayList();
    public void setCurrentBoard(Board currentBoard) {
    }

    public Tile drawTile;

    public void setNumberOfBots(int bots){
        this.numberOfBots = bots;
    }

    public int getNumberOfBots(){
        return this.numberOfBots;
    }
    public boolean isNextTurn() {
        return nextTurn;
    }

    public Tile getDrawTile(){
        return this.drawTile;
    }
    public void setDrawable(Tile t){
        this.drawTile = t;
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


    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public ArrayList<ArrayList<Image>> getTransferBoardViaImages() {
        return transferBoardViaImages;
    }

    public void setTransferBoardViaImages(ArrayList<ArrayList<Image>> transferBoardViaImages) {
        this.transferBoardViaImages = transferBoardViaImages;
    }

}

