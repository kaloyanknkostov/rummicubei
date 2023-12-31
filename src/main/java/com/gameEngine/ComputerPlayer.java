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
            BaselineAgent baselineAgent;
            if(isOut){
                baselineAgent =new BaselineAgent(oldBoard.turnToIntBoard(),deckOfIntTiles);
            }
            else {

                baselineAgent =new BaselineAgent(new ArrayList<ArrayList<Integer>>(),deckOfIntTiles);
            }
            newBoard =baselineAgent.getBestMove();
        }
        else if (type == "mcts"){
            MCTS mctsAgent = new MCTS(oldBoard.turnToIntBoard(), deckOfIntTiles, deckLengths.get(0));
            mctsAgent.loopMCTS(5);

            double highestUCT = Double.NEGATIVE_INFINITY;
            com.MCTS.Node nextNode = null;
            System.err.println(mctsAgent.getRoot().getChildList());
            for (com.MCTS.Node child: mctsAgent.getRoot().getChildList()){
                if(child.getUCT()>highestUCT){
                    highestUCT = child.getUCT();
                    nextNode = child;
                }
            }
            newBoard = nextNode.getGameState().getBoard();
        }

        System.out.println("NEW BOARD WITH INTS: "+newBoard);
        ArrayList<Tile> oldBoardTilesInBoard =oldBoard.getTilesInBoard();
        Board board=new Board();
        if(newBoard == null)
            return oldBoard;
        for (ArrayList<Integer> sets:newBoard){
            Set newSet = new Set();
            for(Integer tileId:sets){
                boolean found=false;
                ArrayList<Tile> removed =new ArrayList<>();
                for(Tile tile:oldBoardTilesInBoard){
                    if(tile.turnToInt()==tileId){
                        newSet.addTile(tile);
                        found =true;
                        removed.add(tile);
                        break;
                    }
                }
                for(Tile tile:removed){
                    oldBoardTilesInBoard.remove(tile);
                }
                if(found) {
                    continue;
                }
                //check if taken from player tile
                removed =new ArrayList<>();
                for(Tile tile:deckOfTiles){
                    if(tile.turnToInt()==tileId){
                        newSet.addTile(tile);
                        removed.add(tile);
                        found=true;
                        break;
                    }
                }
                for(Tile tile:removed){
                    deckOfTiles.remove(tile);
                }
                if (!found){
                    return oldBoard;
                }


            }
            board.addSet(newSet);
        }
        board.printBoard();
        // might break for mcts
        if(!isOut){
            for(Set set:oldBoard.getSetList()){
                board.addSet(set);
            }
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
