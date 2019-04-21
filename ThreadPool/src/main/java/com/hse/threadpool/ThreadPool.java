package com.hse.threadpool;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadPool {
    /** Stores tasks */
    @NotNull private final MultithreadedQueue<LightFuture<?>> tasks = new MultithreadedQueue<>();

    /** Stores threads */
    @NotNull private final ArrayList<Thread> threads = new ArrayList<>();

    /** False if thread pool is open for new tasks */
    private volatile boolean isShutdown = false;

    /** Creates thread pool with given number of threads */
    public ThreadPool(int numberOfThreads) {
        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(new TaskTaker()));
            threads.get(i).start();
        }
    }

    /** Stops applying tasks */
    public void shutdown() {
        isShutdown = true;
        for (var thread : threads) {
            thread.interrupt();
        }
    }

    /** Submits task to thread pool
     * @return container with functions for getting result
     * */
    @NotNull
    public <T> LightFuture<T> submit(@NotNull Supplier<T> task) {
        var lightFuture = new TaskHolder<>(task);
        if (!isShutdown) {
            tasks.add(lightFuture);
        }

        return lightFuture;
    }

    /** Implements thread for thread pool */
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

    /** Implementation of LightFuture interface */
    public class TaskHolder<T> implements LightFuture<T> {
        /** Stores supplier */
        @NotNull private final Supplier<T> supplier;

        /** Stores result of computation */
        @Nullable private T result;

        /** Stores exception if it was thrown */
        @Nullable Exception exception;

        private volatile boolean isReady = false;

        /** Returns true if computation ends */
        @Override
        public boolean isReady() {
            return isReady;
        }

        /** Returns result of computation */
        @Override
         synchronized public T get() throws InterruptedException, LightExecutionException {
            while (!isReady()) {
                wait();
            }

            if (exception != null) {
                throw new LightExecutionException(exception);
            }

            return result;
        }

        /** Compute task */
        @Override
         synchronized public void makeTask() {
            try {
                if (!isReady) {
                    result = supplier.get();
                    isReady = true;
                }
            } catch (Exception e) {
                exception = e;
                isReady = true;
            } finally {
                notifyAll();
            }
        }


        /** Applies given function to result result of current computation */
        @Override
        @NotNull
        synchronized public <U> LightFuture<U> thenApply(@NotNull Function<T, U> applyingFunction) {
            var task = new Supplier<U>() {
                @Override
                public U get() {
                    try {
                        while (!isReady()) {
                            wait();
                        }
                        return applyingFunction.apply(TaskHolder.this.get());
                    } catch (Exception exception) {
                        throw new RuntimeException("Cannot apply function", exception);
                    }
                }
            };
            return ThreadPool.this.submit(task);
        }

        /** Creates a task with given supplier */
        private TaskHolder(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
        }
    }
}
