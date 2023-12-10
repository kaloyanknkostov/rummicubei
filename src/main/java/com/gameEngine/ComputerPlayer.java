package com.gameEngine;



import com.MCTS.ActionSpaceGenerator;
import com.MCTS.MCTS;

import java.util.ArrayList;

public class ComputerPlayer implements Player
{
    String username;
    Boolean isOut;
    private ArrayList<Tile> deckOfTiles;

    public ComputerPlayer(String username)
    {
        this.username = username;
        this.deckOfTiles = new ArrayList<>();
        isOut=false;
    }

    @Override
    public void drawTile(Tile tile)
    {
        deckOfTiles.add(tile);
    }

    @Override
    public Board getNewBoard(Board oldBoard)
    {
        /*
        ArrayList<ArrayList<Integer>> intBoard = oldBoard.turnToIntBoard();
        for(ArrayList<Integer> set:intBoard)
        {
           // board.addAll(set);
        }
        ArrayList<Integer>hand=new ArrayList<>();
        for ( Tile tile:deckOfTiles){
            hand.add(tile.turnToInt());
        }
        ActionSpaceGenerator actionSpaceGenerator = new ActionSpaceGenerator(board,hand);
        ArrayList<ArrayList<Integer>> currentBoard, ArrayList<Integer> currentRack, int lastCheckedSet)
        ArrayList<ArrayList<Integer>> emptyBoard = new ArrayList<>();
        actionSpaceGenerator.createAllMoves(emptyBoard,hand,0);
        return actionSpaceGenerator.getResultingBoards().get(0);
         */


        MCTS mcts =new MCTS(oldBoard,deckOfTiles);
        ArrayList<ArrayList<Integer>> newBoard =mcts.getNextMove();

        ArrayList<Tile> playableTiles =oldBoard.getTilesInBoard();
        playableTiles.addAll(deckOfTiles);
        Board board=new Board();
        for (ArrayList<Integer> set:newBoard){
            Set newSet = new Set();
            for(Integer tileId:set){
                for(Tile tile:playableTiles){
                    if(tile.turnToInt()==tileId){
                        newSet.addTile(tile);
                    }
                }
            }
            board.addSet(newSet);
        }
        return  board;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getIsOut() {
        return isOut;
    }

    public void setIsOut(Boolean out) {
        isOut = out;
    }

    @Override
    public ArrayList<Tile> getDeckOfTiles() {
        return deckOfTiles;
    }

    @Override
    public void setDeckOfTiles(ArrayList<Tile> deckOfTiles) {
        this.deckOfTiles = deckOfTiles;
    }
}
