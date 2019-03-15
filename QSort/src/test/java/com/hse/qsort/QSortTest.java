package com.hse.qsort;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class QSortTest {
    private void fillList(int seed, int size, @NotNull ArrayList<Integer> list) {
        var random = new Random(seed);

        random.ints(size).forEach(list::add);
    }

    private boolean isSorted(@NotNull ArrayList<Integer> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) < list.get(i - 1)) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void testExtraSmall() throws InterruptedException {
        var multiThreaded = new ArrayList<Integer>();
        fillList(1, 4, multiThreaded);

        var singleThreaded = new ArrayList<Integer>(multiThreaded);

        long startTime = System.nanoTime();
        QSort.qsort(multiThreaded, true);
        long endTime = System.nanoTime();
        System.out.println("ExtraSmall, multiThreaded: " + ((endTime - startTime) / 1e6) + " ms.");

        startTime = System.nanoTime();
        QSort.qsort(singleThreaded, false);
        endTime = System.nanoTime();
        System.out.println("ExtraSmall, singleThreaded: " + ((endTime - startTime) / 1e6) + " ms.");

        assertTrue(isSorted(multiThreaded));
        assertTrue(isSorted(singleThreaded));
    }
    // MultiThreaded - 18.1 ms
    // SingleThreaded - 0.088 ms

    @Test
    public void testSmall() throws InterruptedException {
        var multiThreaded = new ArrayList<Integer>();
        fillList(1, 50, multiThreaded);

        var singleThreaded = new ArrayList<Integer>(multiThreaded);

        long startTime = System.nanoTime();
        QSort.qsort(multiThreaded, true);
        long endTime = System.nanoTime();
        System.out.println("Small, multiThreaded: " + ((endTime - startTime) / 1e6) + " ms.");

        startTime = System.nanoTime();
        QSort.qsort(singleThreaded, false);
        endTime = System.nanoTime();
        System.out.println("Small, singleThreaded: " + ((endTime - startTime) / 1e6) + " ms.");

        assertTrue(isSorted(multiThreaded));
        assertTrue(isSorted(singleThreaded));
    }
    // MultiThreaded - 13.67 ms
    // SingleThreaded - 0.46 ms

    @Test
    public void testMedium() throws InterruptedException {
        var multiThreaded = new ArrayList<Integer>();
        fillList(1, 1000, multiThreaded);

        var singleThreaded = new ArrayList<Integer>(multiThreaded);

        long startTime = System.nanoTime();
        QSort.qsort(multiThreaded, true);
        long endTime = System.nanoTime();
        System.out.println("Medium, multiThreaded: " + ((endTime - startTime) / 1e3) + " mcs.");

        startTime = System.nanoTime();
        QSort.qsort(singleThreaded, false);
        endTime = System.nanoTime();
        System.out.println("Medium, singleThreaded: " + ((endTime - startTime) / 1e3) + " mcs.");


        assertTrue(isSorted(multiThreaded));
        assertTrue(isSorted(singleThreaded));
    }
    // MultiThreaded - 1.629 ms
    // SingleThreaded - 0.859 ms

    @Test
    public void testBig() throws InterruptedException {
        var multiThreaded = new ArrayList<Integer>();
        fillList(1, 100_000, multiThreaded);

        var singleThreaded = new ArrayList<Integer>(multiThreaded);

        long startTime = System.nanoTime();
        QSort.qsort(multiThreaded, true);
        long endTime = System.nanoTime();
        System.out.println("Big, multiThreaded: " + ((endTime - startTime) / 1e6) + " ms.");

        startTime = System.nanoTime();
        QSort.qsort(singleThreaded, false);
        endTime = System.nanoTime();
        System.out.println("Big, singleThreaded: " + ((endTime - startTime) / 1e6) + " ms.");

        assertTrue(isSorted(multiThreaded));
        assertTrue(isSorted(singleThreaded));
    }
    // MultiThreaded - 0.387 sec
    // SingleThreaded - 0.359 sec

    @Test
    public void testExtraBig() throws InterruptedException {
        var multiThreaded = new ArrayList<Integer>();
        fillList(1, 10_000_000, multiThreaded);

        var singleThreaded = new ArrayList<Integer>(multiThreaded);

        long startTime = System.nanoTime();
        QSort.qsort(multiThreaded, true);
        long endTime = System.nanoTime();
        System.out.println("ExtraBig, multiThreaded: " + ((endTime - startTime) / 1e9) + " s.");

        startTime = System.nanoTime();
        QSort.qsort(singleThreaded, false);
        endTime = System.nanoTime();
        System.out.println("ExtraBig, singleThreaded: " + ((endTime - startTime) / 1e9) + " s.");

        assertTrue(isSorted(multiThreaded));
        assertTrue(isSorted(singleThreaded));
    }
    // MultiThreaded - 14.9 sec
    // SingleThreaded - 19.3 sec
}