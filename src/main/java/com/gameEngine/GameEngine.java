package com.gameEngine;
import com.example.GUI.GameModel;
import com.example.GUI.StartScreensApplication;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class GameEngine {
    private static Board board;
    private static int numberOfRealPlayers;
    private static int numberOfBots;
    static boolean endGame;
    private static final GameModel gameModel = GameModel.getInstance();
    static int currentPlayerIndex=0;
    private static ArrayList<Tile> potOfTiles = new ArrayList<Tile>();
    private static ArrayList<Tile> potOfTilesCopy=new ArrayList<>();
    private static ArrayList<Player> listOfPlayers = new ArrayList<Player>();
    private final static java.util.logging.Logger logger =  java.util.logging.Logger.getLogger(GameEngine.class.getName());


    public static void main(String[] args) {

        Thread guiThread = new Thread(() -> {
            StartScreensApplication.launch(StartScreensApplication.class);
        });
        guiThread.start();
        while (!gameModel.isStartGame()) {
            try {
                Thread.sleep(1000);  // waits for 100 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        numberOfRealPlayers= gameModel.getNumberOfPlayers();
        numberOfBots =0;
        board = new Board();
        endGame = false;
        generateTiles();
        gameLoop();

    }

    public static void gameLoop(){
        addPlayers();
        // Starts the game loop which runs until a game ending event (quit button, or win, etc.)
        gameTurn();
        while (!isGameEnding()){
            if (gameModel.isNextTurn()){
                gameModel.setNextTurn(false);
               gameTurn();
                boolean isNewBoardValid=isNewBoardValid();
                if(isNewBoardValid)
                    System.out.println("VALID BOARD");
                else
                    System.out.println("NOT VALID");

                // if (check the model.getBoardToCheck() is valid) then next turn, else
                // set model.getBoardToCheck() to model.getCurrentBoard() and restart player tiles

            }else{
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("GAME FINISHED");
    }
    private static boolean isNewBoardValid(){
        ArrayList<ArrayList<Image>> potentialNewBoard = gameModel.getTransferBoardViaImages();
        ArrayList<Tile> listOfBoardTiles=board.getTilesInBoard();
        Board newBoard=new Board();
        boolean isBoardValid=true;
        for(ArrayList<Image> row:potentialNewBoard)
        {
           // System.out.println("");
            Set set=new Set();
            for(Image image:row)
            {

                if(image!=null)
                {
                    boolean checker=false;
                    for(Tile placedTile:listOfBoardTiles) {
                        if (placedTile.getImage().equals(image)){
                           // System.out.print(placedTile.getNumber());
                            set.addTile(placedTile);
                            checker=true;
                            break;
                        }
                    }
                    if(!checker) {
                        for (Tile playerTile : listOfPlayers.get(currentPlayerIndex).getDeckOfTiles()) {
                            if (playerTile.getImage().equals(image)) {
                                listOfPlayers.get(currentPlayerIndex).getDeckOfTiles().remove(playerTile);
                               // logger.info(playerTile.toString());
                              //  System.out.print(playerTile.getColor());
                                set.addTile(playerTile);
                                checker=true;
                                break;
                            }
                        }
                    }
                    if(!checker){
                        for(Tile tile:potOfTilesCopy){
                            if(tile.getImage().equals(image))
                                System.out.println(tile.toString());
                        }
                        logger.severe("PROBLEM WITH TILES");
                    }

                }
                else {
                   // System.out.print("0");
                    if(set.isValid()) {
                        newBoard.addSet(set);
                        set=new Set();
                    } else if (set.isEmpty()) {

                    } else{
                        isBoardValid=false;
                        set=new Set();
                    }

                }
            }
        }
        if(isBoardValid&&newBoard.checkBoardValidity())
        {
            if(board.getTilesInBoard().size() == newBoard.getTilesInBoard().size())listOfPlayers.get(currentPlayerIndex).getDeckOfTiles().add(drawTile());
            board=newBoard;
        }
        return isBoardValid;
    }
    private static void gameTurn(){
        gameModel.setCurrentPlayer(getCurrentPlayer());
        StartScreensApplication.activeController.playerTurn();
        gameModel.setCurrentBoard(board);
        if(currentPlayerIndex==listOfPlayers.size()-1){
            currentPlayerIndex=0;
        } else{
            currentPlayerIndex++;
        }
    }
    private static boolean isGameEnding(){ // check game ending conditions
        if(listOfPlayers.get(currentPlayerIndex).getDeckOfTiles().isEmpty()){
            return true;
        }

        if(potOfTiles.size()==0){
            return true;
        }
        return false;
    }


    private static Tile drawTile(){
        int index= (int)Math.floor(Math.random()*potOfTiles.size());
        //index=0;
        Tile a=potOfTiles.get(index);
        potOfTiles.remove(index);
        return a;
    }
    

    private static void generateTiles(){
        boolean isJoker= true;
        potOfTiles.add(new Tile(0,"",isJoker,"painted_tile_1.png"));
        potOfTiles.add(new Tile(0,"",isJoker,"painted_tile_3.png"));
        isJoker = false;
        for(int i=1;i<14;i++){
         potOfTiles.add(new Tile(i,"red",isJoker,"painted_tile_red_"+i+".png"));

        }
        for(int i=1;i<14;i++){
         potOfTiles.add(new Tile(i,"blue",isJoker,"painted_tile_blue_"+i+".png"));
        }for(int i=1;i<14;i++){
         potOfTiles.add(new Tile(i,"black",isJoker,"painted_tile_black_"+i+".png"));
        }
        for(int i=1;i<14;i++){
         potOfTiles.add(new Tile(i,"yellow",isJoker,"painted_tile_yellow_"+i+".png"));
        }
        for (int i = 0; i <potOfTiles.size() ; i++) {
            potOfTilesCopy.add(potOfTiles.get(i));
        }

    }
    public static Player getCurrentPlayer(){
        return listOfPlayers.get(currentPlayerIndex);
    }

    /**
     * Add players to a list for looping in the game loop
     * @TODO add usernames that come from the gui if needed
     */
    public static void addPlayers()
    {
        for (int i = 0; i <numberOfRealPlayers ; i++)
        {
            listOfPlayers.add(new HumanPlayer("test"));            
            for(int k=0;k<15;k++){
                listOfPlayers.get(listOfPlayers.size()-1).drawTile(drawTile());
            }
            
        }
        for (int i = 0; i <numberOfBots ; i++)
        { 
            listOfPlayers.add(new ComputerPlayer("test"));
            System.out.println("Added one");
           
              for(int k=0;k<15;k++){
                listOfPlayers.get(listOfPlayers.size()-1).drawTile(drawTile());
            }
        }
    }





}
