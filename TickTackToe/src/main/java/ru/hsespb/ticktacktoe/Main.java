package ru.hsespb.ticktacktoe;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
    private static boolean possibleArgs = true;
    private static int size;

    @Override
    public void start(Stage primaryStage) {
        /*if (possibleArgs) {
            primaryStage.setTitle("Match");
            (new PlayGameState()).invokeState(primaryStage, size);
        } else {
            primaryStage.setTitle("Illegal arguments");
            var scene = new Scene(new GridPane(), 500, 500);
            primaryStage.setScene(scene);
            primaryStage.show();
        }*/
        primaryStage.setTitle("Match");
        (new PlayGameState()).invokeState(primaryStage, 4);
    }
    public static void main(String[] args) {
        /*if (args.length != 1) {
            possibleArgs = false;
        } else {
            try {
                size = Integer.parseInt(args[0]);
                if (size < 2 || size > 100 || size % 2 != 1) {
                    possibleArgs = false;
                }
            }
            catch (NumberFormatException e)
            {
                possibleArgs = false;
            }
        }*/

        launch(args);
    }
}
