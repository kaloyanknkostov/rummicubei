package com.gameEngine;

import java.util.ArrayList;

public interface Player
{


    public void drawTile(Tile tile);
    public Board getNewBoard();
     String getUsername();
     Boolean getIsOut();
     ArrayList<Tile> getDeckOfTiles();
     void setUsername(String username);
    void setIsOut(Boolean isOut);
    void setDeckOfTiles(ArrayList<Tile> list);
}
