package com.gameEngine;


import java.util.ArrayList;
import java.util.stream.Collectors;

public class Board {

    private ArrayList<Set> setList;

    public Board() {
        setList = new ArrayList<>();
    }

    public boolean checkBoardValidity() {
        for(int i=0; i<setList.size(); i++) {
            if(!setList.get(i).isValid()) return false;
        }
        return true;
    }

    public void setSetList(ArrayList<Set> setList) {
        this.setList = setList;
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
    @Override
    public String toString() {
        if (setList.isEmpty()) {
            return "Empty Board";
        }

        return setList.stream()
                .map(set -> set.getTilesList().stream()
                        .map(Tile::toString)
                        .collect(Collectors.joining(" ")))
                .collect(Collectors.joining(";"));
    }


}
