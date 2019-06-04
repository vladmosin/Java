package ru.hse.ftp;

import javafx.application.Application;
import javafx.stage.Stage;

public class ClientUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Explorer");
        (new ClientLogicUI(primaryStage)).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
