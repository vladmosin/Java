package ru.hsespb.ticktacktoe;

import org.jetbrains.annotations.NotNull;

/** Listens for buttons clicks */
public class InputListener {
    /** Stores user processor */
    private UserInputProcessor userInputProcessor;
    private boolean active;

    public void start(@NotNull UserInputProcessor userInputProcessor) {
        this.userInputProcessor = userInputProcessor;
        active = true;
    }

    public void stop() {
        active = false;
    }

    /** Processes board click */
    public void onBoardButtonClick(int x, int y) throws InterruptedException {
        if (!active) {
            return;
        }

        userInputProcessor.processClick(x, y);
    }
}
