package com.example.GUI;

import javafx.scene.input.DragEvent;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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
    @FXML
            //these are all the image views for the board there are a lot unfortunately
    ImageView B000;
    @FXML
    ImageView B001;
    @FXML
    ImageView B002;
    @FXML
    ImageView B003;
    @FXML
    ImageView B004;
    @FXML
    ImageView B005;
    @FXML
    ImageView B006;
    @FXML
    ImageView B007;
    @FXML
    ImageView B008;
    @FXML
    ImageView B009;
    @FXML
    ImageView B010;
    @FXML
    ImageView B011;
    @FXML
    ImageView B012;
    @FXML
    ImageView B013;
    @FXML
    ImageView B014;
    @FXML
    ImageView B015;
    @FXML
    ImageView B016;
    @FXML
    ImageView B100;
    @FXML
    ImageView B101;
    @FXML
    ImageView B102;
    @FXML
    ImageView B103;
    @FXML
    ImageView B104;
    @FXML
    ImageView B105;
    @FXML
    ImageView B106;
    @FXML
    ImageView B107;
    @FXML
    ImageView B108;
    @FXML
    ImageView B109;
    @FXML
    ImageView B110;
    @FXML
    ImageView B111;
    @FXML
    ImageView B112;
    @FXML
    ImageView B113;
    @FXML
    ImageView B114;
    @FXML
    ImageView B115;
    @FXML
    ImageView B116;
    @FXML
    ImageView B200;
    @FXML
    ImageView B201;
    @FXML
    ImageView B202;
    @FXML
    ImageView B203;
    @FXML
    ImageView B204;
    @FXML
    ImageView B205;
    @FXML
    ImageView B206;
    @FXML
    ImageView B207;
    @FXML
    ImageView B208;
    @FXML
    ImageView B209;
    @FXML
    ImageView B210;
    @FXML
    ImageView B211;
    @FXML
    ImageView B212;
    @FXML
    ImageView B213;
    @FXML
    ImageView B214;
    @FXML
    ImageView B215;
    @FXML
    ImageView B216;
    @FXML
    ImageView B300;
    @FXML
    ImageView B301;
    @FXML
    ImageView B302;
    @FXML
    ImageView B303;
    @FXML
    ImageView B304;
    @FXML
    ImageView B305;
    @FXML
    ImageView B306;
    @FXML
    ImageView B307;
    @FXML
    ImageView B308;
    @FXML
    ImageView B309;
    @FXML
    ImageView B310;
    @FXML
    ImageView B311;
    @FXML
    ImageView B312;
    @FXML
    ImageView B313;
    @FXML
    ImageView B314;
    @FXML
    ImageView B315;
    @FXML
    ImageView B316;
    ImageView B400;
    @FXML
    ImageView B401;
    @FXML
    ImageView B402;
    @FXML
    ImageView B403;
    @FXML
    ImageView B404;
    @FXML
    ImageView B405;
    @FXML
    ImageView B406;
    @FXML
    ImageView B407;
    @FXML
    ImageView B408;
    @FXML
    ImageView B409;
    @FXML
    ImageView B410;
    @FXML
    ImageView B411;
    @FXML
    ImageView B412;
    @FXML
    ImageView B413;
    @FXML
    ImageView B414;
    @FXML
    ImageView B415;
    @FXML
    ImageView B416;
    @FXML
    ImageView B500;
    @FXML
    ImageView B501;
    @FXML
    ImageView B502;
    @FXML
    ImageView B503;
    @FXML
    ImageView B504;
    @FXML
    ImageView B505;
    @FXML
    ImageView B506;
    @FXML
    ImageView B507;
    @FXML
    ImageView B508;
    @FXML
    ImageView B509;
    @FXML
    ImageView B510;
    @FXML
    ImageView B511;
    @FXML
    ImageView B512;
    @FXML
    ImageView B513;
    @FXML
    ImageView B514;
    @FXML
    ImageView B515;
    @FXML
    ImageView B516;
    @FXML
    ImageView B600;
    @FXML
    ImageView B601;
    @FXML
    ImageView B602;
    @FXML
    ImageView B603;
    @FXML
    ImageView B604;
    @FXML
    ImageView B605;
    @FXML
    ImageView B606;
    @FXML
    ImageView B607;
    @FXML
    ImageView B608;
    @FXML
    ImageView B609;
    @FXML
    ImageView B610;
    @FXML
    ImageView B611;
    @FXML
    ImageView B612;
    @FXML
    ImageView B613;
    @FXML
    ImageView B614;
    @FXML
    ImageView B615;
    @FXML
    ImageView B616;
    @FXML
    ImageView B700;
    @FXML
    ImageView B701;
    @FXML
    ImageView B702;
    @FXML
    ImageView B703;
    @FXML
    ImageView B704;
    @FXML
    ImageView B705;
    @FXML
    ImageView B706;
    @FXML
    ImageView B707;
    @FXML
    ImageView B708;
    @FXML
    ImageView B709;
    @FXML
    ImageView B710;
    @FXML
    ImageView B711;
    @FXML
    ImageView B712;
    @FXML
    ImageView B713;
    @FXML
    ImageView B714;
    @FXML
    ImageView B715;
    @FXML
    ImageView B716;

    private static StartScreensApplication instance;
    public static StartScreensApplication activeController;
    private Parent root;
    private final GameModel gameModel = GameModel.getInstance();
    private final ObjectProperty<ImageView> dragSource = new SimpleObjectProperty<>();

    public StartScreensApplication() {
    }

    @Override
    public void start(Stage stage) throws IOException {
        instance = this;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Start.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

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
            ImageView[] board = {
                    activeController.B000, activeController.B001, activeController.B002, activeController.B003,
                    activeController.B004, activeController.B005, activeController.B006, activeController.B007,
                    activeController.B008, activeController.B009, activeController.B010, activeController.B011,
                    activeController.B012, activeController.B013, activeController.B014, activeController.B015,
                    activeController.B016, activeController.B100, activeController.B101, activeController.B102,
                    activeController.B103, activeController.B104, activeController.B105, activeController.B106,
                    activeController.B107, activeController.B108, activeController.B109, activeController.B110,
                    activeController.B111, activeController.B112, activeController.B113, activeController.B114,
                    activeController.B115, activeController.B116, activeController.B200, activeController.B201,
                    activeController.B202, activeController.B203, activeController.B204, activeController.B205,
                    activeController.B206, activeController.B207, activeController.B208, activeController.B209,
                    activeController.B210, activeController.B211, activeController.B212, activeController.B213,
                    activeController.B214, activeController.B215, activeController.B216, activeController.B300,
                    activeController.B301, activeController.B302, activeController.B303, activeController.B304,
                    activeController.B305, activeController.B306, activeController.B307, activeController.B308,
                    activeController.B309, activeController.B310, activeController.B311, activeController.B312,
                    activeController.B313, activeController.B314, activeController.B315, activeController.B316,
                    activeController.B400, activeController.B401, activeController.B402, activeController.B403,
                    activeController.B404, activeController.B405, activeController.B406, activeController.B407,
                    activeController.B408, activeController.B409, activeController.B410, activeController.B411,
                    activeController.B412, activeController.B413, activeController.B414, activeController.B415,
                    activeController.B416, activeController.B500, activeController.B501, activeController.B502,
                    activeController.B503, activeController.B504, activeController.B505, activeController.B506,
                    activeController.B507, activeController.B508, activeController.B509, activeController.B510,
                    activeController.B511, activeController.B512, activeController.B513, activeController.B514,
                    activeController.B515, activeController.B516, activeController.B600, activeController.B601,
                    activeController.B602, activeController.B603, activeController.B604, activeController.B605,
                    activeController.B606, activeController.B607, activeController.B608, activeController.B609,
                    activeController.B610, activeController.B611, activeController.B612, activeController.B613,
                    activeController.B614, activeController.B615, activeController.B616, activeController.B700,
                    activeController.B701, activeController.B702, activeController.B703, activeController.B704,
                    activeController.B705, activeController.B706, activeController.B707, activeController.B708,
                    activeController.B709, activeController.B710, activeController.B711, activeController.B712,
                    activeController.B713, activeController.B714, activeController.B715, activeController.B716
            };

            for (int i = 0; i < playerBoard.length; i++) {
                if (i < numOfTiles) {
                    String tileImage = gameModel.getCurrentPlayer().getDeckOfTiles().get(i).getPicture();
                    playerBoard[i].setImage(new Image(tileImage));
                    playerBoard[i].setVisible(true);
                    System.out.println(playerBoard[i]);
                } else {
                    playerBoard[i].setVisible(false);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        initializeDragAndDrop();
    }

    private void initializeDragAndDrop() {
        ImageView[] entireBoard = {
                activeController.p00, activeController.p01,
                activeController.p10, activeController.p11,
                activeController.p20, activeController.p21,
                activeController.p30, activeController.p31,
                activeController.p40, activeController.p41,
                activeController.p50, activeController.p51,
                activeController.p60, activeController.p61,
                activeController.p70, activeController.p71,
                activeController.p80, activeController.p81,
                activeController.p90, activeController.p91,
                activeController.B000, activeController.B001, activeController.B002, activeController.B003,
                activeController.B004, activeController.B005, activeController.B006, activeController.B007,
                activeController.B008, activeController.B009, activeController.B010, activeController.B011,
                activeController.B012, activeController.B013, activeController.B014, activeController.B015,
                activeController.B016, activeController.B100, activeController.B101, activeController.B102,
                activeController.B103, activeController.B104, activeController.B105, activeController.B106,
                activeController.B107, activeController.B108, activeController.B109, activeController.B110,
                activeController.B111, activeController.B112, activeController.B113, activeController.B114,
                activeController.B115, activeController.B116, activeController.B200, activeController.B201,
                activeController.B202, activeController.B203, activeController.B204, activeController.B205,
                activeController.B206, activeController.B207, activeController.B208, activeController.B209,
                activeController.B210, activeController.B211, activeController.B212, activeController.B213,
                activeController.B214, activeController.B215, activeController.B216, activeController.B300,
                activeController.B301, activeController.B302, activeController.B303, activeController.B304,
                activeController.B305, activeController.B306, activeController.B307, activeController.B308,
                activeController.B309, activeController.B310, activeController.B311, activeController.B312,
                activeController.B313, activeController.B314, activeController.B315, activeController.B316,
                activeController.B400, activeController.B401, activeController.B402, activeController.B403,
                activeController.B404, activeController.B405, activeController.B406, activeController.B407,
                activeController.B408, activeController.B409, activeController.B410, activeController.B411,
                activeController.B412, activeController.B413, activeController.B414, activeController.B415,
                activeController.B416, activeController.B500, activeController.B501, activeController.B502,
                activeController.B503, activeController.B504, activeController.B505, activeController.B506,
                activeController.B507, activeController.B508, activeController.B509, activeController.B510,
                activeController.B511, activeController.B512, activeController.B513, activeController.B514,
                activeController.B515, activeController.B516, activeController.B600, activeController.B601,
                activeController.B602, activeController.B603, activeController.B604, activeController.B605,
                activeController.B606, activeController.B607, activeController.B608, activeController.B609,
                activeController.B610, activeController.B611, activeController.B612, activeController.B613,
                activeController.B614, activeController.B615, activeController.B616, activeController.B700,
                activeController.B701, activeController.B702, activeController.B703, activeController.B704,
                activeController.B705, activeController.B706, activeController.B707, activeController.B708,
                activeController.B709, activeController.B710, activeController.B711, activeController.B712,
                activeController.B713, activeController.B714, activeController.B715, activeController.B716
        };

        for (ImageView imageView: entireBoard) {
            if(imageView!=null) {
                imageView.setOnDragDetected(event -> {
                    Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(imageView.getImage());
                    db.setContent(content);
                    dragSource.set(imageView);
                    event.consume();

                });

            imageView.setOnDragOver(event -> {
                if (event.getGestureSource() != imageView && event.getDragboard().hasImage()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            imageView.setOnDragDropped(event -> {
                ImageView source = dragSource.get();
                if (source != null && source != imageView) {
                    Image tempImage = imageView.getImage();
                    imageView.setImage(source.getImage());
                    source.setImage(tempImage);
                    event.setDropCompleted(true);
                    dragSource.set(null);
                } else {
                    event.setDropCompleted(false);
                }
                event.consume();
            });

            imageView.setOnDragDone(DragEvent::consume);
        } }
    }
}
