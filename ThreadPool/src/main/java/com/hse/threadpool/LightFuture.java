package com.hse.threadpool;

import java.util.function.Function;

/** Interface for objects which are returned from thread pool */
public interface LightFuture<T> {
    /** Checks, if object ready */
    boolean isReady();

    /** Returns result */
    T get() throws LightExecutionException, InterruptedException;

    /** Applies function to result */
    <U> LightFuture<U> thenApply(Function<? super T, U> changeResult);
}
