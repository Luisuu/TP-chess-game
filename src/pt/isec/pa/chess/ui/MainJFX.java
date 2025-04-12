package pt.isec.pa.chess.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;

public class MainJFX extends Application {
    ChessGameManager chessGameManager;

    public MainJFX() { chessGameManager = new ChessGameManager();}

    @Override
    public void start(Stage stage) throws Exception{
        RootPane root = new RootPane(chessGameManager);
        Scene scene = new Scene(root, 600, 400, Color.ORANGE);
        stage.setScene(scene);
        stage.setTitle("PA Chess Game");
        stage.setResizable(false);
        stage.show();
    }

}
