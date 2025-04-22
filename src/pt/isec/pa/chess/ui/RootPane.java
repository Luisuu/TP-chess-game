package pt.isec.pa.chess.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import pt.isec.pa.chess.model.ChessGameManager;

import java.io.File;

public class RootPane extends BorderPane {

    ChessGameManager data;
    CenterPane cPane;
    Label lbStatus;

    //componentes menu
    Menu mnGame, mnMode;
    MenuItem mnNew, mnOpen, mnSave, mnImport, mnExport, mnExit, mnDebug;
    RadioMenuItem normalMode, learningMode;
    CheckMenuItem showMoves;
    MenuItem undoMove, redoMove;

    public RootPane(ChessGameManager data) {
         this.data = data;
         createViews();
         registerHandlers();
         update();
        }

    private void createViews() {
         setTop(createMenu());
         cPane = new CenterPane(data);
         setCenter(cPane);

         lbStatus = new Label("Game not started. Create or load a game from the menu.");
         lbStatus.setStyle("-fx-background-color: #d0d0d0;");
         lbStatus.setPrefWidth(99999);
         lbStatus.setPadding(new Insets(8));
         setBottom(lbStatus);
        }

    private MenuBar createMenu() {
         MenuBar mb = new MenuBar();

         // Menu "Game"
         mnGame = new Menu("_Game");
         mnNew = new MenuItem("_New");
         mnOpen = new MenuItem("_Open");
         mnSave = new MenuItem("_Save");
         mnImport = new MenuItem("_Import");
         mnExport = new MenuItem("_Export");
         mnExit = new MenuItem("E_xit");
         mnDebug = new MenuItem("_DEBUG");
         mnGame.getItems().addAll(mnNew, mnOpen, mnSave, mnImport, mnExport, new SeparatorMenuItem(), mnExit, mnDebug);

         // Menu "Mode"
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

         mnMode.getItems().addAll(normalMode, learningMode, new SeparatorMenuItem(),
               showMoves, undoMove, redoMove);

              mb.getMenus().addAll(mnGame, mnMode);
             return mb;
        }

    private void registerHandlers() {
        mnNew.setOnAction(actionEvent -> {
              AskName askName = new AskName(data);
              askName.showAndWait();
        });

        mnOpen.setOnAction(e -> {
              FileChooser fileChooser = new FileChooser();
              fileChooser.setTitle("Open Game");
              fileChooser.setInitialDirectory(new File("."));
              fileChooser.getExtensionFilters().addAll(
                     new FileChooser.ExtensionFilter("PA Files (*.paf)", "*.paf"),
                     new FileChooser.ExtensionFilter("All", "*.*")
              );
              File hFile = fileChooser.showOpenDialog(this.getScene().getWindow());
              if (hFile != null) {
                  System.out.println(hFile.getAbsolutePath());
                  data.open(hFile.getName());
              }
        });

        mnSave.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Game");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PA Files (*.paf)", "*.paf"),
                    new FileChooser.ExtensionFilter("All", "*.*")
            );
            File hFile = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (hFile != null) {
                 System.out.println(hFile.getAbsolutePath());
                 data.save(hFile.getName());
            }
        });

        mnImport.setOnAction(e -> {
             FileChooser fileChooser = new FileChooser();
             fileChooser.setTitle("Import Game");
             fileChooser.setInitialDirectory(new File("."));
             fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"),
                    new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
             );
             File file = fileChooser.showOpenDialog(this.getScene().getWindow());
             if (file != null) {
                  System.out.println("Importing game from: " + file.getAbsolutePath());
                  data.importa(file.getName());
             }
        });

        mnExport.setOnAction(e -> {
             FileChooser fileChooser = new FileChooser();
             fileChooser.setTitle("Export Game");
             fileChooser.setInitialDirectory(new File("."));
             fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"),
                    new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv")
             );
             File file = fileChooser.showSaveDialog(this.getScene().getWindow());
             if (file != null) {
                System.out.println("Exporting game to: " + file.getAbsolutePath());
                 data.exporta(file.getName());
             }
        });

        mnExit.setOnAction(actionEvent -> {
             Platform.exit();
        });

        mnDebug.setOnAction(actionEvent -> {
            System.out.println(data.debug());
        });

        //muadanças mode
        normalMode.setOnAction(e -> {
             if (normalMode.isSelected()) {
                 showMoves.setDisable(true);
                 undoMove.setDisable(true);
                 redoMove.setDisable(true);
                 // data.setNormalMode();
                 System.out.println("Normal mode activated");
             }
        });

        learningMode.setOnAction(e -> {
             if (learningMode.isSelected()) {
                 showMoves.setDisable(false);
                 undoMove.setDisable(true); //
                 redoMove.setDisable(true);
                 // data.setLearningMode();
                 System.out.println("Learning mode activated");
             }
        });

        showMoves.setOnAction(e -> {
            boolean show = showMoves.isSelected();
            // data.setShowPossibleMoves(show);
            System.out.println("Show possible moves: " + show);
        });
    }

    private void update() {
        //inicialmente desativado os learning modes
        showMoves.setDisable(true);
        undoMove.setDisable(true);
        redoMove.setDisable(true);
    }
}

