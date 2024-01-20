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
        // Game initialization
        addPlayers();
        startLog();
        StartScreensApplication.getInstance().setMessageLabel(gameModel.playerNames.get(currentPlayerIndex), "");
        gameModel.setCurrentPlayer(getCurrentPlayer());
        StartScreensApplication.activeController.playerTurn();
        // gameModel.setCurrentBoard(board); why was that function there?
        // Starts the game loop which runs until a game ending event (quit button, or win, etc.)
        currentDraw = getThisDrawnTile(); 
        
        while (!isGameEnding()) {
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

            if (gameModel.isNextTurn() || getCurrentPlayer() instanceof ComputerPlayer) {
                gameModel.setNextTurn(false);
                ArrayList<Tile> copy = new ArrayList<>(getCurrentPlayer().getDeckOfTiles());
                Board incomingBoard = getIncomingBoard(); // first we need to get the incoming board which depens on whetehr we have a human or computer player so I moved that to a method 

                
                if(sameBoardCheck(incomingBoard, copy)){ // first we check if the board is the same board as the one we started with, when that is the case we can insatntly handle the dame turn with ease. 
                    board = incomingBoard;
                    gameTurn();
                } else if (incomingBoard.checkBoardValidity()) { // if it is not the exact same we check validity of the board 

                    if (getCurrentPlayer().getIsOut()) { // if the player is out and the new board is valid then this would be a valid turn 
                        sameBoardCheck(incomingBoard, copy); 
                        board = incomingBoard;
                        System.out.println("VALID BOARD");
                        gameTurn();
                    }
                    else {
                        int valueOfTurn = getValueOfTurn(incomingBoard); // if they arent out yet we need to determine two things, one of which is the numerical value
                        boolean gotOut = checkOut(incomingBoard); // changed the method so its outside the main game loop and we call it here so we know whther they follow the rules for going out
            
                        if (valueOfTurn >= 30 && gotOut) { // if the value of the turn is more than 30 than this is a valid move 
                            getCurrentPlayer().setIsOut(true); // we update the plyaer so that they havve player the thirty rule now
                            board = incomingBoard;
                            System.out.println("VALID BOARD");
                            gameTurn();
                        } else {
                                if(getCurrentPlayer() instanceof ComputerPlayer) {  // if the computer cant play more than 30 points than we give it back its originl deck plus the drawn tile 
                                    getCurrentPlayer().setDeckOfTiles(copy);
                                    getCurrentPlayer().getDeckOfTiles().add(currentDraw);
                                    getThisDrawnTile();
                                    gameTurn();
                                }
                                else { // for human player we just tell them they dont have 30 points 
                                    System.out.println("Get more then 30");
                                    StartScreensApplication.getInstance().setMessageLabel("1", "You need to get more then 30 points!");
                                }
                        } }
                    }
                   else {                        
                    getCurrentPlayer().setDeckOfTiles(copy);
                    StartScreensApplication.getInstance().setMessageLabel("1", "Not a valid board");
                    System.out.println("NOT VALID");
                }
            }   else {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                            
            }
            
        }
        
        System.out.println("GAME FINISHED"); 
       
    }
    
    
    /**
     * Checks if the incoming board is the same as the old one
     * @param incomingBoard New board
     * @param copy Copy of the current player's deck
     * @return true if the old and new boards are the same, false otherwise
     */
    private boolean sameBoardCheck(Board incomingBoard, ArrayList<Tile> copy){ 
        boolean same = false; 
        if (board.getTilesInBoard().size() == incomingBoard.getTilesInBoard().size()) {
                            getCurrentPlayer().setDeckOfTiles(copy);
                            getCurrentPlayer().getDeckOfTiles().add(currentDraw);
                            getThisDrawnTile();
                            same = true; 
                        }
        return same; 
        
    }

    /**
     * Checks if all of the points the player put on the board come from new sets
     * @param incomingBoard New board
     * @return true if the requirement is met, false otherwise
     */
    private boolean checkOut(Board incomingBoard){
        boolean gotOut = true; 
         for (Set set : board.getSetList()) {
                boolean contained=false;
                for (Set newSet : incomingBoard.getSetList()) { //compares every set of old board to every set of incomingBoard
                    if (set.equals(newSet)) { //two identical sets
                        contained = true;
                        break;
                    }
                }
                if(!contained){
                    gotOut = false;
                    //StartScreensApplication.getInstance().setMessageLabel(gameModel.playerNames.get(currentPlayerIndex), "You can't use the tiles on the board!");
                    System.out.println("You can't use the tiles in the board!");

                    break;
                }

             }
            return gotOut; 
    }

    /**
     * Calculates the total value of tiles a player put in his turn
     * @param incomingBoard New board
     * @return The value of tiles a player played in his turn
     */
    private int getValueOfTurn(Board incomingBoard) {
        int valueOfTurn = 0;
        for (Set set : incomingBoard.getSetList()) { //all sets on the new board
            boolean present = false;
            for (Set boardSet : board.getSetList()) { //all sets on the old board
                if (set.equals(boardSet)) { //if a set is present in both old and new board we dont include it in the counting
                    present = true;
                    break;
                }
            }
            if (!present) valueOfTurn += set.getValue(); //only adds the value of sets that were not present before
        }
        return valueOfTurn;
    }

    private Tile getThisDrawnTile() {
        Tile draw = drawTile();
        gameModel.setDrawable(draw);
        currentDraw = draw;
        return draw;
    }
    /**
     * Updates DecksLengths for the current player
     */
    private void setLengthOtherDecks(){
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

    /**
     * Gets the board after the player put some tiles on it
     * @return Board after changes
     */
    private Board getIncomingBoard(){
        Board incomingBoard; // first we need to get the incoming board which depens on whetehr we have a human or computer player 
                if(getCurrentPlayer() instanceof HumanPlayer) {
                    incomingBoard = createBoardFromTiles(transformImagesToTiles()); // if human we transform it from tiles 
                }
                else {
                    System.out.println("Computer is playing");
                    setLengthOtherDecks(); // Get length of other players decks:
                    incomingBoard = getCurrentPlayer().getNewBoard(board);
                }
        return incomingBoard; 
    }

    /**
    *Updates the game turn
    */
    private void gameTurn() {
        if (currentPlayerIndex == listOfPlayers.size() - 1) {
            currentPlayerIndex = 0; //switch back from the last to the first player
        } else {
            currentPlayerIndex++;
        }
        // curr = transformIntoBoard();
        GameModel.getInstance().curr = board.turnIntoImages();
        GameModel.getInstance().currBoard = board;
        //move to end maybe
        gameModel.setCurrentPlayer(getCurrentPlayer()); //update current player
        StartScreensApplication.activeController.playerTurn();
        if (getCurrentPlayer() instanceof HumanPlayer) {
            StartScreensApplication.getInstance().setMessageLabel(gameModel.playerNames.get(currentPlayerIndex), "");
        }
        else {
            StartScreensApplication.getInstance().setMessageLabel("Bot", "");
        }
        StartScreensApplication.activeController.updateBoard(board);
    }

/**
 * Transforms an ArrayList of ArrayLists of tiles into a board
 * @param map ArrayList of ArrayLists of tiles
 * @return Map represented as a board
 */
private Board createBoardFromTiles(ArrayList<ArrayList<Tile>> map) {
        Board newBoard = new Board();
        for (ArrayList<Tile> row : map) { //iterate through all the rows of map
            Set set = new Set();
            for (Tile tile : row) {
                if (tile != null) { //check if tile exists
                    set.addTile(tile); //if it does, it must form a set
                } else {
                    if (!set.isEmpty()) {
                        newBoard.addSet(set); //add set to board
                        set = new Set();
                    }
                }
            }
            if (!set.isEmpty()) newBoard.addSet(set);
        }
        return newBoard;
    }

    /**
     * Transforms a 2D ArrayList of Images into a 2D ArrayList of Tiles
     * based on the current state of the game model, player decks, and
     * a pool of tiles.
     *
     * @return A 2D ArrayList of Tiles representing the transformed board.
     */
    private ArrayList<ArrayList<Tile>> transformImagesToTiles() {
        ArrayList<ArrayList<Image>> potentialNewBoard = gameModel.getTransferBoardViaImages();
        ArrayList<ArrayList<Tile>> board2D = new ArrayList<>();
         // Retrieve the lists of tiles on the current board and player's deck
        ArrayList<Tile> listOfBoardTiles = board.getTilesInBoard();
        ArrayList<Tile> listOfPlayerTiles = listOfPlayers.get(currentPlayerIndex).getDeckOfTiles();
        for (ArrayList<Image> row : potentialNewBoard) { // Iterate through each row of images in the potential new board
            ArrayList<Tile> imageToTile = new ArrayList<>();
            for (Image image : row) {
                if (image != null) { // Check if the image is not null
                    boolean checker = false;
                    for (Tile placedTile : listOfBoardTiles) {  // Check if the image corresponds to a tile on the current board
                        if (placedTile.getImage().equals(image)) {
                            imageToTile.add(placedTile);
                            checker = true;
                            break;
                        }
                    }
                    // If the image does not correspond to a placed tile on the board,
                    // check if it corresponds to a tile in the player's deck
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
                    // If the image still does not correspond to a placed tile on the board,
                    // check if it corresponds to a tile in the pool of tiles
                    if (!checker) {
                        for (Tile placedTile : potOfTilesCopy) {
                            if (placedTile.getImage().equals(image)) {
                                imageToTile.add(placedTile);
                                break;
                            }
                        }
                    }

                } else {
                    imageToTile.add(null); // If the image is null, add a null tile to the current row
                }
            }
            board2D.add(imageToTile);
        }
        return board2D;
    }

    /**
     * Checks if the game is ending
     * @return true if one of the conditions for the game to end is true, false otherwise
     */
    private boolean isGameEnding() { 
        //condition 1: A player is out of tiles
        for (Player player : listOfPlayers) {
             if (player.getDeckOfTiles().isEmpty()) {
            StartScreensApplication.getInstance().setMessageLabel(player.getUsername(), player.getUsername() + " won the game!");
            return true;
        }
            
        }
       
        //condition 2: No more tiles to draw
        return potOfTiles.isEmpty();
    }


    /**
     * Draws a tile at random
     * @return Randomly selected tile
     */
    private Tile drawTile() {
        int index = (int) Math.floor(Math.random() * potOfTiles.size()); //Picking a random number between 0 and the size of the pot of tiles
        Tile a = potOfTiles.get(index); //Giving that tile to the player
        potOfTiles.remove(index); //Removing the tile from the pot of tiles
        return a;
    }

    /**
     * Generate all tiles before the start of the game
     */
    private void generateTiles() {

        //boolean isJoker = false;

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
        for (int i = 0; i < numberOfRealPlayers; i++) {
            listOfPlayers.add(new HumanPlayer("test"));
            for (int k = 0; k < 15; k++) {
                listOfPlayers.get(listOfPlayers.size() - 1).drawTile(drawTile());
            }

        }
        for (int i = 0; i < numberOfBots; i++) {
            listOfPlayers.add(new ComputerPlayer("test","baseline"));
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

    /**
     * Formats the current game state into a CSV-compatible string
     */
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

}
