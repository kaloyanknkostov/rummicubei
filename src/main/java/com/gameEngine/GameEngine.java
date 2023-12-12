package com.gameEngine;

import com.example.GUI.GameModel;
import com.example.GUI.StartScreensApplication;
import javafx.animation.FadeTransition;
import javafx.scene.image.Image;

import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.stream.Collectors;


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
    private int valueOfPrevTurn = 0; 

    private final int gameId = 0;
    private int moveNumber  = 0;
    private boolean logged = false;

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
        engine.numberOfBots = 1;
        engine.board = new Board();
        engine.generateTiles();
        engine.gameLoop();
    }

    private Tile currentDraw;

    public void gameLoop() {
        generateTiles();
       // currentDraw = getDrawnTile();
        addPlayers();
        startLog();
        StartScreensApplication.getInstance().setMessageLabel(gameModel.playerNames.get(currentPlayerIndex), "");
        gameModel.setCurrentPlayer(getCurrentPlayer());
        StartScreensApplication.activeController.playerTurn();
        gameModel.setCurrentBoard(board);
        // Starts the game loop which runs until a game ending event (quit button, or win, etc.)
        currentDraw = getThisDrawnTile();

        int turn = 0;
        Board incomingBoard; 
        while (turn < 10000) {
            System.out.println(turn); 
           
            while (!gameModel.isNextTurn()) {
                if (gameModel.isNextTurn()){
                System.out.println("working");
                }else {
                    try{
                     System.out.println("waiting");
                     Thread.sleep(200);
                  }catch(InterruptedException e){
                         e.printStackTrace();
                     }
   }}
            
            System.out.println(gameModel.isNextTurn());
            gameModel.setNextTurn(false);
            ArrayList<Tile> playerDeckCopy = new ArrayList<>(getCurrentPlayer().getDeckOfTiles());
            if(getCurrentPlayer() instanceof HumanPlayer) {
                    incomingBoard = createBoardFromTiles(transformImagesToTiles());
                }
                else {
                    System.out.println("Computer is playing");
                    incomingBoard = getCurrentPlayer().getNewBoard(board);
                }

            //Check if the board is valid
            if (incomingBoard.checkBoardValidity()) {
                //If it's valid check if the player already got out, if not check if he put 30 points on the board
                if (getCurrentPlayer().getIsOut() || checkThirtyRule(incomingBoard, playerDeckCopy)) {
                    //if the board didn't change, then we set the hand to the copy of it draw a tile
                    if (board.getTilesInBoard().size() == incomingBoard.getTilesInBoard().size()) {
                        System.out.println("Player did nothing, drawing a tile");
                        getCurrentPlayer().setDeckOfTiles(playerDeckCopy);
                        drawTile();
                        gameTurn();
                    }
                    System.out.println("Player did a move!!");
                    board = incomingBoard;
                    gameTurn();
                } else {
                    System.out.println("Player did not play 30 points or used tile from the board, drawing a tile");
                    getCurrentPlayer().setDeckOfTiles(playerDeckCopy);
                    drawTile();
                    gameTurn();
                }
            // If the board is not valid print an error and set the tiles in the players hand to the copy of them before the move
            } else {
                getCurrentPlayer().setDeckOfTiles(playerDeckCopy);
                drawTile();
                gameTurn();
                System.out.println("Not a valid board, drawing a tile");
            }
            //here a player won
            if(getCurrentPlayer().getDeckOfTiles().isEmpty()){
                System.out.println("Player " + currentPlayerIndex + " won!");
                break;
            }
            turn++;
        }
        System.out.println("GAME WITH ID "+ gameId + " ENDED");
    }


       
    /*    while (!isGameEnding()) {
            if(!logged){
                logCurrentGameState();
                moveNumber++;
                logged = true;
                System.out.println("LOGGING");
                System.out.println(gameStateLog);
                String fileName = "Game" + Integer.toString(gameId);
                writeGameStateLogToFile(fileName);
                System.out.println("DONE");
            }
            if (gameModel.isNextTurn()|| getCurrentPlayer() instanceof ComputerPlayer) {
               
                //System.out.println("the last board was:");
                gameModel.setNextTurn(false);
                //System.out.println("Image board:");
                //printBoard(gameModel.getTransferBoardViaImages());
                //System.out.println("---------------------------------------------------------------------------------");
                ArrayList<Tile> copy = new ArrayList<>(getCurrentPlayer().getDeckOfTiles());
                Board incomingBoard;
                ArrayList<Set> newSets = new ArrayList<>(); 
                if(getCurrentPlayer() instanceof HumanPlayer) {
                    incomingBoard = createBoardFromTiles(transformImagesToTiles());
                }
                else {
                    System.out.println("Computer is playing");
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
                        for (Set set : incomingBoard.getSetList())
                            if (!board.getSetList().contains(set))
                                //valueOfTurn += set.getValue();
                                newSets.add(set);
                        boolean gotOut = true;
                        for (Set set : board.getSetList()) {
                            if (!incomingBoard.getSetList().contains(set)) {
                                //gotOut = false;
                                StartScreensApplication.getInstance().setMessageLabel(gameModel.playerNames.get(currentPlayerIndex), "You can't use the tiles on the board!");
                                System.out.println("You can't the tiles in the board!");
                                break;
                            }
                        }
                        int totalForTheRound=0;
                        for(Set set: newSets) {
                            System.out.println(set.toString());
                            totalForTheRound+=set.getValue();
                        }
                        if (totalForTheRound - valueOfPrevTurn >= 30 && gotOut) {
                            getCurrentPlayer().setIsOut(true);
                            board = incomingBoard;
                            System.out.println("VALID BOARD");
                            gameTurn();
                            newSets.clear();
                            valueOfPrevTurn = totalForTheRound;
                            System.out.println("value of turn: "+totalForTheRound);
                            totalForTheRound-=totalForTheRound;
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
            } else {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("GAME FINISHED");
        */
    
    

    private Tile getThisDrawnTile() {
        Tile draw = drawTile();
        gameModel.setDrawable(draw);
        currentDraw = draw;
        System.out.println(draw.getPicture());
        return draw;
    }

    private void gameTurn() {
        gameModel.setCurrentBoard(board);
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
       // index=1;
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
    private boolean checkThirtyRule(Board incomingBoard, ArrayList<Tile> playerDeckCopy) {
        ArrayList<Tile> copy = new ArrayList<>(getCurrentPlayer().getDeckOfTiles());
        ArrayList<Set> newSets = new ArrayList<>(); // Stores sets that have been added to the board in the current round
        for (Set set : incomingBoard.getSetList())
            if (!board.getSetList().contains(set)){ // If the new board contains a set and the old one doesn't, we add it to newSets
                newSets.add(set);
            }
        boolean gotOut = true;
        for (Set set : board.getSetList()) {
            if (!incomingBoard.getSetList().contains(set)) {
                System.out.println("You can't the tiles in the board!");
                return false;
            }
        }
        int totalForTheRound = 0;
        for (Set set : newSets) {
            System.out.println(set.toString());
            totalForTheRound+=set.getValue(); // We count the total amount a player put on the board this round
        }
        if (totalForTheRound - valueOfPrevTurn >= 30 && gotOut) { // Checks if the player put at least 30 points on the board
            getCurrentPlayer().setIsOut(true);
            board = incomingBoard;
            newSets.clear(); // Reset before next turn
            valueOfPrevTurn = totalForTheRound;
            //System.out.println("value of turn: " + totalForTheRound);
            totalForTheRound -= totalForTheRound;
            return true;
            // Here we check if the player just didnt do anything
            // TODO: we also check that in the main loop, it should be just checked once (we also draw a card there)
        } else if (board.getTilesInBoard().size() == incomingBoard.getTilesInBoard().size()) {
            return true;
        }
        System.out.println("Get more then 30");
        return false;
    }


}
