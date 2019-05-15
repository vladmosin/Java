package com.hse.threadpool;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Container with thread-safe adding element to the end and getting element from the start
 * */
public class MultithreadedQueue<T> {
    /** Stores elements */
    @NotNull private final Queue<T> queue = new ArrayDeque<>();

    /** Adds element to the end */
    public void add(@NotNull T element) {
        queue.add(element);
    }

    /** Returns true if Queue is empty */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /** Removes and returns first element */
    public T get() throws InterruptedException {
        return queue.poll();
    }
}
