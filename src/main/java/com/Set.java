import java.util.ArrayList;

public class Set {

    private ArrayList<Tile> tilesList;
    private boolean group;
    private boolean run;

    public Set() {
        tilesList = new ArrayList<Tile>();
    }

    public void addTile(Tile tile) {

        if(tile.getIsJoker()==true) {
            tilesList.add(tile);
        } else if(tilesList.isEmpty()) {
            tilesList.add(tile);
        } else if(tilesList.size()==1) {
            if(tile.getNumber()==tilesList.get(0).getNumber() && !tile.getColor().equals(tilesList.get(0).getColor())) {
                group = true;
                tilesList.add(tile);
            } else if(Math.abs(tile.getNumber()-tilesList.get(0).getNumber())==1) {
                run = true;
                 if(tile.getNumber()>tilesList.get(0).getNumber()) {
                    tilesList.add(tile);
                } else {
                    tilesList.add(0, tile);
                }
            }
        } else if(canAdd(tile)) {
            if(group) {
                tilesList.add(tile);
            } else {
                if(tile.getNumber()==tilesList.get(0).getNumber()-1) {
                    tilesList.add(0, tile);
                } else {
                    tilesList.add(tile);
                }
            }
        }

    }

    public boolean canAdd(Tile tile) {
        int num = tile.getNumber();
        String col = tile.getColor();

        if(group) {
            for(int i=0; i<tilesList.size(); i++) {
                if(num!=tilesList.get(i).getNumber()||col.equals(tilesList.get(i).getColor())) {
                    return false;
                }
            }
            return true;

        } else if(run) {
            if(num!=tilesList.get(0).getNumber()-1 && num!=tilesList.get(tilesList.size()-1).getNumber()+1) {
                return false;
            } else if(!col.equals(tilesList.get(0).getColor())) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public boolean isRun() { 
        for(int i=1; i<tilesList.size(); i++) {
            if(tilesList.get(i).getNumber() - 1 != tilesList.get(i-1).getNumber() && !tilesList.get(i).getColor().equals(tilesList.get(i-1).getColor())) {
                return false;
            }
        }
        run=true;
        return true;
    }

    public boolean isGroup() {

        if(tilesList.size()>4) return false;

        ArrayList<String> colorsUsed = new ArrayList<>();
        if(!colorsUsed.isEmpty()) {  
            colorsUsed.add(tilesList.get(0).getColor());
        }
        for(int i=1; i<tilesList.size(); i++){
            if(tilesList.get(i).getNumber()!=tilesList.get(i-1).getNumber() || colorsUsed.contains(tilesList.get(i).getColor())) {
                return false;
            }
            colorsUsed.add(tilesList.get(i).getColor());
        }

        group=true;
        return true;
    }

    public Tile getTileAtIndex(int index) {
        return tilesList.get(index);
    }

    public boolean isValid() {
        if(tilesList.size()<3 || tilesList.size()>13) {
            return false;
        } else if(!isGroup() && !isRun()) return false;
        
        return true;
    }

    public ArrayList<Tile> getTilesList() {
        return this.tilesList;
    }
}
