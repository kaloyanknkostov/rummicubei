package com.gameEngine;

import java.util.ArrayList;

public class Set {

    private ArrayList<Tile> tilesList;
    private boolean group;
    private boolean run;
    private int value;
    private int number;

    public Set() {
        tilesList = new ArrayList<Tile>();
    }

    public void addTile(Tile tile) {
            tilesList.add(tile);
    }



    public boolean isRun() {
        if(tilesList.get(0).isJoker()){
            if(tilesList.get(1).getNumber()==1)
                return false;
            tilesList.get(0).setNumber(tilesList.get(1).getNumber()-1);
            tilesList.get(0).setColor(tilesList.get(1).getColor());
        }
        number= tilesList.get(0).getNumber();
        for(int i=1; i<tilesList.size(); i++) {

            if(tilesList.get(i).isJoker()){
                if(tilesList.get(i-1).getNumber()==13)return false;
                tilesList.get(i).setNumber(tilesList.get(i-1).getNumber()+1);
                tilesList.get(i).setColor(tilesList.get(i-1).getColor());
            }
            if(tilesList.get(i).getNumber() - 1 != tilesList.get(i-1).getNumber() ||  !tilesList.get(i).getColor().equals(tilesList.get(i-1).getColor())) {
                return false;
            }
        }
        run=true;
        group=false;
        return true;
    }

    public boolean isGroup() {

        if(tilesList.size()>4) return false;

        ArrayList<String> colorsUsed = new ArrayList<>();
        int numberUsed=0;
        for(Tile tile:tilesList) {
            if (!tile.isJoker()) {
                if (numberUsed == 0) numberUsed = tile.getNumber();
                if (tile.getNumber() != numberUsed) return false;
                String color = tile.getColor();
                for (String colorUsed : colorsUsed) {
                    if (colorUsed.equals(color)) return false;
                }
                colorsUsed.add(color);

            }
        }
        number=numberUsed;
            run = false;
            group = true;
            return true;

    }

    public Tile getTileAtIndex(int index) {
        return tilesList.get(index);
    }

    public boolean isValid() {
        if(tilesList.size()<3 || tilesList.size()>13) {
            return false;
        }
        else if( isGroup() )
        {
            value=number*tilesList.size();
           return true;
        }
        else if(isRun()){
          value=number*tilesList.size()+tilesList.size();
          return true;
        }
        else return false;
    }

    public ArrayList<Tile> getTilesList() {
        return this.tilesList;
    }

    @Override
    public String toString() {
        return "Set{" +
                "tilesList=" + tilesList +
                ", group=" + group +
                ", run=" + run +
                '}';
    }
    public boolean isEmpty()
    {
        return tilesList.isEmpty();
    }

    public int getValue() {
        return value;
    }
}
