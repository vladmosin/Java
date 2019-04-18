package com.hse.threadpool;

public class LightExecutionException extends Exception {
    private String message;

    public LightExecutionException() {}

    public LightExecutionException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
