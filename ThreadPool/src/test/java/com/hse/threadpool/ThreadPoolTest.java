package com.hse.threadpool;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreadPoolTest {
    private Double timeTakingCalculation(double startValue) {
        double value = startValue;
        for (int i = 0; i < 100; i++) {
            value = Math.sin(value);
        }

        return value;
    }

    private void throwException() {
        throw new IllegalStateException("Just exception");
    }

    @Test
    void testWithOneThread() throws InterruptedException {
        var threadPool = new ThreadPool(1);
        Supplier<Double> supplier42 = () -> timeTakingCalculation(42);

        var task1 = threadPool.submit(supplier42);
        var task2 = threadPool.submit(supplier42);

        Thread.sleep(10);
        threadPool.shutdown();

        assertTrue(task1.isReady());
        assertTrue(task2.isReady());
    }

    @Test
    void testMultiThreaded() throws InterruptedException {
        var threadPool = new ThreadPool(4);
        var tasks = new ArrayList<LightFuture<Double>>();

        for (int i = 0; i < 100; i++) {
            final int j = i;
            tasks.add(threadPool.submit(() -> timeTakingCalculation(j)));
        }

        Thread.sleep(50);
        for (var task : tasks) {
            assertTrue(task.isReady());
        }
    }
}