package com.hse.myjunit;

import org.jetbrains.annotations.NotNull;

public class FailedBeforeTestRunsException extends Exception {
    @NotNull private String message;

    public FailedBeforeTestRunsException(@NotNull String message) {
        this.message = message;
    }

    @NotNull
    public String getMessage() {
        return message;
    }
}
