package com.example.GUI;

import com.gameEngine.Board;
import com.gameEngine.Player;
import com.gameEngine.Set;
import com.gameEngine.Tile;
import javafx.application.Platform;
import javafx.scene.control.Label;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

@SuppressWarnings("CallToPrintStackTrace")
public class StartScreensApplication extends Application {
    @FXML
    public TextField firstPlayerName, secondPlayerName, thirdPlayerName, fourthPlayerName;
    public Label messageLabel;
    @FXML
    private ComboBox<String> playerChoiceComboBox;
    @FXML
    private ImageView DrawTile;
    @FXML
    private ImageView p00, p01, p10, p11, p20, p21, p30, p31, p40, p41, p50, p51, p60, p61, p70, p71, p80, p81, p90, p91, p100, p101, p110, p111, p120, p121;
    @FXML
    private ImageView B000, B001, B002, B003, B004, B005, B006, B007, B008, B009, B010, B011, B012, B013, B014, B015, B016,
            B100, B101, B102, B103, B104, B105, B106, B107, B108, B109, B110, B111, B112, B113, B114, B115, B116,
            B200, B201, B202, B203, B204, B205, B206, B207, B208, B209, B210, B211, B212, B213, B214, B215, B216,
            B300, B301, B302, B303, B304, B305, B306, B307, B308, B309, B310, B311, B312, B313, B314, B315, B316,
            B400, B401, B402, B403, B404, B405, B406, B407, B408, B409, B410, B411, B412, B413, B414, B415, B416,
            B500, B501, B502, B503, B504, B505, B506, B507, B508, B509, B510, B511, B512, B513, B514, B515, B516,
            B600, B601, B602, B603, B604, B605, B606, B607, B608, B609, B610, B611, B612, B613, B614, B615, B616,
            B700, B701, B702, B703, B704, B705, B706, B707, B708, B709, B710, B711, B712, B713, B714, B715, B716;

    private static StartScreensApplication instance;
    public static StartScreensApplication activeController;
    private Parent root;
    private final GameModel gameModel = GameModel.getInstance();
    private final ObjectProperty<ImageView> dragSource = new SimpleObjectProperty<>();
    private final StartScreenHelper helper = StartScreenHelper.getInstance();
    private ArrayList<ArrayList<Image>> curr;

