package com.gameEngine;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Collectors;


public class BaselineVsMcts {
    private final BaselineVsMctsGameModel gameModel = BaselineVsMctsGameModel.getInstance();
    private static final ArrayList<Tile> potOfTiles = new ArrayList<>();
    private final ArrayList<Player> listOfPlayers = new ArrayList<>();
    private Board board;
    private int currentPlayerIndex = 0;
    private int gameId = 0;
    private int moveNumber = 0;
    private int valueOfPrevTurn = 0;

    public static void main(String[] args) {
        System.out.println("start");
        BaselineVsMcts engine = new BaselineVsMcts();
        System.out.println("created the engine");
        //engine.generateTiles();
        engine.board = new Board();
        engine.botLoop();
    }


    public BaselineVsMcts() {
        generateTiles();
        currentDraw = getDrawnTile();
    }

    private Tile currentDraw;

    public ArrayList<Tile> removeUsedTiles(Board board, ArrayList<Tile> deck) {
        ArrayList<Set> setList = board.getSetList();
        for (Set currentSet : setList) {
            ArrayList<Tile> tileList = currentSet.getTilesList();
            for (Tile currentTile : tileList) {
                if (deck.contains(currentTile)) {
                    System.out.println("REMOVING " + currentTile);
                    deck.remove(currentTile);
                }
            }
        }
        return deck;
    }

    public void botLoop() {
        // Setting the random gameId
        Random random = new Random();
        this.gameId = random.nextInt();
        // Setup for the gameplay
        addPlayers();
        startLog();
        // Start logging for this game
        moveNumber++;
        String fileName = "Game" + gameId;
        writeGameStateLogToFile(fileName);
        // Main loop
        int turn = 0;
        while (turn < 10000) {
            Board oryginalBoard = board.copy();
            ArrayList<Tile> playerDeckCopy = new ArrayList<>(getCurrentPlayer().getDeckOfTiles());
            Board incomingBoard = gameTurn();
            //Check if the board is valid
            if (incomingBoard.checkBoardValidity()) {
                //If it's valid check if the player already got out, if not check, if he put 30 points on the board
                if (getCurrentPlayer().getIsOut() || checkThirtyRule(incomingBoard)) {
                    //if the board didn't change, then we set the hand to the copy of it draw a tile
                    if (board.getTilesInBoard().size() == incomingBoard.getTilesInBoard().size()) {
                        System.out.println("Player did nothing, drawing a tile");
                        getCurrentPlayer().setDeckOfTiles(playerDeckCopy);
                        drawTile();
                        board = oryginalBoard;
                    } else {
                        System.out.println("Player did a move!!");
                        board = incomingBoard.copy();
                        System.out.println("OLD DECK:");
                        System.out.println(playerDeckCopy);
                        System.out.println("NEW DECK");
                        System.out.println(getCurrentPlayer().getDeckOfTiles());
                        getCurrentPlayer().setDeckOfTiles(removeUsedTiles(incomingBoard, playerDeckCopy));
                        System.out.println("printing the board");
                        board.printBoard();
                    }
                } else {
                    System.out.println("Player did not play 30 points or used tile from the board, drawing a tile");
                    board = oryginalBoard;
                    getCurrentPlayer().setDeckOfTiles(playerDeckCopy);
                    drawTile();
                }
                // If the board is not valid print an error and set the tiles in the players hand to the copy of them before the move
            } else {
                getCurrentPlayer().setDeckOfTiles(playerDeckCopy);
                drawTile();
                board = oryginalBoard;
                System.out.println("Not a valid board, drawing a tile");
            }
            //here a player won
            if (getCurrentPlayer().getDeckOfTiles().isEmpty()) {
                System.out.println("Player " + currentPlayerIndex + " won!");
                break;
            }
            System.out.println("its turn: " + turn);

            turn++;
        }
        writeGameStateLogToFile();
        System.out.println("GAME WITH ID " + gameId + " ENDED");
    }

