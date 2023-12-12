package com.gameEngine;



import com.MCTS.ActionSpaceGenerator;
import com.MCTS.BaselineAgent;
import com.MCTS.MCTS;

import java.util.ArrayList;

public class ComputerPlayer implements Player
{
    String username;
    Boolean isOut;
    private ArrayList<Tile> deckOfTiles;
    private ArrayList<Integer> deckLengths;
    private String type;

    public ComputerPlayer(String username, String type)
    {
        this.type = type;
        this.username = username;
        this.deckOfTiles = new ArrayList<>();
        isOut=false;
    }

    @Override
    public void drawTile(Tile tile)
    {
        deckOfTiles.add(tile);
    }

    public void setDeckLengths(ArrayList<Integer> deckLengths){
        this.deckLengths = deckLengths;
    }

    @Override
    public Board getNewBoard(Board oldBoard)
    {

        ArrayList<Integer> deckOfIntTiles =new ArrayList<>();
        for (Tile tile: deckOfTiles){
            deckOfIntTiles.add(tile.turnToInt());
        }
        System.out.println("Gave the bot this deck: "+deckOfIntTiles);
        ArrayList<ArrayList<Integer>> newBoard = null;
        if (type == "baseline"){
            BaselineAgent baselineAgent =new BaselineAgent(oldBoard.turnToIntBoard(),deckOfIntTiles);
            newBoard =baselineAgent.getBestMove();
        } else if (type == "mcts"){
            MCTS mctsAgent = new MCTS(oldBoard.turnToIntBoard(), deckOfIntTiles, deckLengths.get(0));
            mctsAgent.loopMCTS(5);
            newBoard = mctsAgent.getRoot().selectNode().getGameState().getBoard();
        }

        System.out.println("NEW BOARD WITH INTS: "+newBoard);
        ArrayList<Tile> oldBoardTilesInBoard =oldBoard.getTilesInBoard();
        Board board=new Board();
        for (ArrayList<Integer> sets:newBoard){
            Set newSet = new Set();
            for(Integer tileId:sets){
                System.out.println("Tile id being checked "+tileId);
                //check if taken from board
                boolean found=false;
                ArrayList<Tile> removed =new ArrayList<>();
                for(Tile tile:oldBoardTilesInBoard){
                    if(tile.turnToInt()==tileId){
                        newSet.addTile(tile);
                        found =true;
                        System.out.println("Matched with tile from board "+tile.turnToInt());
                        removed.add(tile);
                        break;
                    }
                }
                for(Tile tile:removed){
                    oldBoardTilesInBoard.remove(tile);
                }
                if(found) {
                    System.out.println("Skipped");
                    continue;
                }
                //check if taken from player tile
                removed =new ArrayList<>();
                for(Tile tile:deckOfTiles){
                    if(tile.turnToInt()==tileId){
                        newSet.addTile(tile);
                        System.out.println("Matched with tile from player "+tile.turnToInt());
                        removed.add(tile);
                        found=true;
                        break;
                    }
                }
                for(Tile tile:removed){
                    deckOfTiles.remove(tile);
                }
                if (!found){
                    System.out.println("bot draw card");
                    return oldBoard;
                }


            }
            board.addSet(newSet);
        }
        board.printBoard();


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
