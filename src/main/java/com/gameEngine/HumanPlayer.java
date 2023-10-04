package com.gameEngine;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Setter
@Getter
public class HumanPlayer implements Player
{
    private String username;
    private Boolean isOut;
    private ArrayList<Tile> deckOfTiles;

    public HumanPlayer(String username)
    {
        this.username = username;
        deckOfTiles=new ArrayList<>();
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
        // get Board from GUI
        Board board=new Board();
        return board;
    }
    //TODO fix the set methods
    @Override
    public String setUsername() {
        return null;
    }

    @Override
    public Boolean setIsOut() {
        return null;
    }

    @Override
    public ArrayList<Tile> setDeckOfTiles() {
        return null;
    }
}
