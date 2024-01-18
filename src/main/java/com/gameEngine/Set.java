package com.gameEngine;

import java.util.ArrayList;
import java.util.Objects;

public class Set {

    private ArrayList<Tile> tilesList;
    private boolean group;
    private boolean run;

    public Set() {
        tilesList = new ArrayList<Tile>();
    }

    public void addTile(Tile tile) {
            tilesList.add(tile);
    }


    public int getSize(){
        return tilesList.size();
    }
    public boolean isRun() {
        if(tilesList.get(0).isJoker()){
            if(tilesList.get(1).getNumber()==1)
                return false;
            tilesList.get(0).setNumber(tilesList.get(1).getNumber()-1);
            tilesList.get(0).setColor(tilesList.get(1).getColor());
        }
        for(int i=1; i<tilesList.size(); i++) {

            if(tilesList.get(i).isJoker()){
                if(tilesList.get(i-1).getNumber()==13)return false;
                tilesList.get(i).setNumber(tilesList.get(i-1).getNumber()+1);
                tilesList.get(i).setColor(tilesList.get(i-1).getColor());
                //tilesList.get(i).setJoker(false);WHY WOULD WE DO THIS??? WE LOOSE THE JOKER PROPERTY
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
            return true;
        }
        else return isRun();
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
        int totalValue = 0;
        for (Tile tile : tilesList) {
            totalValue += tile.getNumber();
        }
        return totalValue;
    }

    public Set copy() {
        Set newSet = new Set();
        for (Tile tile : this.tilesList) {
            newSet.addTile(tile.copy());
        }
        return newSet;
    }

    public int getSizes(){
        return tilesList.size();
    }
    public ArrayList<Integer> turnSetToInt(){
        ArrayList<Integer> list=new ArrayList<>();
        for(Tile tile:tilesList){
           list.add(tile.turnToInt());
        }
        return list;
    }

    @Override
    public boolean equals(Object o) {
        boolean equals = true;
        if(!(o instanceof Set))return false;
        if(!(tilesList.size()==((Set) o).getSizes()))return false;
        else {
           //run
            if (isRun()) {
                for (int i = 0; i < tilesList.size() ; i++) {
                    if (!(tilesList.get(i).turnToInt() == ((Set) o).getTilesList().get(i).turnToInt())) return false;
                }
            }
            //group
            else{
                for (Tile firstTile : tilesList) {
                    boolean present =false;
                    for (Tile tile : ((Set) o).getTilesList()) {

                        if (firstTile.turnToInt() == tile.turnToInt()) present = true;
                    }
                    if(!present) return present;
                }

            }
        }
        return equals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tilesList, group, run);
    }
}
