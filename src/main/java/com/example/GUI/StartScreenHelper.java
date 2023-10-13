package com.example.GUI;


import javafx.scene.control.TextField;

public class StartScreenHelper {
    private static final StartScreenHelper instance = new StartScreenHelper();
    private static final GameModel gameModel = GameModel.getInstance();

    private StartScreenHelper() {}
    public static StartScreenHelper getInstance() {
        return instance;
    }

    public boolean checkNames(TextField firstPlayerName, TextField secondPlayerName, TextField thirdPlayerName, TextField fourthPlayerName) {
        switch (gameModel.getNumberOfPlayers()) {
            case 2 -> {
                return (!(firstPlayerName.getCharacters().isEmpty() || secondPlayerName.getCharacters().isEmpty()));
            }
            case 3 -> {
                return (!(firstPlayerName.getCharacters().isEmpty() || secondPlayerName.getCharacters().isEmpty() || thirdPlayerName.getCharacters().isEmpty()));
            }
            case 4 -> {
                return (!(firstPlayerName.getCharacters().isEmpty() || secondPlayerName.getCharacters().isEmpty() || thirdPlayerName.getCharacters().isEmpty() || fourthPlayerName.getCharacters().isEmpty()));
            }
            default -> {
                return false;
            }
        }
    }
}
