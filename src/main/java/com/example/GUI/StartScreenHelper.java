package com.example.GUI;


import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;

public class StartScreenHelper {
    private static final StartScreenHelper instance = new StartScreenHelper();
    private static final GameModel gameModel = GameModel.getInstance();

    private StartScreenHelper() {
    }

    public static StartScreenHelper getInstance() {
        return instance;
    }

    public boolean checkNames(TextField firstPlayerName, TextField secondPlayerName, TextField thirdPlayerName, TextField fourthPlayerName) {
        switch (gameModel.getNumberOfPlayers()) {
            case 2 -> {
                if (!(firstPlayerName.getCharacters().isEmpty() || secondPlayerName.getCharacters().isEmpty())) {
                    gameModel.playerNames.add(firstPlayerName.getCharacters().toString());
                    gameModel.playerNames.add(secondPlayerName.getCharacters().toString());
                    return true;
                }
            }
            case 3 -> {
                if (!(firstPlayerName.getCharacters().isEmpty() || secondPlayerName.getCharacters().isEmpty() || thirdPlayerName.getCharacters().isEmpty())) {
                    gameModel.playerNames.add(firstPlayerName.getCharacters().toString());
                    gameModel.playerNames.add(secondPlayerName.getCharacters().toString());
                    gameModel.playerNames.add(thirdPlayerName.getCharacters().toString());
                    return true;
                }
            }
            case 4 -> {
                if (!(firstPlayerName.getCharacters().isEmpty() || secondPlayerName.getCharacters().isEmpty() || thirdPlayerName.getCharacters().isEmpty() || fourthPlayerName.getCharacters().isEmpty())) {
                    gameModel.playerNames.add(firstPlayerName.getCharacters().toString());
                    gameModel.playerNames.add(secondPlayerName.getCharacters().toString());
                    gameModel.playerNames.add(thirdPlayerName.getCharacters().toString());
                    gameModel.playerNames.add(fourthPlayerName.getCharacters().toString());
                    return true;
                }
            }
            default -> {
                return false;
            }
        }
        return false;
    }
}
