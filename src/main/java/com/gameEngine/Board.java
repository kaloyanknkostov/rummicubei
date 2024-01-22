package com.gameEngine;


import javafx.scene.image.Image;

import java.util.ArrayList;
public class Board {

    private ArrayList<Set> setList;

    public Board() {
        setList = new ArrayList<>();
    }

    public boolean checkBoardValidity() {
        for (Set set : setList) {
            if (!set.isValid()) {
                return false;}
        }
        return true;
    }

    public ArrayList<Set> getSetList() {
        return setList;
    }
    public ArrayList<Tile> getTilesInBoard() {
        ArrayList<Tile> returnable=new ArrayList<>();
        for(Set set:setList) {
            returnable.addAll(set.getTilesList());
        }
        return returnable;
    }
    public void addSet(Set set) {
        setList.add(set);
    }

    public void printBoard() {
        System.out.println("----- BOARD -----");
        for (int i = 0; i < setList.size(); i++) {
            System.out.println("Set " + (i + 1) + ": ");
            for (Tile tile : setList.get(i).getTilesList()) {
                System.out.println(tile);
            }
            System.out.println("-----------------");
        }
    }
    public Board copy() {
        Board newBoard = new Board();
        for (Set set : this.setList) {
            newBoard.addSet(set.copy());
        }
        return newBoard;
    }
    public ArrayList<ArrayList<Integer>> turnToIntBoard()
    {
        ArrayList<ArrayList<Integer>> board=new ArrayList<>();
        for (Set set:setList){
            ArrayList<Integer> intSet=set.turnSetToInt();
            board.add(intSet);
        }
        return board;
    }

    public ArrayList<ArrayList<Image>> turnIntoImages(){
        ArrayList<ArrayList<Image>> board=new ArrayList<>();
        for (Set set : setList){
            ArrayList<Image> ImageSet=new ArrayList<>();
            ImageSet.add(null);
            board.add(ImageSet);
        }
        return board;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < setList.size(); i++) {
            for (Tile tile : setList.get(i).getTilesList()) {
                sb.append(tile.turnToInt());
                sb.append(" ");
            }
            sb.append(";"); // ; between the sets
        }
        if(sb.length() != 0){
            sb.deleteCharAt(sb.length() -1);
        }
        return sb.toString();
    }

}
