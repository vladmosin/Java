package com.hse.threadpool;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadPool {
    @NotNull private final MultithreadedQueue<LightFuture<?>> tasks = new MultithreadedQueue<>();
    @NotNull private final ArrayList<Thread> threads = new ArrayList<>();
    @NotNull private final ArrayList<LightExecutionException> lightExecutionExceptions = new ArrayList<>();

    public ThreadPool(int numberOfThreads) {
        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(new TaskTaker()));
            threads.get(i).start();
        }
    }

    public void shutdown() {
        for (var thread : threads) {
            thread.interrupt();
        }
    }

    public <T> LightFuture<T> submit(@NotNull Supplier<T> task) {
        var lightFuture = new TaskHolder<>(task);
        synchronized (tasks) {
            tasks.add(lightFuture);
            notify();
        }

        return lightFuture;
    }

    private class TaskTaker implements Runnable {
        @Override
        public void run() {
            while(!Thread.interrupted()) {
                try {
                    tasks.get().get();
                } catch (LightExecutionException e) {
                    lightExecutionExceptions.add(e);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    private class TaskHolder<T> implements LightFuture<T> {
        @Nullable private Supplier<T> supplier;
        @Nullable private T result;

        @Override
        synchronized public boolean isReady() {
            return supplier == null;
        }

        @Override
        public T get() throws LightExecutionException {
            try {
                if (supplier != null) {
                    synchronized (this) {
                        if (supplier != null) {
                            result = supplier.get();
                            supplier = null;
                        }
                    }
                }
                return result;
            } catch (Exception e) {
                throw new LightExecutionException(e.getMessage());
            }
        }

        @Override
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

        public TaskHolder(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
        }
    }

}
