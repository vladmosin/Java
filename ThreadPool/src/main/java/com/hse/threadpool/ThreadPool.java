package com.hse.threadpool;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadPool {
    @NotNull private final MultithreadedQueue<LightFuture<?>> tasks = new MultithreadedQueue<>();
    @NotNull private final ArrayList<Thread> threads = new ArrayList<>();
    private volatile boolean isShutdown = false;

    public ThreadPool(int numberOfThreads) {
        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(new TaskTaker()));
            threads.get(i).start();
        }
    }

    public synchronized void shutdown() {
        isShutdown = true;
        for (var thread : threads) {
            thread.interrupt();
        }
    }

    @NotNull
    public <T> LightFuture<T> submit(@NotNull Supplier<T> task) {
        var lightFuture = new TaskHolder<>(task);
        if (!isShutdown) {
            tasks.add(lightFuture);
        }

        return lightFuture;
    }

    private class TaskTaker implements Runnable {
        @Override
        public void run() {
            while(!Thread.interrupted()) {
                try {
                    tasks.get().makeTask();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public class TaskHolder<T> implements LightFuture<T> {
        @NotNull private Supplier<T> supplier;
        @Nullable private T result;
        @Nullable Exception exception;
        private boolean isReady = false;

        @Override
        synchronized public boolean isReady() {
            return isReady;
        }

        public synchronized T get() throws InterruptedException, LightExecutionException {
            while (!isReady()) {
                wait();
            }

            if (exception != null) {
                throw new LightExecutionException(exception);
            }

            return result;
        }

        synchronized public void makeTask() {
            try {
                if (!isReady) {
                    result = supplier.get();
                    isReady = true;
                }
            } catch (Exception e) {
                exception = e;
                isReady = true;
            }
            notifyAll();
        }

        @Override
        @NotNull
        synchronized public <U> LightFuture<U> thenApply(@NotNull Function<T, U> applyingFunction) {
            var task = new Supplier<U>() {
                @Override
                public U get() {
                    try {
                        return applyingFunction.apply(TaskHolder.this.get());
                    } catch (Exception exception) {
                        throw new RuntimeException("Cannot apply function", exception);
                    }
                }
            };
            return ThreadPool.this.submit(task);
        }

        private TaskHolder(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
        }
    }
}
