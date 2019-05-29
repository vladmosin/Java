package ru.hsespb.ticktacktoe;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class PlayView {
    private GridPane gridPane;
    private Stage stage;
    private int width;
    private int height;
    private Button[][] buttons;

    public PlayView(Stage stage, int width, int height) {
        this.stage = stage;
        this.width = width;
        this.height = height;
    }

    public void show() {
        var scene = new Scene(gridPane, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    public void createBoard(@NotNull InputListener listener) {
        buttons = new Button[height][width];
        gridPane = new GridPane();
        for (int i = 0; i < height; i++) {
            var row = new RowConstraints();
            row.setPercentHeight(100.0 / height);
            gridPane.getRowConstraints().add(row);
        }

        for (int i = 0; i < width; i++) {
            var column = new ColumnConstraints();
            column.setPercentWidth(100.0 / width);
            gridPane.getColumnConstraints().add(column);
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                final int tmpI = i;
                final int tmpJ = j;
                var button = new Button("");
                buttons[i][j] = button;
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.setOnAction(event -> {
                    try {
                        listener.onBoardButtonClick(tmpI, tmpJ);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                gridPane.add(button, j, i);
            }
        }
    }

    public void setButtonText(int x, int y, @NotNull String text) {
        buttons[x][y].setText(text);
    }

    public void showResult(String result) {
        var label = new Label(result);
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gridPane.add(label, 1,1);
    }
}
