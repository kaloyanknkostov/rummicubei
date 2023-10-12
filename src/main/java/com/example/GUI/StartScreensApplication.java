package com.example.GUI;
import com.gameEngine.GameEngine;
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
import com.gameEngine.GameRunner;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.fxml.Initializable;


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



    private final GameModel gameModel = GameModel.getInstance();

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Start.fxml")));
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
    //ImageView[] playerboard={p00,p10,p20,p30,p40,p50,p60,p70,p80,p90,p01,p11,p21,p31,p41,p51,p61,p71,p81,p91};


    public void handleStartGame(ActionEvent event) throws IOException {
        System.out.println("checkNames:" + checkNames());
        if (checkNames()) {
            GameRunner.gameStartSignal.complete(null);
            GameEngine game = new GameEngine(gameModel.getNumberOfPlayers(), 0);
           // switchScene("GamePane.fxml", event);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("GamePane.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

            StartScreensApplication controller = loader.getController();
            ImageView[] playerboard={controller.p00,controller.p10,controller.p20,controller.p30,controller.p40,controller.p50,controller.p60,controller.p70,controller.p80,controller.p90,controller.p01,controller.p11,controller.p21,controller.p31,controller.p41,controller.p51,controller.p61,controller.p71,controller.p81,controller.p91};

            controller.p20.setImage(new Image("painted_tile_black_3.png"));
            controller.p30.setImage(new Image("painted_tile_black_3.png"));

            Image ima = new Image("painted_tile_black_3.png");
            int numOfTiles = game.getCurrentPlayer().getDeckOfTiles().size();
            
            for (int i = 0; i < numOfTiles; i++) {
                String test = game.getCurrentPlayer().getDeckOfTiles().get(i).getPicture();
                playerboard[i].setImage(new Image(test));
            }
            

            

            System.out.println("checkNames:" + checkNames());


            } else {
            switchScene("ErrorNoName.fxml", event);
        }
        //needs to save names for now it's here but should be moved to game logic
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
        if(x==2){
            switchScene("TwoPeopleNameInputScene.fxml", event);
        }  if(x==3){
            switchScene("ThreePeopleNameInputScene.fxml", event);
        }  if(x==4) {
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
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(sceneName)));
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
