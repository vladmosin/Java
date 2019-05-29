package ru.hsespb.ticktacktoe;

import org.jetbrains.annotations.NotNull;

public class InputListener {
    private UserInputProcessor userInputProcessor;
    private boolean active;

    public void start(@NotNull UserInputProcessor userInputProcessor) {
        this.userInputProcessor = userInputProcessor;
        active = true;
    }

    public void stop() {
        active = false;
    }

    public void onBoardButtonClick(int x, int y) throws InterruptedException {
        if (!active) {
            return;
        }

        userInputProcessor.processClick(x, y);
    }
}
