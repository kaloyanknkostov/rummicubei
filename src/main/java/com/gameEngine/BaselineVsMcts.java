package com.gameEngine;

import com.example.GUI.StartScreensApplication;
import javafx.scene.image.Image;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Collectors;


public class BaselineVsMcts {
    private final BaselineVsMctsGameModel gameModel = BaselineVsMctsGameModel.getInstance();
    private final ArrayList<Tile> potOfTiles = new ArrayList<>();
    private final ArrayList<Tile> potOfTilesCopy = new ArrayList<>();
    private final ArrayList<Player> listOfPlayers = new ArrayList<>();
    private Board board;
    private int numberOfBots;
    private boolean endGame;
    private int currentPlayerIndex = 0;
    private int gameId = 0;
    private int moveNumber  = 0;

    public static void main(String[] args) {
        BaselineVsMcts engine = new BaselineVsMcts();
        engine.numberOfBots = 2;
        engine.board = new Board();
        engine.generateTiles();
        engine.gameLoop();
    }

    private Tile currentDraw;

    public void bootLoop(){
        // Setting the random gameId
        Random random = new Random();
        this.gameId = random.nextInt();
        // Setup for the gameplay
        addPlayers();
        startLog();
        gameModel.setCurrentBoard(board);
        currentDraw = getThisDrawnTile();
        // Start logging for this game
        logCurrentGameState();
        moveNumber++;
        System.out.println("LOGGING");
        System.out.println(gameStateLog);
        String fileName = "Game" + Integer.toString(gameId);
        writeGameStateLogToFile(fileName);
        System.out.println("DONE");
        // Main loop
        while (!isGameEnding()){
            ArrayList<Tile> playerDeckCopy = new ArrayList<>(getCurrentPlayer().getDeckOfTiles());
            Board incomingBoard;
            incomingBoard = getCurrentPlayer().getNewBoard(board);
            if (incomingBoard.checkBoardValidity()) {
                if (true) {
                    if (board.getTilesInBoard().size() == incomingBoard.getTilesInBoard().size()) {
                        getCurrentPlayer().setDeckOfTiles(playerDeckCopy);
                        getCurrentPlayer().getDeckOfTiles().add(currentDraw);
                        System.out.println(currentDraw.getPicture());
                        getThisDrawnTile();
                    }
                    board = incomingBoard;
                    System.out.println("VALID BOARD");
                    gameTurn();
                } else {
                }
            } else {
                getCurrentPlayer().setDeckOfTiles(playerDeckCopy);
                System.out.println("Not a valid board");
            }

        }

    }
    public void gameLoop() {
        addPlayers();
        startLog();
        StartScreensApplication.getInstance().setMessageLabel(gameModel.playerNames.get(currentPlayerIndex), "");
        gameModel.setCurrentPlayer(getCurrentPlayer());
        StartScreensApplication.activeController.playerTurn();
        gameModel.setCurrentBoard(board);
        // Starts the game loop which runs until a game ending event (quit button, or win, etc.)
        currentDraw = getThisDrawnTile();
        while (!isGameEnding()) {
            //if(!logged){
                logCurrentGameState();
                moveNumber++;
                //logged = true;
                System.out.println("LOGGING");
                System.out.println(gameStateLog);
                String fileName = "Game" + Integer.toString(gameId);
                writeGameStateLogToFile(fileName);
                System.out.println("DONE");
            //}
            if (gameModel.isNextTurn()|| getCurrentPlayer() instanceof ComputerPlayer) {
                gameModel.setNextTurn(false);
                ArrayList<Tile> copy = new ArrayList<>(getCurrentPlayer().getDeckOfTiles());
                Board incomingBoard;
                System.out.println("Computer is playing");
                incomingBoard = getCurrentPlayer().getNewBoard(board);
                if (incomingBoard.checkBoardValidity()) {
                    if (true) {
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
                                StartScreensApplication.getInstance().setMessageLabel("1", "You need to get more then 30 points!");
                            }
                        }
                    }
                } else {
                    getCurrentPlayer().setDeckOfTiles(copy);
                    StartScreensApplication.getInstance().setMessageLabel("1", "Not a valid board");
                    System.out.println("NOT VALID");
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
        //move to end maybe
        gameModel.setCurrentPlayer(getCurrentPlayer());
        StartScreensApplication.activeController.playerTurn();
        for (String s : gameModel.playerNames) {
            System.out.println(s);
        }
        System.out.println("changing that to the player index: " + currentPlayerIndex);
        if (getCurrentPlayer() instanceof HumanPlayer) {
            StartScreensApplication.getInstance().setMessageLabel(gameModel.playerNames.get(currentPlayerIndex), "");
        }
        else {
            StartScreensApplication.getInstance().setMessageLabel("Bot", "");
        }
        System.out.println(board);
        StartScreensApplication.activeController.updateBoard(board);
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

    private boolean isGameEnding() { // check game ending conditions
        if (listOfPlayers.get(currentPlayerIndex).getDeckOfTiles().isEmpty()) {
            return true;
        }
        return potOfTiles.isEmpty();
    }


    private Tile drawTile() {
        int index = (int) Math.floor(Math.random() * potOfTiles.size());
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
        // TODO: Adding the baseline bot
        listOfPlayers.add(new ComputerPlayer("test"));
        // TODO: Adding the MCTS bot
        listOfPlayers.add(new ComputerPlayer("test"));
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

    private StringBuilder gameStateLog = new StringBuilder();

    // New method to format the current game state into a CSV-compatible string
    private void logCurrentGameState() {
        StringJoiner sj = new StringJoiner(",");
        sj.add(String.valueOf(gameId)); // Logging gameId
        sj.add(board.toString()); // Logging the board
        sj.add(listOfPlayers.stream()
                .map(player -> player.getDeckOfTiles().stream()
                        .map(Tile::toString)
                        .collect(Collectors.joining(" ")))
                .collect(Collectors.joining(";"))); // Logging player's hands
        sj.add(String.valueOf(moveNumber)); // Logging moveNumber
        sj.add(Integer.toString(currentPlayerIndex)); // Logging the current player
        gameStateLog.append(sj.toString()).append("\n");
    }

    private void startLog(){
        gameStateLog.append("GameId, Board, PlayersHands, MoveNumber, CurrentPlayer");
    }

    private void writeGameStateLogToFile(String fileName) {
        try (FileWriter writer = new FileWriter("data/raw_data/" + fileName + ".csv")) {
            writer.write(gameStateLog.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mctsMove(){
        System.out.println("MCTS Move:");
    }

    private void baselineMove(){
        System.out.println("MCTS Move:");
    }

    private boolean checkThirtyRule(Board incomingBoard, ArrayList<Tile> playerDeckCopy){
        int valueOfTurn = 0;
        for (Set set : incomingBoard.getSetList())
            if (!board.getSetList().contains(set))
                valueOfTurn += set.getValue();
        boolean gotOut = true;
        for (Set set : board.getSetList()) {
            if (!incomingBoard.getSetList().contains(set)) {
                gotOut = false;
                System.out.println("You can't use the tiles in the board!");
                break;
            }
        }
        if (valueOfTurn >= 30 && gotOut) {
            getCurrentPlayer().setIsOut(true);
            board = incomingBoard;
            return true;
        } else {
            if (board.getTilesInBoard().size() == incomingBoard.getTilesInBoard().size()) {
                getCurrentPlayer().setDeckOfTiles(playerDeckCopy);
                getCurrentPlayer().getDeckOfTiles().add(currentDraw);
                System.out.println(currentDraw.getPicture());
                getThisDrawnTile();
                System.out.println("VALID BOARD");
                gameTurn();
            } else {
                return  false;
            }
        }
        return true;
    }

}
