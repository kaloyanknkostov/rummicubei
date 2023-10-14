package com.gameEngine;


import javafx.scene.image.Image;

public class Tile
{

    private int number;
    private String color;
    private boolean isJoker;
    private String pictureName;
    private Image image;
    public Tile(int number, String color, boolean isJoker, String pictureName)
    {
        this.number = number;
        this.color = color;
        this.isJoker = isJoker;
        this.pictureName=pictureName;
        this.image=new Image(pictureName);
    }

    public Image getImage() {
        return image;
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

    @Override
    public String toString() {
        return "Tile{" +
                "number=" + number +
                ", color='" + color + '\'' +
                ", isJoker=" + isJoker +
                ", pictureName='" + pictureName + '\'' +
                ", image=" + image +
                '}';
    }
}
