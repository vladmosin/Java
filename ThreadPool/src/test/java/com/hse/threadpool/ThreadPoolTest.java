package com.hse.threadpool;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class ThreadPoolTest {
    private Double timeTakingCalculation(double startValue) {
        double value = startValue;
        for (int i = 0; i < 100; i++) {
            value = Math.sin(value);
        }

        return value;
    }

    private Integer throwException() {
        throw new IllegalStateException("Just exception");
    }

    private Integer calculateSum() {
        int result = 0;
        for (int i = 0; i < 10; i++) {
            result += i;
        }

        return result;
    }

    @Test
    void testWithOneThread() throws InterruptedException, LightExecutionException {
        var threadPool = new ThreadPool(1);
        Supplier<Double> supplier42 = () -> timeTakingCalculation(42);

        var task1 = threadPool.submit(supplier42);
        var task2 = threadPool.submit(supplier42);

        task1.get();
        task2.get();

        assertTrue(task1.isReady());
        assertTrue(task2.isReady());
    }

    @Test
    void testMultiThreaded() throws InterruptedException, LightExecutionException {
        var threadPool = new ThreadPool(4);
        var tasks = new ArrayList<LightFuture<Double>>();

        for (int i = 0; i < 100; i++) {
            final int j = i;
            tasks.add(threadPool.submit(() -> timeTakingCalculation(j)));
        }


        for (var task : tasks) {
            task.get();
            assertTrue(task.isReady());
        }
    }

    @Test
    void testManyThreads() throws LightExecutionException, InterruptedException {
        var threadPool = new ThreadPool(10000);
        var tasks = new ArrayList<LightFuture<Double>>();

        for (int i = 0; i < 100000; i++) {
            final int j = i;
            tasks.add(threadPool.submit(() -> timeTakingCalculation(j)));
        }


        for (var task : tasks) {
            task.get();
            assertTrue(task.isReady());
        }
    }

    @Test
    void testThrows() {
        var threadPool = new ThreadPool(2);
        var task = threadPool.submit(this::throwException);

        assertThrows(LightExecutionException.class, task::get);
    }

    @Test
    void testShutdown() throws InterruptedException {
        var threadPool = new ThreadPool(4);
        threadPool.shutdown();

        var task = threadPool.submit(() -> timeTakingCalculation(3));
        Thread.sleep(10);

        assertFalse(task.isReady());
    }

    @Test
    void testThenApply() throws LightExecutionException, InterruptedException {
        var threadPool = new ThreadPool(4);
        var task = threadPool.submit(this::calculateSum).thenApply(j -> j + j);

        assertEquals(90, (int)task.get());
    }
}