package com.gameEngine;
import com.example.GUI.StartScreensApplication;

import java.util.ArrayList;
public class GameEngine {
    Board board = new Board();

    int numberOfRealPlayers;
    int numberOfBots;
    boolean endGame;
    int currentPlayerIndex=0;
    private ArrayList<Tile> potOfTiles = new ArrayList<Tile>();
    private ArrayList<Player> listOfPlayers = new ArrayList<Player>();
    StartScreensApplication startScreensApplication = new StartScreensApplication();
    public GameEngine(int numberOfRealPlayers, int numberOfBots){
        this.numberOfRealPlayers=numberOfRealPlayers;
        this.numberOfBots =numberOfBots;
        endGame=false;
    }

    public void gameLoop(){
        //startScreensApplication.main(args);
        generateTiles();
        addPlayers();
        // Starts the game loop which runs until a game ending event (quit button, or win, etc.)
        while (!endGame){
        gameTurn();//maybe we should create 2 functions, one which sets everything up and sends board, and one whcih can recieve a new board from gui and check for specific conditions
        isGameEnding();}
    }

    private void gameTurn(){
        if(currentPlayerIndex==listOfPlayers.size()-1){
            currentPlayerIndex=0;
        } else{
            currentPlayerIndex++;
        }

        // Simulate the turn of one player (either bot or normal)
        // waiting for the new board that we get from the gui#
        // Check if it is valid -> if not wait for new board
        // if valid then end turn
    }
    private boolean isGameEnding(){ // check game ending conditions
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
    private Tile drawTile(){
        int index= (int)Math.random()*potOfTiles.size();
        Tile a=potOfTiles.get(index);
        potOfTiles.remove(index);
        return a;
    }
    

    private void generateTiles(){
        boolean isJoker= true;
        potOfTiles.add(new Tile(0,"",isJoker,"src\\main\\resources\\Tiles\\painted_tile_1.png"));
        potOfTiles.add(new Tile(0,"",isJoker,"src\\main\\resources\\Tiles\\painted_tile_3.png"));
        isJoker = false;
        for(int i=1;i<14;i++){
         potOfTiles.add(new Tile(i,"red",isJoker,"src\\main\\resources\\Tiles\\painted_tile_red_"+i+".png"));
        }
        for(int i=1;i<14;i++){
         potOfTiles.add(new Tile(i,"blue",isJoker,"src\\main\\resources\\Tiles\\painted_tile_blue_"+i+".png"));
        }for(int i=1;i<14;i++){
         potOfTiles.add(new Tile(i,"black",isJoker,"src\\main\\resources\\Tiles\\painted_tile__black_"+i+".png"));
        }
        for(int i=1;i<14;i++){
         potOfTiles.add(new Tile(i,"yellow",isJoker,"src\\main\\resources\\Tiles\\painted_tile_yellow_"+i+".png"));
        }
    }

    /**
     * Add players to a list for looping in the game loop
     * @TODO add usernames thah come from the gui if needed
     */
    public  void addPlayers()
    {
        for (int i = 0; i <numberOfRealPlayers ; i++)
        {
            listOfPlayers.add(new HumanPlayer("test"));            
            System.out.println("Added one");
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
