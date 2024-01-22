package com.MCTS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.lang.reflect.Array;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MCTS {
    private GameState gameState;
    private Node root;
    private ArrayList<ArrayList<Integer>> board;
    private ArrayList<Integer> deck;
    private ArrayList<Integer> guessedOppononetDeck;
    private String time;
    private ArrayList<Integer> guessedPile;
    private boolean melted;



    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> board = new ArrayList<>();
        board.add(new ArrayList<>(Arrays.asList(1,2, 3)));
        board.add(new ArrayList<>(Arrays.asList(5,6, 7)));
        ArrayList<Integer> deck =  new ArrayList<>(Arrays.asList(10, 11, 12, 13));

    }


    public MCTS(ArrayList<ArrayList<Integer>> board, ArrayList<Integer> deck, int numberTilesOpponent, boolean melted){
        // get game state
        this.board = board;
        this.deck = deck;
        this.guessedOppononetDeck = new ArrayList<>();
        this.guessedPile = new ArrayList<>();
        this.melted = melted;
        // Get predictions of other players decks
        // We can decide here if we want to create multiple trees by sampling the tiles based on the predictions/ probabilities we got (advanced stuff)
        guessPlayer2DeckAndPile(numberTilesOpponent);
        this.gameState = new GameState(this.deck, this.guessedOppononetDeck, this.board ,this.guessedPile);
        this.root = new Node(this.gameState, null, 0, false, this.melted, this.root);// is this legal?
    }

    public void loopMCTS(int loops){
        // Should loop n* player count times
        for (int i = 0; i < loops*2; i++){//TODO only works for 2 players
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            time = LocalTime.now().format(formatter);
            System.err.println(time + " || Loop: " + i + " || SELECTION");
            Node selected_node = this.root.selectNode();

            time = LocalTime.now().format(formatter);
            System.err.println(time + " || Loop: " + i + " || CHECKING FOR EARLY STOP");
            Node bestChild = this.root.getBestChild(true);
            if(bestChild != null && bestChild.getLeaf()){
                System.err.println("Winning move detected - SEARCH STOPPED!");
                System.err.println("Best next board: " + bestChild.getGameState().getBoard().toString());
                break;
            }

            time = LocalTime.now().format(formatter);
            System.err.println(time + " || Loop: " + i + " || EXPANSION");
            selected_node.expand();
            if(i==0 && selected_node.getChildList().size() == 1){
                // Only one move possible can only be at the start
                System.err.println("NEXT MOVE DETERMINED - only one is possible");
                System.err.println(selected_node.getChildList().get(0).getGameState().getBoard());
                break;
            }
            // Get a child from the selected node to start Play-Out (first child node)
            time = LocalTime.now().format(formatter);
            System.err.println(time + " || Loop: " + i + " || SELECTION FOR PLAYOUT");
            selected_node = selected_node.selectNode();

            time = LocalTime.now().format(formatter);
            System.err.println(time + " || Loop: " + i + " || PLAYOUT");
            selected_node.playOut();
        }
        // Debugging prints to determine the outcome of MCTS
        System.err.println("LOOP OVER");
        System.err.println("Child nodes of root: "+ this.root.getChildList().size());
        for(Node child: this.root.getChildList()){
            System.err.println(child.getGameState().getBoard()+"  "+child.getUCT());
        }
        System.err.println("Next move: "+ this.root.getBestChild(true).getGameState().getBoard());
    }
    private void guess2NDPlayerML(int opponetDeckSize) {
        String rack = "--rack=";
        rack+=setToString(this.deck);
        //ArrayList<ArrayList<Integer>> board
        StringBuilder stringBoard = new StringBuilder("--board1=[");
        for (ArrayList<Integer> set : this.board) {
            //System.out.println(setToString(set));
            stringBoard.append(setToString(set)).append(", ");
        }
        stringBoard.setLength(Math.max(stringBoard.length() - 2, 0));
        stringBoard.append("]");
        try {
            //
            ProcessBuilder processBuilder = new ProcessBuilder("python" ,"machine_learning/run_model.py",rack,stringBoard.toString()).redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read current output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String array = reader.readLine();
            array = array.replace("[", "").replace("]", "");
            array =array.replaceAll("\\s+", "");
            ArrayList<String> list = new ArrayList<String>(Arrays.asList(array.split(",")));

            ArrayList<Double> tileProbability = new ArrayList<>();
            for (String s : list) {
                tileProbability.add(Double.parseDouble(s));
            }


            HashMap<Integer,Double> map =new HashMap<>();
            for (int i = 0; i <tileProbability.size(); i++) {
                map.put(i, tileProbability.get(i));
            }


            List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(map.entrySet());
            Collections.sort(entryList, new Comparator<Map.Entry<Integer, Double>>() {
                @Override
                public int compare(Map.Entry<Integer, Double> entry1, Map.Entry<Integer, Double> entry2) {
                    return Double.compare(entry1.getValue(), entry2.getValue());
                }
            });
            Map<Integer, Double> sortedMap = new LinkedHashMap<>();
            for (Map.Entry<Integer, Double> entry : entryList) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }
            Map<Integer,Double> saved = new LinkedHashMap<>();
            for (Map.Entry<Integer, Double> entry : entryList) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }
            //index 52 last
            int counter=0;
            ArrayList<Integer> out=new ArrayList<>();
            for (Map.Entry<Integer, Double> entry : sortedMap.entrySet()) {
                counter++;
                if(53-counter<opponetDeckSize){
                    out.add(entry.getKey());
                }
            }
            guessedOppononetDeck=out;
            if(guessedOppononetDeck.size()!=opponetDeckSize) System.out.println("PROBELM");
        }
        catch (IOException e) {
            System.out.println("PROBLEM");
            e.printStackTrace();
        }
    }
    //test method
    public String setToString(ArrayList<Integer> set){
        StringBuilder newString = new StringBuilder("[");
        for (int i = 0; i < set.size()-1; i++) {
            newString.append(set.get(i)).append(", ");
        }
        newString.append(set.get(set.size()-1)).append("]");
        return newString.toString();
    }



    private void guessPlayer2DeckAndPile(int opponentDeckSize){
        ArrayList<Integer> allTilesNotPile = CustomUtility.decompose(this.board);
        allTilesNotPile.addAll(this.deck);
        ArrayList<Integer> allTiles = new ArrayList<>(Arrays.asList(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
            31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
            51, 52, 53));
        allTiles.removeAll(allTilesNotPile);
        Collections.shuffle(allTiles);
        for(int i = 0; i < allTiles.size(); i++){
            if(i < opponentDeckSize){
                this.guessedOppononetDeck.add(allTiles.get(i));

            } else {
                this.guessedPile.add(allTiles.get(i));
            }
        }
    }

    public Node getRoot(){
        return this.root;
    }
}
