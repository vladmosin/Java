package com.hse.qsort;

import org.jetbrains.annotations.NotNull;

import java.sql.Array;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QSort {
    @NotNull private static final Random random = new Random();
    //@NotNull private static final ExecutorService threadPool = Executors.newFixedThreadPool(4);

    private static class Partition<T> {
        private final ArrayList<T> listLess;
        private final ArrayList<T> listEquals;
        private final ArrayList<T> listGreater;

        private Partition(@NotNull ArrayList<T> listLess, @NotNull ArrayList<T> listEquals,
                          @NotNull ArrayList<T> listGreater) {
            this.listEquals = listEquals;
            this.listLess = listLess;
            this.listGreater = listGreater;
        }
    }

    public static <T extends Comparable<? super T>> void qsort(@NotNull Collection<T> collection)
            throws InterruptedException {
        qsort(collection, createComparator());
    }



    public static <T> void qsort(@NotNull Collection<T> collection, @NotNull Comparator<? super T> comparator)
            throws InterruptedException {
        var list = new ArrayList<T>(collection);

        multithreadedQSort(list, comparator);
        collection.clear();
        collection.addAll(list);
    }

    @SuppressWarnings("unchecked")
    private static <T> Comparator<T> createComparator() {
        return (o1, o2) -> ((Comparable<? super T>)o1).compareTo(o2);
    }
    private static <T> void multithreadedQSort(@NotNull ArrayList<T> list,
                                               @NotNull Comparator<? super T> comparator) throws InterruptedException {
        if (list.size() == 0) {
            return;
        }

        T value = chooseValue(list);
        var partition = splitByValue(list, value, comparator);

        Thread thread1 = new Thread(() -> {
            try {
                multithreadedQSort(partition.listLess, comparator);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            try {
                multithreadedQSort(partition.listGreater, comparator);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread2.start();

        thread1.join();
        thread2.join();
        merge(list, partition);
    }

    private static <T> void merge(ArrayList<T> list, Partition<T> partition) {
        list.clear();

        list.addAll(partition.listLess);
        list.addAll(partition.listEquals);
        list.addAll(partition.listGreater);
    }

    private static <T> T chooseValue(ArrayList<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    private static <T> Partition<T> splitByValue(@NotNull ArrayList<T> list, @NotNull T value,
                                           @NotNull Comparator<? super T> comparator) {
        var listLess = new ArrayList<T>();
        var listGreater = new ArrayList<T>();
        var listEquals = new ArrayList<T>();

        for (var element : list) {
            if (comparator.compare(element, value) < 0) {
                listLess.add(element);
            } else if (comparator.compare(element, value) > 0) {
                listGreater.add(element);
            } else {
                listEquals.add(element);
            }
        }

        return new Partition<T>(listLess, listEquals, listGreater);
    }

    public static void main(String[] args) throws InterruptedException {
        var list = new ArrayList<Integer>();
        for (int i = 0; i < 10000; i++) {
            list.add(random.nextInt());
        }

        qsort(list);
        for (int element : list) {
            System.out.print(element + " ");
        }
    }
}