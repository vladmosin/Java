package ru.hsespb.ticktacktoe;

import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/** Stores start state of game */
public class PlayGameState {
    /** Listens for buttons clicks */
    private InputListener inputListener;

    /** Stores board with cells */
    private CheckeredButtonBoard board;

    /** Stores game viewer */
    private PlayView playView;

    /** Stores user processor */
    private UserInputProcessor userInputProcessor;

    /** Invokes game */
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
