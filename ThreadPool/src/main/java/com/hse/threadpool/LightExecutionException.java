package com.hse.threadpool;

import org.jetbrains.annotations.NotNull;

public class LightExecutionException extends Exception {
    private String message;

    public LightExecutionException() {}

    public LightExecutionException(@NotNull String message) {
        this.message = message;
    }

    public LightExecutionException(@NotNull Exception exception) {
        message = exception.getMessage();
    }

    public String getMessage() {
        return message;
    }
}
