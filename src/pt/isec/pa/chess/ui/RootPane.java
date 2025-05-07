package pt.isec.pa.chess.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.pieces.Position;
import pt.isec.pa.chess.ui.res.ImageManager;

import java.io.File;

import static java.lang.Double.min;

public class RootPane extends BorderPane {

    private final ChessGameManager data;
    private final int numRows;
    private final int numCols;
    private final double canvasSize = 600;

    private Canvas canvas;
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

        canvas = new Canvas();
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty().subtract(lbStatus.heightProperty()).subtract(25));
        setCenter(canvas);
        canvas.setVisible(false);

        canvas.widthProperty().addListener((obs, oldVal, newVal) -> update());
        canvas.heightProperty().addListener((obs, oldVal, newVal) -> update());
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

    private void drawBoard(GraphicsContext gc) {
        double sizeUser = min(canvas.getWidth(), canvas.getHeight());
        double width = sizeUser;
        double height = sizeUser;
        double marginX = width * 0.08;
        double marginY = height * 0.08;
        double boardWidth = width - 2 * marginX;
        double boardHeight = height - 2 * marginY;
        double cellWidth = boardWidth / numCols;
        double cellHeight = boardHeight / numRows;

        gc.clearRect(0, 0, width, height);

        Color lightColor = Color.web("#fceee9");
        Color darkColor = Color.web("#944b45");

        gc.setFont(javafx.scene.text.Font.font(cellHeight * 0.4));

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                boolean light = (row + col) % 2 == 0;
                gc.setFill(light ? lightColor : darkColor);
                double x = marginX + col * cellWidth;
                double y = marginY + row * cellHeight;
                gc.fillRect(x, y, cellWidth, cellHeight);
            }
        }

        for (int col = 0; col < numCols; col++) {
            char colLabel = (char) ('a' + col);
            double x = marginX + col * cellWidth + cellWidth / 2 - 5;
            double y = marginY + boardHeight + cellHeight * 0.7;
            gc.setFill(Color.BLACK);
            gc.fillText(String.valueOf(colLabel), x, y);
        }

        for (int row = 0; row < numRows; row++) {
            String rowLabel = String.valueOf(numRows - row);
            double y = marginY + row * cellHeight + cellHeight / 2 + 5;

            gc.fillText(rowLabel, marginX - cellWidth * 0.4, y);
            gc.fillText(rowLabel, marginX + boardWidth + cellWidth * 0.1, y);
        }
    }


    private void drawPieces(GraphicsContext gc) {
        double sizeUser = min(canvas.getWidth(), canvas.getHeight());

        double width = sizeUser;
        double height = sizeUser;

        double marginX = width * 0.08;
        double marginY = height * 0.08;

        double boardWidth = width - 2 * marginX;
        double boardHeight = height - 2 * marginY;

        double cellWidth = boardWidth / numCols;
        double cellHeight = boardHeight / numRows;

        String pieceData = data.getBoardPiecesString();
        String[] tokens = pieceData.split(",");

        for (String token : tokens) {
            token = token.trim();
            if (token.length() < 3) continue;

            char type = token.charAt(0);
            boolean isWhite = Character.isUpperCase(type);
            String position = token.substring(1, 3).toLowerCase();
            int col = position.charAt(0) - 'a';
            int row = numRows - Character.getNumericValue(position.charAt(1));

            String pieceName = switch (Character.toUpperCase(type)) {
                case 'K' -> "king";
                case 'Q' -> "queen";
                case 'R' -> "rook";
                case 'B' -> "bishop";
                case 'N' -> "knight";
                case 'P' -> "pawn";
                default -> null;
            };

            if (pieceName != null) {
                String filename = "pieces/" + pieceName + (isWhite ? "W" : "B") + ".png";
                Image img = ImageManager.getImage(filename);
                if (img != null) {
                    double x = marginX + col * cellWidth + cellWidth * 0.1;
                    double y = marginY + row * cellHeight + cellHeight * 0.1;
                    double w = cellWidth * 0.8;
                    double h = cellHeight * 0.8;
                    gc.drawImage(img, x, y, w, h);
                }
            }
        }
    }

    private void registerHandlers() {
        canvas.setOnMouseClicked(this::handleClick);

        mnNew.setOnAction(actionEvent -> {
            canvas.setVisible(false);
            //lbStatus = new Label("Game not started. Create or load a game from the menu.");
            data.setPlayerName(1, null);
            data.setPlayerName(2, null);
            AskName askName = new AskName(data);
            askName.showAndWait();
            if (data.getPlayerName(data.getcCurrentPlayer()) != null) {
                data.restart();
                canvas.setVisible(true);
                lbStatus.setText("Current Player: " + data.getPlayerName(data.getcCurrentPlayer()) + " Color: " + data.getcCurrentPlayer().toString());
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
                canvas.setVisible(true);
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
                canvas.setVisible(true);
                lbStatus.setText("Current Player: " + data.getPlayerName(data.getcCurrentPlayer()) + " Color: " + data.getcCurrentPlayer().toString());
                update();
            }
        });

        mnExport.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Game");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"), new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));
            File file = fileChooser.showSaveDialog(this.getScene().getWindow());
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

    private void handleClick(MouseEvent e) {
        double sizeUser = min(canvas.getWidth(), canvas.getHeight());

        double width = sizeUser;
        double height = sizeUser;
        double marginX = width * 0.08;
        double marginY = height * 0.08;
        double boardWidth = width - 2 * marginX;
        double boardHeight = height - 2 * marginY;
        double cellWidth = boardWidth / numCols;
        double cellHeight = boardHeight / numRows;
        double mouseX = e.getX();
        double mouseY = e.getY();

        if (mouseX < marginX || mouseX > marginX + boardWidth || mouseY < marginY || mouseY > marginY + boardHeight) {
            return;
        }

        int col = (int) ((mouseX - marginX) / cellWidth);
        int row = (int) ((mouseY - marginY) / cellHeight);

        char colLetter = (char) ('a' + col);
        int boardRow = numRows - row;

        Position clicked = new Position(colLetter, boardRow);

        //System.out.println("Clicked: " + clicked);

        if (selectedPos == null) {
            selectedPos = clicked;
        } else {
            //System.out.println("selectedPos: " + selectedPos);
            //System.out.println("clicked: " + clicked);
            data.movePiece(selectedPos, clicked);
            selectedPos = null;
            lbStatus.setText("Current Player: " + data.getPlayerName(data.getcCurrentPlayer()) + " Color: " + data.getcCurrentPlayer().toString());
            update();
        }
    }


    private char showPromotionDialog() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Queen", "Queen", "Rook", "Bishop", "Knight");
        dialog.setTitle("Promoção do Peão");
        dialog.setHeaderText("Escolha uma peça para promoção:");
        dialog.setContentText("Peça:");
        String result = dialog.showAndWait().orElse("Queen");
        return result.charAt(0);
    }

    private void update() {
        showMoves.setDisable(true);
        undoMove.setDisable(true);
        redoMove.setDisable(true);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawBoard(gc);
        drawPieces(gc);
    }
}
