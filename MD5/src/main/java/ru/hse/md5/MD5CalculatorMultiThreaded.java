package ru.hse.md5;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/** Multithreaded implementation of MD5 */
public class MD5CalculatorMultiThreaded {
    @NotNull private ArrayList<Exception> listExceptions = new ArrayList<Exception>();
    @NotNull private ForkJoinPool pool;

    public MD5CalculatorMultiThreaded(int numberOfThreads) {
        if (numberOfThreads < 1) {
            throw new IllegalArgumentException("Number of threads is less than 1");
        }
        pool = new ForkJoinPool(numberOfThreads);
    }

    /** Returns list of happened exceptions */
    @NotNull public ArrayList<Exception> getExceptions() {
        return listExceptions;
    }

    /** Calculates hash */
    public byte[] calculate(@NotNull File directory) throws MD5Exception {
        var result = pool.invoke(new MD5Task(directory));

        if (!listExceptions.isEmpty()) {
            throw new MD5Exception("Exception happened");
        } else {
            return result;
        }
    }

    /** Task for pool */
    private class MD5Task extends RecursiveTask<byte[]> {
        @NotNull private File directory;

        @Override
        protected byte[] compute() {
            if (!directory.exists()) {
                listExceptions.add(new FileNotFoundException("File was not found"));
            }

            if (!directory.isDirectory()) {
                try {
                    return MD5FileHash.hashFile(directory);
                } catch (NoSuchAlgorithmException | MD5Exception e) {
                    listExceptions.add(e);
                }
            }

            MessageDigest encryptedMessage;
            try {
                encryptedMessage = MessageDigest.getInstance("MD5");
                encryptedMessage.update(directory.getName().getBytes());
                var listFiles = directory.listFiles();

                if (listFiles != null) {
                    var listTasks = new ArrayList<MD5Task>();

                    for (var file : listFiles) {
                        var task = new MD5Task(file);

                        listTasks.add(task);
                        task.fork();
                    }

                    for (var task : listTasks) {
                        encryptedMessage.update(task.join());
                    }

                    return encryptedMessage.digest();
                }
            } catch (NoSuchAlgorithmException e) {
                listExceptions.add(e);
            }

            return new byte[0]; // If exception happened
        }

        private MD5Task(@NotNull File directory) {
            this.directory = directory;
        }
    }
}
