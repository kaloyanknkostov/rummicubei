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

import java.io.IOException;
import java.util.Objects;

public class StartScreensApplication extends Application {
    @FXML
    public TextField firstPlayerName;
    @FXML
    public TextField secondPlayerName;
    @FXML
    private ComboBox<String> playerChoiceComboBox;

    private int numberOfPlayers;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Start.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public void handleStartGame(ActionEvent event) {
        System.out.println("checkNames:" + checkNames());
        if (checkNames()) {
            switchScene("GamePane.fxml", event);
        }
        //needs to save names for now it's here but should be moved
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

    public void handlePlayerChoice(ActionEvent event) {
        if (playerChoiceComboBox.getValue() == null) {
            switchScene("ErrorNoPlayersSelected.fxml", event);
        } else {
            switch (playerChoiceComboBox.getValue()) {
                case "2 Players" -> {
                    System.out.println("Starting a game for 2 players");
                    numberOfPlayers = 2;
                    System.out.println("number of players is: " + numberOfPlayers);
                    switchScene("TwoPeopleNameInputScene.fxml", event);
                }
                case "3 Players" -> {
                    System.out.println("Starting a game for 3 players");
                    numberOfPlayers = 3;
                    switchScene("ThreePeopleNameInputScene.fxml", event);
                }
                case "4 Players" -> {
                    System.out.println("Starting a game for 4 players");
                    numberOfPlayers = 4;
                    switchScene("FourPlayerNameInput.fxml", event);
                }
            }
        }
    }


    public void switchScene(String sceneName, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(sceneName)));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkNames() {
        System.out.println("number of players is: " + numberOfPlayers);
        if (numberOfPlayers == 2) {
            System.out.println("name one: " + firstPlayerName);
            System.out.println("name two: " + secondPlayerName);
            return (!(firstPlayerName == null || secondPlayerName == null));
        }
        return true;
    }

    public static void main(String[] args) {
        launch();
    }
}
