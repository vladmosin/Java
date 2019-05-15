package ru.hse.md5;

import org.jetbrains.annotations.NotNull;

public class MD5Exception extends Exception {
    @NotNull private String message;

    public MD5Exception(@NotNull String message) {
        this.message = message;
    }

    @NotNull public String getMessage() {
        return message;
    }
}
