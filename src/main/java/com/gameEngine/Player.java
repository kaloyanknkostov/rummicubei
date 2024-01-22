package com.gameEngine;

import java.util.ArrayList;

public interface Player
{


    public void drawTile(Tile tile);
    public Board getNewBoard(Board oldBoard);
     String getUsername();
     Boolean getIsOut();
     ArrayList<Tile> getDeckOfTiles();
     void setUsername(String username);
    void setIsOut(Boolean isOut);
    void setDeckOfTiles(ArrayList<Tile> list);
    public void setDeckLengths(ArrayList<Integer> deckLengths);
}
