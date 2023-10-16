package com.gameEngine;

import com.example.GUI.GameModel;
import com.example.GUI.StartScreensApplication;
import javafx.scene.image.Image;

import java.util.ArrayList;


public class GameEngine {
    private final GameModel gameModel = GameModel.getInstance();
    private final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GameEngine.class.getName());
    private final ArrayList<Tile> potOfTiles = new ArrayList<Tile>();
    private final ArrayList<Tile> potOfTilesCopy = new ArrayList<>();
    private final ArrayList<Player> listOfPlayers = new ArrayList<Player>();
    private Board board;
    private int numberOfRealPlayers;
    private int numberOfBots;
    private boolean endGame;
    private int currentPlayerIndex = 0;

    public static void main(String[] args) {
        GameEngine engine = new GameEngine();

        Thread guiThread = new Thread(() -> {
            StartScreensApplication.launch(StartScreensApplication.class);
        });
        guiThread.start();
        while (!engine.gameModel.isStartGame()) {
            try {
                Thread.sleep(1000);  // waits for 100 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //set up for game
        engine.numberOfRealPlayers = engine.gameModel.getNumberOfPlayers();
        engine.numberOfBots = 0;
        engine.board = new Board();
        engine.endGame = false;
        engine.generateTiles();
        engine.gameLoop();
    }


    public void gameLoop() {
        //Sets up the gameloop
        addPlayers();
        gameModel.setCurrentPlayer(getCurrentPlayer());
        StartScreensApplication.activeController.playerTurn();
        gameModel.setCurrentBoard(board);
        // Starts the game loop which runs until a game ending event (quit button, or win, etc.)
        while (!isGameEnding()) {
            if (gameModel.isNextTurn()) {
                gameModel.setNextTurn(false);
                ArrayList<Tile> copy = new ArrayList<>(getCurrentPlayer().getDeckOfTiles());
                Board incomingBoard = createBoardFromTiles(transformImagesToTiles());
                System.out.println("Incoming board");
                incomingBoard.printBoard();
                if (incomingBoard.checkBoardValidity()) {
                    if (getCurrentPlayer().getIsOut()) {
                        if (board.getTilesInBoard().size() == incomingBoard.getTilesInBoard().size()) {
                            getCurrentPlayer().setDeckOfTiles(copy);
                            getCurrentPlayer().getDeckOfTiles().add(drawTile());
                        }

                        board = incomingBoard;
                        System.out.println("VALID BOARD");
                        gameTurn();
                    } else {
                        int valueOfTurn = 0;
                        for (Set set : incomingBoard.getSetList())
                            valueOfTurn += set.getValue();
                        if (valueOfTurn >= 30) {
                            getCurrentPlayer().setIsOut(true);
                            board = incomingBoard;
                            System.out.println("VALID BOARD");
                            gameTurn();
                        } else {
                            if (board.getTilesInBoard().size() == incomingBoard.getTilesInBoard().size()) {
                                getCurrentPlayer().setDeckOfTiles(copy);
                                getCurrentPlayer().getDeckOfTiles().add(drawTile());

                                System.out.println("VALID BOARD");
                                gameTurn();
                            } else System.out.println("Get more then 30");
                        }
                    }

                } else {
                    getCurrentPlayer().setDeckOfTiles(copy);
                    System.out.println("NOT VALID");
                }
            } else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("GAME FINISHED");
    }

    private void gameTurn() {
        if (currentPlayerIndex == listOfPlayers.size() - 1) {
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex++;
        }
        gameModel.setCurrentPlayer(getCurrentPlayer());
        StartScreensApplication.activeController.playerTurn();
        gameModel.setCurrentBoard(board);

    }


    private Board createBoardFromTiles(ArrayList<ArrayList<Tile>> map) {
        Board newBoard = new Board();
        for (ArrayList<Tile> row : map) {
            Set set = new Set();
            for (Tile tile : row) {
                if (tile != null) {
                    set.addTile(tile);
                } else {
                    if (!set.isEmpty()) {
                        newBoard.addSet(set);
                        set = new Set();
                    }
                }
            }
            if (!set.isEmpty()) newBoard.addSet(set);
        }
        return newBoard;
    }


    private ArrayList<ArrayList<Tile>> transformImagesToTiles() {
        ArrayList<ArrayList<Image>> potentialNewBoard = gameModel.getTransferBoardViaImages();
        ArrayList<ArrayList<Tile>> board2D = new ArrayList<>();
        ArrayList<Tile> listOfBoardTiles = board.getTilesInBoard();
        ArrayList<Tile> listOfPlayerTiles = listOfPlayers.get(currentPlayerIndex).getDeckOfTiles();
        for (ArrayList<Image> row : potentialNewBoard) {
            ArrayList<Tile> imageToTile = new ArrayList<>();
            for (Image image : row) {
                if (image != null) {

                    boolean checker = false;
                    for (Tile placedTile : listOfBoardTiles) {
                        if (placedTile.getImage().equals(image)) {
                            imageToTile.add(placedTile);
                            checker = true;
                            break;
                        }
                    }
                    if (!checker) {
                        for (Tile playerTile : listOfPlayerTiles) {
                            if (playerTile.getImage().equals(image)) {
                                listOfPlayers.get(currentPlayerIndex).getDeckOfTiles().remove(playerTile);
                                imageToTile.add(playerTile);
                                checker = true;
                                break;
                            }
                        }
                    }

                    if (!checker) {
                        for (Tile placedTile : potOfTilesCopy) {
                            if (placedTile.getImage().equals(image)) {
                                imageToTile.add(placedTile);
                                break;
                            }
                        }
                    }

                } else {
                    imageToTile.add(null);
                }
            }
            board2D.add(imageToTile);
        }
        return board2D;
    }


    private boolean isGameEnding() { // check game ending conditions
        if (listOfPlayers.get(currentPlayerIndex).getDeckOfTiles().isEmpty()) {
            return true;
        }

        return potOfTiles.isEmpty();
    }


    private Tile drawTile() {
        int index = (int) Math.floor(Math.random() * potOfTiles.size());
        //index=0;
        Tile a = potOfTiles.get(index);
        potOfTiles.remove(index);
        return a;
    }


    private void generateTiles() {

        boolean isJoker = false;
        for (int i = 1; i < 14; i++) {
            potOfTiles.add(new Tile(i, "red", false, "painted_tile_red_" + i + ".png"));
        }
        for (int i = 1; i < 14; i++) {
            potOfTiles.add(new Tile(i, "blue", false, "painted_tile_blue_" + i + ".png"));
        }
        for (int i = 1; i < 14; i++) {
            potOfTiles.add(new Tile(i, "black", false, "painted_tile_black_" + i + ".png"));
        }
        for (int i = 1; i < 14; i++) {
            potOfTiles.add(new Tile(i, "yellow", false, "painted_tile_yellow_" + i + ".png"));
        }
        for (int i = 0; i < potOfTiles.size(); i++) {
            potOfTilesCopy.add(potOfTiles.get(i));
        }
        int a =potOfTiles.size();
        for (int i = 0; i < a; i++) {
            potOfTiles.add(potOfTiles.get(i));
        }
         isJoker = true;

        potOfTiles.add(new Tile(0, "", isJoker, "painted_tile_1.png"));
        potOfTiles.add(new Tile(0, "", isJoker, "painted_tile_3.png"));

    }

    public Player getCurrentPlayer() {
        return listOfPlayers.get(currentPlayerIndex);
    }

    public void addPlayers() {
        for (int i = 0; i < numberOfRealPlayers; i++) {
            listOfPlayers.add(new HumanPlayer("test"));
            for (int k = 0; k < 15; k++) {
                listOfPlayers.get(listOfPlayers.size() - 1).drawTile(drawTile());
            }

        }
        for (int i = 0; i < numberOfBots; i++) {
            listOfPlayers.add(new ComputerPlayer("test"));
            System.out.println("Added one");

            for (int k = 0; k < 15; k++) {
                listOfPlayers.get(listOfPlayers.size() - 1).drawTile(drawTile());
            }
        }
    }


}
