package com.gameEngine;

import com.example.GUI.GameModel;
import javafx.scene.image.Image;

import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringJoiner;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;


public class GameEngine {
    private final GameModel gameModel = GameModel.getInstance();
    private final ArrayList<Tile> potOfTiles = new ArrayList<>();
    private final ArrayList<Tile> potOfTilesCopy = new ArrayList<>();
    private final ArrayList<Player> listOfPlayers = new ArrayList<>();
    public Board board;
    public int numberOfRealPlayers;
    public int numberOfBots;
    private boolean endGame;
    private int currentPlayerIndex = 0;
    private Random random = new Random();
    private int gameId = random.nextInt();
    ;
    private int moveNumber = 0;
    private boolean logged = false;
    private final int startingTiles = 7;

    public static void main(String[] args) {

        GameEngine engine = new GameEngine();
        engine.numberOfRealPlayers = 0;
        engine.numberOfBots = 2;
        engine.board = new Board();
        engine.generateTiles();
        engine.gameLoop();
    }

    private Tile currentDraw;

    public void gameLoop() {
        // Game initialization
        addPlayers();
        startLog();
        gameModel.setCurrentPlayer(getCurrentPlayer());
        // Starts the game loop which runs until a game ending event (quit button, or win, etc.)
        currentDraw = getThisDrawnTile();

        while (!isGameEnding()) {

            logCurrentGameState();
            moveNumber++;
            String fileName = "Game" + Integer.toString(gameId);
            writeGameStateLogToFile(fileName);


            if (gameModel.isNextTurn() || getCurrentPlayer() instanceof ComputerPlayer) {
                gameModel.setNextTurn(false);
                ArrayList<Tile> copy = new ArrayList<>(getCurrentPlayer().getDeckOfTiles());
                Board incomingBoard = getIncomingBoard(); // first we need to get the incoming board which depens on whetehr we have a human or computer player so I moved that to a method


                if (sameBoardCheck(incomingBoard, copy)) { // first we check if the board is the same board as the one we started with, when that is the case we can insatntly handle the dame turn with ease.
                    board = incomingBoard;
                    gameTurn();
                } else if (incomingBoard.checkBoardValidity()) { // if it is not the exact same we check validity of the board

                    if (getCurrentPlayer().getIsOut()) { // if the player is out and the new board is valid then this would be a valid turn
                        sameBoardCheck(incomingBoard, copy);
                        board = incomingBoard;
                        gameTurn();
                    } else {
                        int valueOfTurn = getValueOfTurn(incomingBoard); // if they arent out yet we need to determine two things, one of which is the numerical value
                        boolean gotOut = checkOut(incomingBoard); // changed the method so its outside the main game loop and we call it here so we know whther they follow the rules for going out

                        if (valueOfTurn >= 30 && gotOut) { // if the value of the turn is more than 30 than this is a valid move
                            getCurrentPlayer().setIsOut(true); // we update the plyaer so that they havve player the thirty rule now
                            board = incomingBoard;
                            gameTurn();
                        } else {
                            if (getCurrentPlayer() instanceof ComputerPlayer) {  // if the computer cant play more than 30 points than we give it back its originl deck plus the drawn tile
                                getCurrentPlayer().setDeckOfTiles(copy);
                                getCurrentPlayer().getDeckOfTiles().add(currentDraw);
                                getThisDrawnTile();
                                gameTurn();
                            } else { // for human player we just tell them they dont have 30 points
                                System.out.println("Get more then 30");
                            }
                        }
                    }
                } else {
                    getCurrentPlayer().setDeckOfTiles(copy);
                    System.out.println("NOT VALID, DRAWING");
                    getCurrentPlayer().setDeckOfTiles(copy);
                    getCurrentPlayer().getDeckOfTiles().add(currentDraw);
                    getThisDrawnTile();
                    gameTurn();
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


    private boolean sameBoardCheck(Board incomingBoard, ArrayList<Tile> copy) { // checks if the board is the same returns true if it is and already draws tile
        boolean same = false;
        if (board.getTilesInBoard().size() == incomingBoard.getTilesInBoard().size()) {
            getCurrentPlayer().setDeckOfTiles(copy);
            getCurrentPlayer().getDeckOfTiles().add(currentDraw);
            getThisDrawnTile();
            same = true;
        }
        return same;

    }

    private boolean checkOut(Board incomingBoard) {
        boolean gotOut = true;
        for (Set set : board.getSetList()) {
            boolean contained = false;
            for (Set newSet : incomingBoard.getSetList()) {
                if (set.equals(newSet)) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                gotOut = false;
                //StartScreensApplication.getInstance().setMessageLabel(gameModel.playerNames.get(currentPlayerIndex), "You can't use the tiles on the board!");
                System.out.println("You can't the tiles in the board!");
                break;
            }

        }
        return gotOut;
    }

    private int getValueOfTurn(Board incomingBoard) {
        int valueOfTurn = 0;
        for (Set set : incomingBoard.getSetList()) {
            boolean present = false;
            for (Set boardSet : board.getSetList()) {
                if (set.equals(boardSet)) {
                    present = true;
                    break;
                }
            }
            if (!present) valueOfTurn += set.getValue();
        }
        return valueOfTurn;
    }

    private Tile getThisDrawnTile() {
        Tile draw = drawTile();
        gameModel.setDrawable(draw);
        currentDraw = draw;
        return draw;
    }

    private void setLengthOtherDecks() {

        ArrayList<Integer> deck_lengths = new ArrayList<Integer>();
        for (Player player : listOfPlayers) {
            // Check if the current object is not the one to be excluded
            if (!player.equals(getCurrentPlayer())) {
                // Add the object to the result list
                deck_lengths.add(player.getDeckOfTiles().size());
            }
        }
        getCurrentPlayer().setDeckLengths(deck_lengths);


    }

    private Board getIncomingBoard() {
        Board incomingBoard; // first we need to get the incoming board which depens on whetehr we have a human or computer player
        setLengthOtherDecks(); // Get length of other players decks:
        incomingBoard = getCurrentPlayer().getNewBoard(board);
        return incomingBoard;
    }

    private void gameTurn() {
        if (currentPlayerIndex == listOfPlayers.size() - 1) {
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex++;
        }
        //move to end maybe
        // System.out.println("NEW TURN " + String.valueOf(moveNumber)  + " current player: " + currentPlayerIndex );

        gameModel.setCurrentPlayer(getCurrentPlayer());

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
        // index=1;
        Tile a = potOfTiles.get(index);
        potOfTiles.remove(index);
        return a;
    }


    public void generateTiles() {

        boolean isJoker = false;

        String[] colors = {"red", "blue", "black", "yellow"};

        for (String color : colors) {
            for (int i = 1; i < 14; i++) {
                potOfTiles.add(new Tile(i, color, false, "painted_tile_" + color + "_" + i + ".png"));
            }
        }

        potOfTiles.add(new Tile(0, "", true, "painted_tile_3.png"));

    }

    public Player getCurrentPlayer() {
        return listOfPlayers.get(currentPlayerIndex);
    }

    public void addPlayers() {
        listOfPlayers.add(new ComputerPlayer("mcts 1", "mcts")); // player 0 is mcts
        listOfPlayers.add(new ComputerPlayer("mcts 2", "mcts"));
        for (int k = 0; k < startingTiles; k++) {
            listOfPlayers.get(0).drawTile(drawTile());
        }
        for (int k = 0; k < startingTiles; k++) {
            listOfPlayers.get(1).drawTile(drawTile());
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

    private StringBuilder gameStateLog = new StringBuilder();

    // New method to format the current game state into a CSV-compatible string
    private void logCurrentGameState() {
        String gameIdAsString = String.valueOf(gameId) ; // encode gameId
        String boardAsString = board.toString() ; //encode the board

        ArrayList<Tile> handOne = listOfPlayers.get(0).getDeckOfTiles();
        String handOneAsString = ""; // encode the first hand
        boolean firstTile = true;
        for (Tile tile : handOne) {
            if (firstTile) {
                firstTile = false;
                handOneAsString += String.valueOf(tile.turnToInt());
            } else {
                handOneAsString += " " + String.valueOf(tile.turnToInt());
            }
        }

        ArrayList<Tile> handTwo = listOfPlayers.get(1).getDeckOfTiles(); // encode the second hand
        String handTwoAsString = "";
        firstTile = true;
        for (Tile tile : handTwo) {
            if (firstTile) {
                firstTile = false;
                handTwoAsString += String.valueOf(tile.turnToInt());
            } else {
                handTwoAsString += " " + String.valueOf(tile.turnToInt());
            }
        }

        String moveNumberAsString = String.valueOf(this.moveNumber); // encode the move number
        String currentPlayerAsString = String.valueOf(this.currentPlayerIndex); // encode the current player index

        String result = gameIdAsString + ", " + boardAsString + ", " +handOneAsString +", " +handTwoAsString +", " + moveNumberAsString +", " + currentPlayerAsString;
        gameStateLog.append(result).append("\n");

    }

    private void startLog() {
        gameStateLog.append("GameId, Board, HandOne, HandTwo, MoveNumber, CurrentPlayer\n");
    }

    private void writeGameStateLogToFile(String fileName) {
        try (FileWriter writer = new FileWriter("data/raw_data/" + fileName + ".csv")) {
            writer.write(gameStateLog.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
