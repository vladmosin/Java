package com.hse.threadpool;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadPool {
    @NotNull private final MultithreadedQueue<LightFuture<?>> tasks = new MultithreadedQueue<>();
    @NotNull private final ArrayList<Thread> threads = new ArrayList<>();
    @NotNull private final MultithreadedArrayList<LightExecutionException> lightExecutionExceptions =
            new MultithreadedArrayList<>();

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

    @NotNull
    public ArrayList<LightExecutionException> getHappenedException() {
        return lightExecutionExceptions.getArrayList();
    }

    @NotNull
    public <T> LightFuture<T> submit(@NotNull Supplier<T> task) {
        var lightFuture = new TaskHolder<>(task);
        tasks.add(lightFuture);

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
        @Nullable private Supplier<T> supplier;
        @Nullable private T result;
        @Nullable Exception exception;

        @Override
        synchronized public boolean isReady() {
            return supplier == null;
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

        public void makeTask() {
            try {
                if (supplier != null) {
                    synchronized (this) {
                        if (supplier != null) {
                            result = supplier.get();
                            supplier = null;
                        }
                    }
                }
                notifyAll();
            } catch (Exception e) {
                exception = e;
            }
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
