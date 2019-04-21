package com.hse.threadpool;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Exception for problems with LightFuture */
public class LightExecutionException extends Exception {
    @Nullable private String message;

    /** Constructs LightExecutionException with same message as in given exception */
    public LightExecutionException(@NotNull Exception exception) {
        message = exception.getMessage();
    }

    /** Returns message */
    @Nullable public String getMessage() {
        return message;
    }
}
