package com.gameEngine;


import java.util.ArrayList;
public class Board {

    private ArrayList<Set> setList;

    public Board() {
        setList = new ArrayList<>();
    }

    private boolean checkBoardValidity() {
        for(int i=0; i<setList.size(); i++) {
            if(!setList.get(i).isValid()) return false;
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
}
