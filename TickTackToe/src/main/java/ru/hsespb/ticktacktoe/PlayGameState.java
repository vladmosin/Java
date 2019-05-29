package ru.hsespb.ticktacktoe;

import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class PlayGameState {
    private InputListener inputListener;
    private CheckeredButtonBoard board;
    private PlayView playView;
    private UserInputProcessor userInputProcessor;

    public void invokeState(@NotNull Stage stage, int size) {
        inputListener = new InputListener();
        board = new CheckeredButtonBoard(size, size);
        userInputProcessor = new UserInputProcessor(this, board);

        playView = new PlayView(stage, size, size);
        board.setPlayView(playView);
        playView.createBoard(inputListener);
        startGame();
    }

    private void startGame() {
        playView.show();
        inputListener.start(userInputProcessor);
    }

    public void finishGame() {
        inputListener.stop();
        playView.showResult("You win!");
    }
}
