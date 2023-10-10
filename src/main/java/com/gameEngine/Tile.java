package com.gameEngine;


public class Tile
{

    private int number;
    private String color;
    private boolean isJoker;
    private String pictureName;
    public Tile(int number, String color, boolean isJoker, String pictureName)
    {
        this.number = number;
        this.color = color;
        this.isJoker = isJoker;
        this.pictureName=pictureName;
    }

    public int getNumber() {
        return number;
    }

    public String getColor() {
        return color;
    }

    public boolean isJoker() {
        return isJoker;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setJoker(boolean joker) {
        isJoker = joker;
    }
    public String getPicture(){
        return pictureName;
    }
}
