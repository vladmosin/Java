package com.hse.qsort;

import org.jetbrains.annotations.NotNull;

import java.sql.Array;
import java.util.*;
import java.util.concurrent.*;

/** Implements quickSort */
public class QSort {
    @NotNull private static final Random random = new Random();

    /** Information about array splitting */
    private static class SplitInfo {
        private int left;
        private int right;

        private SplitInfo(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }

    /** Stores partition results */
    private static class Partition<T> {
        @NotNull private final ArrayList<T> listLess;
        @NotNull private final ArrayList<T> listEquals;
        @NotNull private final ArrayList<T> listGreater;

        private Partition(@NotNull ArrayList<T> listLess, @NotNull ArrayList<T> listEquals,
                          @NotNull ArrayList<T> listGreater) {
            this.listEquals = listEquals;
            this.listLess = listLess;
            this.listGreater = listGreater;
        }
    }

    /** QuickSort Algorithm */
    public static <T extends Comparable<? super T>> void qsort(@NotNull Collection<T> collection,
                                                               boolean isMultiThreaded)
            throws InterruptedException {
        qsort(collection, createComparator(), isMultiThreaded);
    }


    /** QuickSort Algorithm */
    public static <T> void qsort(@NotNull Collection<T> collection, @NotNull Comparator<? super T> comparator,
                                 boolean isMultiThreaded)
            throws InterruptedException {
        var list = new ArrayList<T>(collection);
        var pool = new ForkJoinPool();

        if (isMultiThreaded) {
            pool.invoke(new QuickSort<>(0, list.size(), list, comparator, isMultiThreaded));
        } else {
            (new QuickSort<>(0, list.size(), list, comparator, isMultiThreaded)).compute();
        }

        collection.clear();
        collection.addAll(list);
    }

    /** Creates comparator */
    @SuppressWarnings("unchecked")
    private static <T> Comparator<T> createComparator() {
        return (o1, o2) -> ((Comparable<? super T>)o1).compareTo(o2);
    }

    /** Class for launching in threadPool */
    private static class QuickSort<T> extends RecursiveAction {
        @NotNull private ArrayList<T> list;
        private int from;
        private int to;
        private boolean isMultithreaded;
        private Comparator<? super T> comparator;

        public QuickSort(int from, int to, @NotNull ArrayList<T> list, @NotNull Comparator<? super T> comparator,
                         boolean isMultithreaded) {
            this.from = from;
            this.to = to;
            this.list = list;
            this.isMultithreaded = isMultithreaded;
            this.comparator = comparator;
        }

        /** Calculates right order for list */
        @Override
        public void compute() {
            if (to - from <= 1) {
                return;
            }

            T value = chooseValue();
            var splitInfo = splitByValue(value);

            if (isMultithreaded) {
                invokeAll(new QuickSort<>(from, splitInfo.left, list, comparator, isMultithreaded),
                          new QuickSort<>(splitInfo.right, to, list, comparator, isMultithreaded));
            } else {
                (new QuickSort<>(from, splitInfo.left, list, comparator, isMultithreaded)).compute();
                (new QuickSort<>(splitInfo.right, to, list, comparator, isMultithreaded)).compute();
            }
        }

        /** Chooses value from list */
        @NotNull private T chooseValue() {
            return list.get(random.nextInt(to - from) + from);
        }

        /** Splits list into three parts */
        private SplitInfo splitByValue(@NotNull T value) {
            var listLess = new ArrayList<T>();
            var listGreater = new ArrayList<T>();
            var listEquals = new ArrayList<T>();

            for (int i = from; i < to; i++) {
                var element = list.get(i);
                if (comparator.compare(element, value) < 0) {
                    listLess.add(element);
                } else if (comparator.compare(element, value) > 0) {
                    listGreater.add(element);
                } else {
                    listEquals.add(element);
                }
            }

            return fillFromGivenPartition(new Partition<>(listLess, listEquals, listGreater));
        }

        /** Fills main list from given partition */
        @NotNull
        private SplitInfo fillFromGivenPartition(@NotNull Partition<T> partition) {
            var temporaryList = new ArrayList<>(partition.listLess);
            temporaryList.addAll(partition.listEquals);
            temporaryList.addAll(partition.listGreater);

            for (int i = from; i < to; i++) {
                list.set(i, temporaryList.get(i - from));
            }

            int lessSize = partition.listLess.size();
            int equalsSize = partition.listEquals.size();

            return new SplitInfo(lessSize + from, lessSize + equalsSize + from);
        }
    }
}