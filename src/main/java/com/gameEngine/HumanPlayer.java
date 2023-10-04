package com.gameEngine;



import java.util.ArrayList;

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
