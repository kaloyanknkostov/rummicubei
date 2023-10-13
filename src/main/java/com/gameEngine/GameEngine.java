package com.gameEngine;
import com.example.GUI.GameModel;
import com.example.GUI.StartScreensApplication;

import java.util.ArrayList;

public class GameEngine {
    Board board = new Board();
    private static int numberOfRealPlayers;
    private static int numberOfBots;
    static boolean endGame;
    private static final GameModel gameModel = GameModel.getInstance();
    static int currentPlayerIndex=0;
    private static ArrayList<Tile> potOfTiles = new ArrayList<Tile>();
    public static ArrayList<Player> listOfPlayers = new ArrayList<Player>();
    private static StartScreensApplication startScreensApplication = StartScreensApplication.getInstance();

    public static void gameLoop(){
        //startScreensApplication.main(args);
       
        // Starts the game loop which runs until a game ending event (quit button, or win, etc.)
        while (!endGame){
        gameTurn();//maybe we should create 2 functions, one which sets everything up and sends board, and one whcih can recieve a new board from gui and check for specific conditions
            try {
                Thread.sleep(5000);  // waits for 100 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isGameEnding();}
    }

//    public GameEngine(int numberOfRealPlayers, int numberOfBots){
//        this.numberOfRealPlayers=numberOfRealPlayers;
//        this.numberOfBots =numberOfBots;
//        endGame=false;
//        generateTiles();
//        addPlayers();
//    }
    public static void main(String[] args) {

        Thread guiThread = new Thread(() -> {
            StartScreensApplication.launch(StartScreensApplication.class);
        });
        guiThread.start();
        while (!gameModel.isStartGame()) {
            System.out.print(gameModel.isStartGame());
            try {
                Thread.sleep(1000);  // waits for 100 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        numberOfRealPlayers= gameModel.getNumberOfPlayers();
        numberOfBots =0;
        endGame=false;
        generateTiles();
        addPlayers();
        gameLoop();
    }

    private static void gameTurn(){
        gameModel.setCurrentPlayer(getCurrentPlayer());
        System.out.println(gameModel.getCurrentPlayer().getDeckOfTiles().toString());
        StartScreensApplication.activeController.playerTurn();
        System.out.print("The game turn is running");
        if(currentPlayerIndex==listOfPlayers.size()-1){
            currentPlayerIndex=0;
        } else{
            currentPlayerIndex++;
        }
    }
    private static boolean isGameEnding(){ // check game ending conditions
        if(listOfPlayers.get(currentPlayerIndex).getDeckOfTiles().size()==0){
            return true;
        }

        if(potOfTiles.size()==0){
            return true;
        }
        return false;
    }

    private boolean isTurnValid(){
        return false;
    }
    private static Tile drawTile(){
        int index= (int)Math.random()*potOfTiles.size();
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
    }
    public static Player getCurrentPlayer(){
        return listOfPlayers.get(currentPlayerIndex);
    }

    /**
     * Add players to a list for looping in the game loop
     * @TODO add usernames thah come from the gui if needed
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