    @Override
    public void start(Stage stage) throws IOException {
        instance = this;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Start.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public StartScreensApplication() {
    }

    public static StartScreensApplication getInstance() {
        if (instance == null) {
            instance = new StartScreensApplication();
        }
        return instance;
    }

    public void handleStartGame(ActionEvent event) {
        if (helper.checkNames(firstPlayerName, secondPlayerName, thirdPlayerName, fourthPlayerName)) {
            System.out.print("checkNames passed");
            gameModel.setStartGame(true);
            switchScene("GamePane.fxml", event);
        } else {
            System.out.println("checkNames not passed");
        }
    }

    public void handleSingleAction(ActionEvent event) {
        gameModel.setNumberOfBots(1);
        gameModel.setNumberOfPlayers(1);
        switchScene("SinglePlayerNameInputScene.fxml", event);
    }

    public void handleNextTurn() {
        gameModel.setNextTurn(true);
        gameModel.setTransferBoardViaImages(transformIntoBoard());
        curr = transformIntoBoard();
    }

    public void handleResetBoard(ActionEvent event) {
        ImageView[] entireBoard = getBoard();
        ArrayList<ArrayList<Image>> newBoardImages = curr;

        int currentIndex = 0;
        ArrayList<Tile> tilesToRemoveFromBoard = new ArrayList<>();

        for (ArrayList<Image> row : newBoardImages) {
            for (Image image : row) {
                ImageView currentImageView = entireBoard[currentIndex];
                if (currentImageView.getImage() != null) {
                    Tile correspondingTile = findTileByImage(image, gameModel.getCurrentPlayer());
                    if (correspondingTile != null) {
                        tilesToRemoveFromBoard.add(correspondingTile);
                    }
                }
                currentImageView.setImage(image);
                currentIndex++;
            }
        }

        Player currentPlayer = gameModel.getCurrentPlayer();
        if (currentPlayer != null) {
            Platform.runLater(() -> currentPlayer.getDeckOfTiles().addAll(tilesToRemoveFromBoard));
        }
        int numOfTiles = gameModel.getCurrentPlayer().getDeckOfTiles().size();
        ImageView[] playerBoard = getPlayerBoard();
        for (int i = 0; i < playerBoard.length; i++) {
            if (i < numOfTiles) {
                Image image = gameModel.getCurrentPlayer().getDeckOfTiles().get(i).getImage();
                playerBoard[i].setImage(image);
                playerBoard[i].setVisible(true);
            } else {
                playerBoard[i].setVisible(false);
            }
        }
        initializeDragAndDrop();
    }

    private Tile findTileByImage(Image image, Player player) {
        if (player != null) {
            for (Tile tile : player.getDeckOfTiles()) {
                if (tile.getImage() != null && tile.getImage().equals(image)) {
                    return tile;
                }
            }
        }
        return null; // Tile not found
    }

    public ArrayList<ArrayList<Image>> transformIntoBoard() {
        ImageView[] imageViews = getBoard();
        ArrayList<ArrayList<Image>> images = new ArrayList<>();
        ArrayList<Image> temp = new ArrayList<>();
        for (int i = 0; i < imageViews.length; i++) {
            temp.add(imageViews[i].getImage());
            if ((i + 1) % 17 == 0) {  // Split every 17 items
                images.add(temp);
                temp = new ArrayList<>();
            }
        }
        if (!temp.isEmpty()) {
            images.add(temp);
        }

        return images;
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
                    gameModel.setNumberOfPlayers(2);
                    switchScene("TwoPeopleNameInputScene.fxml", event);
                }
                case "3 Players" -> {
                    gameModel.setNumberOfPlayers(3);
                    switchScene("ThreePeopleNameInputScene.fxml", event);
                }
                case "4 Players" -> {
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

    public void playerTurn() {
        if (drewATile) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ImageView draw = getDrawImageView();
            draw.setVisible(false);
            drewATile = false;
        }
        try {
            int numOfTiles = gameModel.getCurrentPlayer().getDeckOfTiles().size();
            ImageView[] playerBoard = getPlayerBoard();
            for (int i = 0; i < playerBoard.length; i++) {
                if (i < numOfTiles) {
                    Image image = gameModel.getCurrentPlayer().getDeckOfTiles().get(i).getImage();
                    playerBoard[i].setImage(image);
                    playerBoard[i].setVisible(true);
                } else {
                    playerBoard[i].setVisible(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initializeDragAndDrop();
    }

    public void updateBoard(Board newBoard) {
        ImageView[] GuiBoard = getBoard();
        // clean the board
        for (int index = 0; index < GuiBoard.length; index++) {
            GuiBoard[index].setImage(null);
        }
        ArrayList<Set> setArrayList = newBoard.getSetList();
        int lastEmptySlot = 0;
        //go thought the sets
        for (int setIndex = 0; setIndex < setArrayList.size(); setIndex++) {
            //go thought the tiles in the set
            Set currentTileSet = setArrayList.get(setIndex);
            for (int tileIndex = 0; tileIndex < currentTileSet.getSizes(); tileIndex++) {
                Tile currentTile = currentTileSet.getTileAtIndex(tileIndex);
                GuiBoard[lastEmptySlot].setImage(currentTile.getImage());
                lastEmptySlot+=1;
            }
            lastEmptySlot += 1;
        }
    }


    private boolean drewATile = false;

    public void handleDrawTile() {
        ImageView draw = getDrawImageView();
        Tile drawnTile = gameModel.getDrawTile();
        draw.setImage(new Image(drawnTile.getPicture()));
        draw.setVisible(true);
        drewATile = true;
        handleNextTurn();
    }

    private void initializeDragAndDrop() {
        ArrayList<ImageView> tilesMovedFromDeck = new ArrayList<>();

        ImageView[] playerBoard = getPlayerBoard();
        ImageView[] entireBoard = getEntireBoard();
        Collections.addAll(tilesMovedFromDeck, playerBoard);

        for (ImageView imageView : entireBoard) {
            if (imageView != null) {
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
                    boolean legalMove = true;
                    boolean NoNeedToSwap = false;
                    for (ImageView a : playerBoard) {
                        if ((Objects.equals(source.getId(), a.getId())) && (!tilesMovedFromDeck.contains(imageView) && imageView.getImage() != null)) {
                            legalMove = false;
                        }
                        if ((Objects.equals(imageView.getId(), a.getId())) && !tilesMovedFromDeck.contains(source)) {
                            legalMove = false;
                        }
                    }
                    if (tilesMovedFromDeck.contains(imageView) && tilesMovedFromDeck.contains(source)) {
                        NoNeedToSwap = true;
                    }

                    if (source != null && source != imageView && legalMove) {
                        if (!NoNeedToSwap) {
                            if (tilesMovedFromDeck.contains(source)) {
                                tilesMovedFromDeck.remove(source);
                                tilesMovedFromDeck.add(imageView);
                            } else if (tilesMovedFromDeck.contains(imageView)) {
                                tilesMovedFromDeck.remove(imageView);
                                tilesMovedFromDeck.add(source);
                            }
                        }
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
            }
        }
    }

    public void setMessageLabel(String player, String extraMessage) {
        String text = "Current Player: " + player + "\n" + extraMessage;
        Platform.runLater(() -> activeController.messageLabel.setText(text));
    }

    private ImageView[] getPlayerBoard() {
        return new ImageView[]{activeController.p00, activeController.p01, activeController.p10, activeController.p11,
                activeController.p20, activeController.p21, activeController.p30, activeController.p31,
                activeController.p40, activeController.p41, activeController.p50, activeController.p51,
                activeController.p60, activeController.p61, activeController.p70, activeController.p71,
                activeController.p80, activeController.p81, activeController.p90, activeController.p91,
                activeController.p100, activeController.p101, activeController.p110, activeController.p111,
                activeController.p120, activeController.p121
        };
    }

    private ImageView[] getBoard() {
        return new ImageView[]{
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
    }

    private ImageView getDrawImageView() {
        return activeController.DrawTile;
    }

    private ImageView[] getEntireBoard() {
        return new ImageView[]{
                activeController.p00, activeController.p01, activeController.p10, activeController.p11,
                activeController.p20, activeController.p21, activeController.p30, activeController.p31,
                activeController.p40, activeController.p41, activeController.p50, activeController.p51,
                activeController.p60, activeController.p61, activeController.p70, activeController.p71,
                activeController.p80, activeController.p81, activeController.p90, activeController.p91,
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
                activeController.B713, activeController.B714, activeController.B715, activeController.B716,
                activeController.p100, activeController.p101, activeController.p110, activeController.p111,
                activeController.p120, activeController.p121
        };
    }
}
