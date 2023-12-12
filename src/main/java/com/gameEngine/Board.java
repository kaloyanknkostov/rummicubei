package com.gameEngine;


import java.util.ArrayList;
public class Board {

    private ArrayList<Set> setList;

    public Board() {
        setList = new ArrayList<>();
    }

    public boolean checkBoardValidity() {
        for (Set set : setList) {
            if (!set.isValid()) return false;
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
}
