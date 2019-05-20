package ru.hse.cannon;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/** Implements Scorched Earth game */
public class ScorchedEarth extends Application {

    /** Stores scene and game controller */
    private static class GameInformation {
        @NotNull private Scene scene;
        @NotNull private GameController gameController;

        private GameInformation(@NotNull Scene scene, @NotNull GameController gameController) {
            this.scene = scene;
            this.gameController = gameController;
        }
    }

    /** Width in pixels of game */
    private static final double BEST_WIDTH = 800;

    /** Height in pixels of game */
    private static final double BEST_HEIGHT = 640;

    /** Main scene */
    private Scene mainScene;

    /** Starts game */
    @Override
    public void start(@NotNull Stage primaryStage) throws Exception {
        primaryStage.setTitle("Scorched Earth");

        var gameInformation = createGame();
        var scene = gameInformation.scene;
        var gameController = gameInformation.gameController;

        mainScene = scene;
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(event -> gameController.onKeyPressed(event.getCode()));
        scene.setOnKeyReleased(event -> gameController.onKeyReleased(event.getCode()));

        gameController.launchGame();
    }

    /** Creates game */
    @NotNull private GameInformation createGame() {
        var gridPane = new GridPane();
        gridPane.setPrefSize(BEST_WIDTH, BEST_HEIGHT);

        var canvas = new Canvas();
        gridPane.getChildren().add(canvas);
        canvas.widthProperty().bind(gridPane.widthProperty());
        canvas.heightProperty().bind(gridPane.heightProperty());

        var gameController = new GameController(this, canvas.getGraphicsContext2D());
        var scene = new Scene(gridPane);

        return new GameInformation(scene, gameController);
    }

    /** Shows end screen */
    public void showScreenWithCongratulations() {
        var label = new Label("You win!!!");
        var gridPane = new GridPane();

        label.setFont(new Font(40));
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);

        gridPane.getChildren().add(label);
        mainScene.setRoot(gridPane);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
