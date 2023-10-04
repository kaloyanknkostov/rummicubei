package com.gameEngine;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Setter
@Getter
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
    public Board getNewBoard()
    {
        Board board=new Board();
        // AI PART
        return board;
    }
}
