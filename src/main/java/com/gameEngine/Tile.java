package com.gameEngine;

import lombok.Getter;

@Getter
public class Tile
{

    private int number;
    private String color;
    private boolean isJoker;

    public Tile(int number, String color, boolean isJoker)
    {
        this.number = number;
        this.color = color;
        this.isJoker = isJoker;
    }


}