    private Board gameTurn() {
        if (currentPlayerIndex == 0) {
            currentPlayerIndex = 1;
        } else {
            currentPlayerIndex = 0;
        }
        logCurrentGameState();
        return baselineMove();
// commented out code switches the bots, right not its baseline vs baseline
//        if (currentPlayerIndex == 0) {
//            currentPlayerIndex++;
//            return baselineMove();
//        }
//        currentPlayerIndex++;
//        return mctsMove();
    }


    private Tile getDrawnTile() {
        System.out.println("There are " + potOfTiles.size() + " tiles");
        int index = (int) Math.floor(Math.random() * potOfTiles.size());
        if (index >= potOfTiles.size()) {
            System.out.println("cant draw tiles");
            index--;
            Tile a = potOfTiles.get(index);
            potOfTiles.remove(index);
            return a;
        } else {
            Tile a = potOfTiles.get(index);
            potOfTiles.remove(index);
            return a;
        }
    }


    private void generateTiles() {
        System.out.println("creating tiles");
        String[] colors = {"red", "blue", "black", "yellow"};
        for (String color : colors) {
            for (int i = 1; i < 14; i++) {
                potOfTiles.add(new Tile(i, color, false, "painted_tile_" + color + "_" + i + ".png"));
            }
        }
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

        for (int k = 0; k < 15; k++) {
            drawTile();
            currentPlayerIndex = 1;
            drawTile();
            currentPlayerIndex = 0;
        }
    }


    private StringBuilder gameStateLog = new StringBuilder();

    // New method to format the current game state into a CSV-compatible string
    private void logCurrentGameState() {
        System.out.println("Logging");
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
        gameStateLog.append(sj).append("\n");
    }

    private void startLog() {
        gameStateLog.append("GameId, Board, PlayersHands, MoveNumber, CurrentPlayer");
    }

    private void writeGameStateLogToFile() {
        String title = "game" + gameId;
        writeGameStateLogToFile(title);
    }

    private void writeGameStateLogToFile(String fileName) {
        try (FileWriter writer = new FileWriter("data/raw_data/" + fileName + ".csv")) {
            writer.write(gameStateLog.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Board mctsMove() {
        // TODO: add moves
        System.out.println("MCTS Move:");
        return null;
    }

    private Board baselineMove() {
        System.out.println("Player " + currentPlayerIndex + " Baseline Move:");
        return getCurrentPlayer().getNewBoard(board.copy());
    }

    private boolean checkThirtyRule(Board incomingBoard) {
        ArrayList<Set> newSets = new ArrayList<>(); // Stores sets that have been added to the board in the current round
        for (Set set : incomingBoard.getSetList())
            if (!board.getSetList().contains(set)) { // If the new board contains a set and the old one doesn't, we add it to newSets
                newSets.add(set);
            }
        for (Set set : board.getSetList()) {
            if (!incomingBoard.getSetList().contains(set)) {
                System.out.println("You can't the tiles in the board!");
                return false;
            }
        }
        int totalForTheRound = 0;
        for (Set set : newSets) {
            totalForTheRound += set.getValue(); // We count the total amount a player put on the board this round
        }
        if (totalForTheRound - valueOfPrevTurn >= 30) { // Checks if the player put at least 30 points on the board
            board = incomingBoard;
            newSets.clear(); // Reset before next turn
            valueOfPrevTurn = totalForTheRound;
            totalForTheRound -= totalForTheRound;
            System.out.println("Player " + getCurrentPlayer() + " got out");
            getCurrentPlayer().setIsOut(true);
            return true;
            // Here we check if the player just didnt do anything
            // TODO: we also check that in the main loop, it should be just checked once (we also draw a card there)
        } else if (board.getTilesInBoard().size() == incomingBoard.getTilesInBoard().size()) {
            return true;
        }
        System.out.println("Get more then 30");
        return false;
    }

    // Function that adds a tile to the current player's hand and replaces the currentDraw
    private void drawTile() {
        getCurrentPlayer().getDeckOfTiles().add(currentDraw);
        Tile draw = getDrawnTile();
        gameModel.setDrawable(draw);
        currentDraw = draw;
    }

}
