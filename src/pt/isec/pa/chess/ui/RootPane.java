package pt.isec.pa.chess.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.pieces.Position;

import java.io.File;

public class RootPane extends BorderPane {

    private final ChessGameManager data;
    private final int numRows;
    private final int numCols;

    private CanvasBoard canvasBoard;
    private Label lbStatus;
    private Position selectedPos = null;

    Menu mnGame, mnMode;
    MenuItem mnNew, mnOpen, mnSave, mnImport, mnExport, mnExit, mnDebug;
    RadioMenuItem normalMode, learningMode;
    CheckMenuItem showMoves;
    MenuItem undoMove, redoMove;

    public RootPane(ChessGameManager data) {
        this.data = data;
        this.numRows = data.getBoardRows();
        this.numCols = data.getBoardCols();
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        setTop(createMenu());

        lbStatus = new Label("Game not started. Create or load a game from the menu.");
        lbStatus.setStyle("-fx-background-color: #d0d0d0;");
        lbStatus.setPrefWidth(Double.MAX_VALUE);
        lbStatus.setPadding(new Insets(8));
        setBottom(lbStatus);

        canvasBoard = new CanvasBoard(data, this);
        canvasBoard.widthProperty().bind(widthProperty());
        canvasBoard.heightProperty().bind(heightProperty().subtract(lbStatus.heightProperty()).subtract(25));
        setCenter(canvasBoard);
        canvasBoard.setVisible(false);
    }

    private MenuBar createMenu() {
        MenuBar mb = new MenuBar();

        mnGame = new Menu("_Game");
        mnNew = new MenuItem("_New");
        mnOpen = new MenuItem("_Open");
        mnSave = new MenuItem("_Save");
        mnImport = new MenuItem("_Import");
        mnExport = new MenuItem("_Export");
        mnExit = new MenuItem("E_xit");
        mnDebug = new MenuItem("_DEBUG");

        mnGame.getItems().addAll(mnNew, mnOpen, mnSave, mnImport, mnExport, new SeparatorMenuItem(), mnExit, mnDebug);

        mnMode = new Menu("Mode");
        normalMode = new RadioMenuItem("Normal");
        learningMode = new RadioMenuItem("Learning");
        ToggleGroup modeGroup = new ToggleGroup();
        normalMode.setToggleGroup(modeGroup);
        learningMode.setToggleGroup(modeGroup);
        normalMode.setSelected(true);

        showMoves = new CheckMenuItem("Show possible moves");
        undoMove = new MenuItem("Undo");
        redoMove = new MenuItem("Redo");
        undoMove.setDisable(true);
        redoMove.setDisable(true);

        mnMode.getItems().addAll(normalMode, learningMode, new SeparatorMenuItem(), showMoves, undoMove, redoMove);

        mb.getMenus().addAll(mnGame, mnMode);
        return mb;
    }

    private void registerHandlers() {
        mnNew.setOnAction(actionEvent -> {
            canvasBoard.setVisible(false);
            lbStatus.setText("Game not started. Create or load a game from the menu.");
            data.setPlayerName(1, null);
            data.setPlayerName(2, null);
            AskName askName = new AskName(data);
            askName.showAndWait();
            if (data.getPlayerName(data.getcCurrentPlayer()) != null) {
                data.restart();
                canvasBoard.setVisible(true);

            }
            update();
        });

        mnOpen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Game");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Bin (*.bin)", "*.bin"), new FileChooser.ExtensionFilter("All", "*.*"));
            File hFile = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (hFile != null) {
                System.out.println(hFile.getAbsolutePath());
                data.open(hFile.getAbsolutePath());
                //AskName askName = new AskName(data);
                //askName.showAndWait();
                canvasBoard.setVisible(true);
                lbStatus.setText("Current Player: " + data.getPlayerName(data.getcCurrentPlayer()) + " Color: " + data.getcCurrentPlayer().toString());
                update();
            }
        });

        mnSave.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Game");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Bin (*.bin)", "*.bin"), new FileChooser.ExtensionFilter("All", "*.*"));
            File hFile = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (hFile != null) {
                System.out.println(hFile.getAbsolutePath());
                data.save(hFile.getAbsolutePath());
            }
        });

        mnImport.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Game");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"), new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"), new FileChooser.ExtensionFilter("All Files", "*.*"));
            File file = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (file != null) {
                data.importa(file.getAbsolutePath());
                AskName askName = new AskName(data);
                askName.showAndWait();
                canvasBoard.setVisible(true);
                lbStatus.setText("Current Player: " + data.getPlayerName(data.getcCurrentPlayer()) + " Color: " + data.getcCurrentPlayer().toString());
                update();
            }
        });

        mnExport.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Game");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"),
                    new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));
            File file = fileChooser.showSaveDialog(getScene().getWindow());
            if (file != null) {
                data.exporta(file.getAbsolutePath());
            }
        });

        mnExit.setOnAction(actionEvent -> {
            Platform.exit();
        });
        mnDebug.setOnAction(actionEvent -> {
            System.out.println(data.debug());
        });

        normalMode.setOnAction(e -> {
            if (normalMode.isSelected()) {
                showMoves.setDisable(true);
                undoMove.setDisable(true);
                redoMove.setDisable(true);
                System.out.println("Normal mode activated");
            }
        });

        learningMode.setOnAction(e -> {
            if (learningMode.isSelected()) {
                showMoves.setDisable(false);
                undoMove.setDisable(true);
                redoMove.setDisable(true);
                System.out.println("Learning mode activated");
            }
        });

        showMoves.setOnAction(e -> {
            boolean show = showMoves.isSelected();
            System.out.println("Show possible moves: " + show);
        });

        data.setPromotionListener(position -> {
            char chosen = showPromotionDialog();
            data.promotePawn(position, chosen);
            update();
        });
    }

    public void update() {
        lbStatus.setText("Current Player: " + data.getPlayerName(data.getcCurrentPlayer()) +
                " Color: " + data.getcCurrentPlayer().toString());

        if (data.getWinner() != null) {
            lbStatus.setText("Game finished: " + data.getWinner().toString() + " won");
        }

        canvasBoard.update();
    }

    private char showPromotionDialog() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Queen", "Queen", "Rook", "Bishop", "Knight");
        dialog.setTitle("Promoção do Peão");
        dialog.setHeaderText("Escolha uma peça para promoção:");
        dialog.setContentText("Peça:");
        String result = dialog.showAndWait().orElse("Queen");
        return result.charAt(0);
    }
}
