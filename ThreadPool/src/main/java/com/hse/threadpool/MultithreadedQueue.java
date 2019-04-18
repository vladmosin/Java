package com.hse.threadpool;

import java.util.ArrayDeque;
import java.util.Queue;

public class MultithreadedQueue<T> {
    private Queue<T> queue = new ArrayDeque<>();

    public synchronized void add(T element) {
        queue.add(element);
        if (queue.size() == 1) {
            notifyAll();
        }
    }

    public synchronized T get() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        return queue.poll();
    }
}
