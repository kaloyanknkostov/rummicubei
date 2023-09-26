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
        return this.setList;
    }
}
