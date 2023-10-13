package com.example.GUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.IOException;
import java.util.Objects;

public class StartScreensApplication extends Application {
    @FXML
    public TextField firstPlayerName;
    @FXML
    public TextField secondPlayerName;
    @FXML
    public TextField thirdPlayerName;
    @FXML
    public TextField fourthPlayerName;
    @FXML
    private ComboBox<String> playerChoiceComboBox;
    public static StartScreensApplication activeController;
    private Parent root;
    private final GameModel gameModel = GameModel.getInstance();

    @Override
    public void start(Stage stage) throws IOException {
        instance = this;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Start.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    ImageView p00;
    @FXML
    ImageView p01;
    @FXML
    ImageView p10;
    @FXML
    ImageView p11;
    @FXML
    ImageView p20;
    @FXML
    ImageView p21;
    @FXML
    ImageView p30;
    @FXML
    ImageView p31;
    @FXML
    ImageView p40;
    @FXML
    ImageView p41;
    @FXML
    ImageView p50;
    @FXML
    ImageView p51;
    @FXML
    ImageView p60;
    @FXML
    ImageView p61;
    @FXML
    ImageView p70;
    @FXML
    ImageView p71;
    @FXML
    ImageView p80;
    @FXML
    ImageView p81;
    @FXML
    ImageView p90;
    @FXML
    ImageView p91;
    private static StartScreensApplication instance;

    // Step 1: Private constructor
    public StartScreensApplication() {
    }

    // Step 3: Public static method to get the instance
    public static StartScreensApplication getInstance() {
        if (instance == null) {
            instance = new StartScreensApplication();
        }
        return instance;
    }

    public void handleStartGame(ActionEvent event) {
        System.out.println("checkNames:" + checkNames());
        if (checkNames()) {
            System.out.print("checkNames passed");
            gameModel.setStartGame(true);
            switchScene("GamePane.fxml", event);
        }
    }

    public void playerTurn() {
        try {
            int numOfTiles = gameModel.getCurrentPlayer().getDeckOfTiles().size();

            ImageView[] playerBoard = {
                    activeController.p00, activeController.p01,
                    activeController.p10, activeController.p11,
                    activeController.p20, activeController.p21,
                    activeController.p30, activeController.p31,
                    activeController.p40, activeController.p41,
                    activeController.p50, activeController.p51,
                    activeController.p60, activeController.p61,
                    activeController.p70, activeController.p71,
                    activeController.p80, activeController.p81,
                    activeController.p90, activeController.p91
            };

            for (int i = 0; i < Math.min(numOfTiles, playerBoard.length); i++) {
                String tileImage = gameModel.getCurrentPlayer().getDeckOfTiles().get(i).getPicture();
                playerBoard[i].setImage(new Image(tileImage)); // I've also changed the image to be set from the tileImage variable.
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void handleSingleAction(ActionEvent event) {
        switchScene("ErrorAI.fxml", event);
    }

    public void handleMultiplayerAction(ActionEvent event) {
        switchScene("SelectPlayerScene.fxml", event);
    }

    public void handleBackToStart(ActionEvent event) {
        switchScene("Start.fxml", event);
    }

    public void handleBackToChoice(ActionEvent event) {
        switchScene("SelectPlayerScene.fxml", event);
    }

    public void handleBackToName(ActionEvent event) {
        int x = gameModel.getNumberOfPlayers();
        if (x == 2) {
            switchScene("TwoPeopleNameInputScene.fxml", event);
        }
        if (x == 3) {
            switchScene("ThreePeopleNameInputScene.fxml", event);
        }
        if (x == 4) {
            switchScene("FourPlayerNameInput.fxml", event);
        }
    }

    public void handlePlayerChoice(ActionEvent event) {
        if (playerChoiceComboBox.getValue() == null) {
            switchScene("ErrorNoPlayersSelected.fxml", event);
        } else {
            switch (playerChoiceComboBox.getValue()) {
                case "2 Players" -> {
                    System.out.println("Starting a game for 2 players");
                    gameModel.setNumberOfPlayers(2);
                    switchScene("TwoPeopleNameInputScene.fxml", event);
                }
                case "3 Players" -> {
                    System.out.println("Starting a game for 3 players");
                    gameModel.setNumberOfPlayers(3);
                    switchScene("ThreePeopleNameInputScene.fxml", event);
                }
                case "4 Players" -> {
                    System.out.println("Starting a game for 4 players");
                    gameModel.setNumberOfPlayers(4);
                    switchScene("FourPlayerNameInput.fxml", event);
                }
            }
        }
    }


    public void switchScene(String sceneName, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
            root = loader.load();
            activeController = loader.getController(); // set the active controller

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean checkNames() {
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
