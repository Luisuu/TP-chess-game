package pt.isec.pa.chess.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.ModelLog;
import pt.isec.pa.chess.model.data.pieces.PieceColor;
import pt.isec.pa.chess.model.data.pieces.Position;

import pt.isec.pa.chess.ui.res.SoundManager;

import java.io.File;

public class RootPane extends BorderPane {

    private final ChessGameManager data;
    private final int numRows;
    private final int numCols;

    private CanvasBoard canvasBoard;
    private Label lbStatus;
    private Position selectedPos = null;
    private LogList logWindow;

    Menu mnGame, mnMode, mnSettings;
    MenuItem mnNew, mnOpen, mnSave, mnImport, mnExport, mnExit, mnDebug;
    RadioMenuItem normalMode, learningMode;
    CheckMenuItem showMoves, soundToggle;
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
        showMoves.setDisable(true);

        mnMode.getItems().addAll(normalMode, learningMode, new SeparatorMenuItem(), showMoves, undoMove, redoMove);

        mnSettings = new Menu("Settings");
        soundToggle = new CheckMenuItem("Sound");
        mnSettings.getItems().addAll(soundToggle);

        mb.getMenus().addAll(mnGame, mnMode, mnSettings);
        return mb;
    }

    private void registerHandlers() {
        soundToggle.setOnAction(e -> {
            boolean bSound = soundToggle.isSelected();
            SoundManager.setEnabled(bSound);
        });

        data.addPropertyChangeListener(ChessGameManager.PROP_BOARD_UPDATE, evt -> {
            //System.out.println("Board updated");
            //canvasBoard.update();
        });

        data.addPropertyChangeListener(ChessGameManager.PROP_GAME_STATE, evt -> {
            //System.out.println("Game state");
            updateStatus();
        });


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
                ModelLog.getInstance().log("New game started.");
            }
        });

        mnOpen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Game");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Bin (*.bin)", "*.bin"), new FileChooser.ExtensionFilter("All", "*.*"));
            File hFile = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (hFile != null) {
                //System.out.println(hFile.getAbsolutePath());
                data.open(hFile.getAbsolutePath());
                //AskName askName = new AskName(data);
                //askName.showAndWait();
                canvasBoard.setVisible(true);
                lbStatus.setText("Current Player: " + data.getPlayerName(data.getcCurrentPlayer()) + " Color: " + data.getcCurrentPlayer().toString());
                ModelLog.getInstance().log("Game loaded from: " + hFile.getAbsolutePath());
            }
        });

        mnSave.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Game");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Bin (*.bin)", "*.bin"), new FileChooser.ExtensionFilter("All", "*.*"));
            File hFile = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (hFile != null) {
                //System.out.println(hFile.getAbsolutePath());
                data.save(hFile.getAbsolutePath());
                ModelLog.getInstance().log("Game saved to: " + hFile.getAbsolutePath());
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
                ModelLog.getInstance().log("Game imported from: " + file.getAbsolutePath());
            }
        });

        mnExport.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Game");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"), new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));
            File file = fileChooser.showSaveDialog(getScene().getWindow());
            if (file != null) {
                data.exporta(file.getAbsolutePath());
                ModelLog.getInstance().log("Game exported to: " + file.getAbsolutePath());
            }
        });

        mnExit.setOnAction(actionEvent -> {
            Platform.exit();
        });

        mnDebug.setOnAction(actionEvent -> {
            String debugOutput = data.debug();
            //System.out.println(debugOutput);
            if (logWindow != null) {
                logWindow.log("DEBUG:");
                for (String line : debugOutput.split("\n")) {
                    logWindow.log("  " + line);
                }
            }
        });

        normalMode.setOnAction(e -> {
            if (normalMode.isSelected()) {
                showMoves.setDisable(true);
                undoMove.setDisable(true);
                redoMove.setDisable(true);
            }
        });

        learningMode.setOnAction(e -> {
            if (learningMode.isSelected()) {
                showMoves.setDisable(false);
                undoMove.setDisable(false);
                redoMove.setDisable(false);
            }
        });

        showMoves.setOnAction(e -> {
            boolean show = showMoves.isSelected();
            showMoves.setDisable(false);
            //System.out.println("Show possible moves: " + show);
        });

        undoMove.setOnAction(e -> {
            data.undo();
        });

        redoMove.setOnAction(e -> {
            data.redo();
        });

        /* FIXME tirar promotion
        data.setPromotionListener(position -> {
            char chosen = showPromotionDialog();
            data.promotePawn(position, chosen);
        });*/
    }

    private void updateStatus() {
        if (data.getPlayerName(PieceColor.WHITE) == null || data.getPlayerName(PieceColor.BLACK) == null) {
            lbStatus.setText("Game not started. Create or load a game from the menu.");
            return;
        }
        if (data.isGameOver()) {
            if (data.getWinner() != null) {
                lbStatus.setText("Game finished: " + data.getWinner().toString() + " won");
            } else {
                lbStatus.setText("Game finished: Draw");
            }
        } else {
            lbStatus.setText("Current Player: " + data.getPlayerName(data.getcCurrentPlayer()) + " Color: " + data.getcCurrentPlayer().toString());
        }
    }

    private void update() { //VERIFICAR SE E PRIVATE OU PUBLIC ACHO QU E PRIVATE
        updateStatus();
        //canvasBoard.update();
    }

    public char showPromotionDialog() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Queen", "Queen", "Rook", "Bishop", "Knight");
        dialog.setTitle("Pawn Promotion");
        dialog.setHeaderText("Chose a piece for promotion:");
        dialog.setContentText("Piece:");
        String result = dialog.showAndWait().orElse("Queen");
        return switch (result) {
            case "Queen" -> 'Q';
            case "Rook" -> 'R';
            case "Bishop" -> 'B';
            case "Knight" -> 'N';
            default -> 'Q'; // fallback
        };
    }

    public void setLogWindow(LogList logWindow) {
        this.logWindow = logWindow;
    }

    public LogList getLogWindow() {
        return logWindow;
    }

    public boolean isLearningModeActive() {
        return learningMode.isSelected();
    }

    public boolean isShowMovesActive() {
        return showMoves.isSelected();
    }
}
