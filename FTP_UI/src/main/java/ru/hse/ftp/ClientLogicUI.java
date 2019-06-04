package ru.hse.ftp;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ClientLogicUI {
    private static class FileInformation {
        @NotNull private String name;
        private boolean isDirectory;

        public FileInformation(@NotNull String name, boolean isDirectory) {
            this.name = name;
            this.isDirectory = isDirectory;
        }

        public String toString() {
            if (isDirectory) {
                return "Directory: " + name;
            } else {
                return "File: " + name;
            }
        }
    }

    @NotNull private ArrayList<String> path = new ArrayList<>();
    @NotNull private Client client = new Client();
    @NotNull private Stage stage;

    public ClientLogicUI(@NotNull Stage stage) throws IOException {
        this.stage = stage;
    }

    public void start() throws IOException {
        showNewState();
    }

    @NotNull private String getPath() {
        var stringBuilder = new StringBuilder(".");

        for (var directory : path) {
            stringBuilder.append(File.separator);
            stringBuilder.append(directory);
        }

        return stringBuilder.toString();
    }

    private void changeState(@NotNull String directory) throws IOException {
        if (directory.equals("...")) {
            if (path.size() != 0) {
                path.remove(path.size() - 1);
                showNewState();
            }
        } else {
            if (directory.split(" ")[0].equals("Directory:")) {
                path.add(directory.split(" ")[1]);
                showNewState();
            } else {
                path.add(directory.split(" ")[1]);
                showFile();
            }
        }
    }

    private void showNewState() throws IOException {
        showNewState(parseAnswer(client.executeList(getPath())));
    }

    private void showNewState(@NotNull ArrayList<FileInformation> filesInfo) {
        var gridPane = createGridPane(filesInfo);
        var scene = new Scene(gridPane, 500, 500);

        stage.setScene(scene);
        stage.show();
    }

    private GridPane createGridPane(@NotNull ArrayList<FileInformation> filesInfo) {
        var gridPane = new GridPane();

        for (int i = 0; i <= filesInfo.size(); i++) {
            var row = new RowConstraints();
            row.setPercentHeight(100.0 / (filesInfo.size() + 1));
            gridPane.getRowConstraints().add(row);
        }

        var column = new ColumnConstraints();
        column.setPercentWidth(100.0);
        gridPane.getColumnConstraints().add(column);

        for (int i = 0; i <= filesInfo.size(); i++) {
            var button = new Button();
            if (i == filesInfo.size()) {
                button.setText("...");
            } else {
                button.setText(filesInfo.get(i).toString());
            }

            button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            button.setOnAction(event -> {
                try {
                    changeState(button.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            gridPane.add(button, 0, i);
        }

        return gridPane;
    }

    private void showFile() throws IOException {
        System.out.println(getPath());
        System.out.println(client.executeGet(getPath()));
        var fileContent = getFileContent(client.executeGet(getPath()));
        var gridPane = new GridPane();

        var row1 = new RowConstraints();
        var row2 = new RowConstraints();

        row1.setPercentHeight(90.0);
        row2.setPercentHeight(10.0);
        gridPane.getRowConstraints().add(row1);
        gridPane.getRowConstraints().add(row2);

        var column = new ColumnConstraints();
        column.setPercentWidth(100.0);
        gridPane.getColumnConstraints().add(column);

        var textField = new TextArea();
        textField.setText(fileContent);
        textField.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        var button = new Button();
        button.setText("...");
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button.setOnAction(event -> {
            try {
                changeState("...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gridPane.add(textField, 0, 0);
        gridPane.add(button, 0, 1);

        var scene = new Scene(gridPane, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    private ArrayList<FileInformation> parseAnswer(@NotNull String answer) {
        var filesInfo = new ArrayList<FileInformation>();
        var lines = answer.split(" ");
        if (lines.length % 2 != 1) {
            throw new IllegalArgumentException("Wrong format");
        }

        for (int i = 1; i < lines.length; i += 2) {
            if (lines[i + 1].equals("1")) {
                filesInfo.add(new FileInformation(lines[i], true));
            } else if (lines[i + 1].equals("0")) {
                filesInfo.add(new FileInformation(lines[i], false));
            } else {
                throw new IllegalArgumentException("Wrong format");
            }
        }

        return filesInfo;
    }

    @NotNull
    private String getFileContent(@NotNull String answer) {
        int index = 0;
        while (Character.isDigit(answer.charAt(index))) {
            index++;
        }

        return answer.substring(index + 1);
    }
}
