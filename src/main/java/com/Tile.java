package com;
public class Tile {

    private int number;
    private String color;
    private boolean isJoker; 

    public Tile(int number, String color, boolean isJoker) {
        this.number = number;
        this.color = color;
        this.isJoker = isJoker;
    }

    public int getNumber() {
        return this.number;
    }

    public String getColor() {
        return this.color;
    }   

    public boolean getIsJoker() {
        return this.isJoker;
    }

    public void printTile() {
        System.out.println(this.getColor() + " " + this.getNumber());
    }

}
