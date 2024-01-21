package com.gameEngine;


import javafx.scene.image.Image;

public class Tile
{

    private int number;
    private String color;
    private boolean isJoker;
    private final String pictureName;

    public void setJoker(boolean joker) {
        isJoker = joker;
    }

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

    public String getPicture(){
        return pictureName;
    }

    @Override
    public String toString() {
        if(isJoker){
            return "Joker";
        }
        return color + "|" + number;
    }
    public Tile copy() {
        return new Tile(this.number, this.color, this.isJoker, this.pictureName);
    }
    public int turnToInt(){
        if(isJoker){
            return 53;
        }
        return switch (color) {
            case "red" -> number;
            case "blue" -> number + 13;
            case "black" -> number + 26;
            case "yellow" -> number + 39;
            default -> {
                System.out.println("PROBLEM");
                yield -1;
            }
        };
    }
}
