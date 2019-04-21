package com.hse.threadpool;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Container with thread-safe adding element to the end and getting element from the start
 * */
public class MultithreadedQueue<T> {
    /** Stores elements */
    private Queue<T> queue = new ArrayDeque<>();

    /** Adds element to the end */
    public synchronized void add(T element) {
        queue.add(element);
        if (queue.size() == 1) {
            notifyAll();
        }
    }

    /** Removes and returns first element */
    public synchronized T get() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        return queue.poll();
    }
}
