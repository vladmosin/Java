package ru.hsespb.ticktacktoe;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Match");
        (new PlayGameState()).invokeState(primaryStage, 4);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
