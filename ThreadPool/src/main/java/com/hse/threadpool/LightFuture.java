package com.hse.threadpool;

import java.util.function.Function;

/** Interface for objects which are returned from thread pool */
public interface LightFuture<T> {
    boolean isReady();
    T get() throws LightExecutionException, InterruptedException;
    <U> LightFuture<U> thenApply(Function<T, U> changeResult);
    void makeTask();
}
