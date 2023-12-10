package com.gameEngine;

import com.example.GUI.GameModel;
import com.example.GUI.StartScreensApplication;
import javafx.scene.image.Image;
import java.util.ArrayList;


public class GameEngine {
    private final GameModel gameModel = GameModel.getInstance();
    private final ArrayList<Tile> potOfTiles = new ArrayList<>();
    private final ArrayList<Tile> potOfTilesCopy = new ArrayList<>();
    private final ArrayList<Player> listOfPlayers = new ArrayList<>();
    private Board board;
    private int numberOfRealPlayers;
    private int numberOfBots;
    private boolean endGame;
    private int currentPlayerIndex = 0;


    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        Thread guiThread = new Thread(() -> StartScreensApplication.launch(StartScreensApplication.class));
        guiThread.start();
        while (!engine.gameModel.isStartGame()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        engine.numberOfRealPlayers = engine.gameModel.getNumberOfPlayers();
        engine.numberOfBots = 0;
        engine.board = new Board();
        engine.generateTiles();
        engine.gameLoop();
    }

    private Tile currentDraw;

    public void gameLoop() {
        addPlayers();
        StartScreensApplication.getInstance().setMessageLabel(gameModel.playerNames.get(currentPlayerIndex), "");
        gameModel.setCurrentPlayer(getCurrentPlayer());
        StartScreensApplication.activeController.playerTurn();
        gameModel.setCurrentBoard(board);
        // Starts the game loop which runs until a game ending event (quit button, or win, etc.)
        currentDraw = getThisDrawnTile();
        while (!isGameEnding()) {
            if (gameModel.isNextTurn()|| getCurrentPlayer() instanceof ComputerPlayer) {
                //System.out.println("the last board was:");
                gameModel.setNextTurn(false);
                //System.out.println("Image board:");
                //printBoard(gameModel.getTransferBoardViaImages());
                //System.out.println("---------------------------------------------------------------------------------");
                ArrayList<Tile> copy = new ArrayList<>(getCurrentPlayer().getDeckOfTiles());
                Board incomingBoard;
                if(getCurrentPlayer() instanceof HumanPlayer) {
                    incomingBoard = createBoardFromTiles(transformImagesToTiles());
                }
                else {
                     incomingBoard = getCurrentPlayer().getNewBoard(board);
                }
                //System.out.println("Incoming board (tiles)");
                //incomingBoard.printBoard();

                if (incomingBoard.checkBoardValidity()) {
                    if (getCurrentPlayer().getIsOut()) {
                        if (board.getTilesInBoard().size() == incomingBoard.getTilesInBoard().size()) {
                            getCurrentPlayer().setDeckOfTiles(copy);
                            getCurrentPlayer().getDeckOfTiles().add(currentDraw);
                            System.out.println(currentDraw.getPicture());
                            getThisDrawnTile();
                        }
                        board = incomingBoard;
                        System.out.println("VALID BOARD");
                        gameTurn();
                    } else {
                        int valueOfTurn = 0;
                        for (Set set : incomingBoard.getSetList())
                            if (!board.getSetList().contains(set))
                                valueOfTurn += set.getValue();
                        boolean gotOut = true;
                        for (Set set : board.getSetList()) {
                            if (!incomingBoard.getSetList().contains(set)) {
                                gotOut = false;
                                StartScreensApplication.getInstance().setMessageLabel(gameModel.playerNames.get(currentPlayerIndex), "You can't use the tiles on the board!");
                                System.out.println("You can't the tiles in the board!");
                                break;
                            }
                        }
                        if (valueOfTurn >= 30 && gotOut) {
                            getCurrentPlayer().setIsOut(true);
                            board = incomingBoard;
                            System.out.println("VALID BOARD");
                            gameTurn();
                        } else {
                            if (board.getTilesInBoard().size() == incomingBoard.getTilesInBoard().size()) {
                                getCurrentPlayer().setDeckOfTiles(copy);
                                getCurrentPlayer().getDeckOfTiles().add(currentDraw);
                                System.out.println(currentDraw.getPicture());
                                getThisDrawnTile();
                                System.out.println("VALID BOARD");
                                gameTurn();
                            } else {
                                System.out.println("Get more then 30");
                                StartScreensApplication.getInstance().setMessageLabel(gameModel.playerNames.get(currentPlayerIndex), "You need to get more then 30 points!");
                            }
                        }
                    }
                } else {
                    getCurrentPlayer().setDeckOfTiles(copy);
                    StartScreensApplication.getInstance().setMessageLabel(gameModel.playerNames.get(currentPlayerIndex), "Not a valid board");
                    System.out.println("NOT VALID");
                }
            } else {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("GAME FINISHED");
    }

    private Tile getThisDrawnTile() {
        Tile draw = drawTile();
        gameModel.setDrawable(draw);
        currentDraw = draw;
        System.out.println(draw.getPicture());
        return draw;
    }

    private void gameTurn() {
        if (currentPlayerIndex == listOfPlayers.size() - 1) {
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex++;
        }
        gameModel.setCurrentPlayer(getCurrentPlayer());
        StartScreensApplication.activeController.playerTurn();
        for (String s : gameModel.playerNames) {
            System.out.println(s);
        }
        System.out.println("changing that to the player index: " + currentPlayerIndex);
        StartScreensApplication.getInstance().setMessageLabel(gameModel.playerNames.get(currentPlayerIndex), "");
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
        index=1;
        Tile a = potOfTiles.get(index);
        potOfTiles.remove(index);
        return a;
    }


    private void generateTiles() {

        boolean isJoker = false;

        String[] colors = {"red", "blue", "black", "yellow"};

        for (String color : colors) {
            for (int i = 1; i < 14; i++) {
                potOfTiles.add(new Tile(i, color, false, "painted_tile_" + color + "_" + i + ".png"));
            }
        }

        potOfTilesCopy.addAll(potOfTiles);
        int a = potOfTiles.size();
        for (int i = 0; i < a; i++) {
            potOfTiles.add(potOfTiles.get(i));
        }

        potOfTiles.add(new Tile(0, "", true, "painted_tile_1.png"));
        potOfTiles.add(new Tile(0, "", true, "painted_tile_3.png"));

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

    public void printBoard(ArrayList<ArrayList<Image>> board) {
        for (ArrayList<Image> row : board) {
            for (Image img : row) {
                if (img == null) {
                    System.out.print("-");
                } else {
                    System.out.print("+");
                }
            }
            System.out.println();
        }
    }


}
