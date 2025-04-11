package pt.isec.pa.chess.ui;

import javafx.application.Application;

import java.awt.*;


public class MainJFX extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        Scene scene = new Scene(root, 600, 400, Color.ORANGE);
        stage.setScene(scene);
        stage.setTitle("PA Chess Game");
        stage.setResizable(false);
        stage.show();
    }

}
