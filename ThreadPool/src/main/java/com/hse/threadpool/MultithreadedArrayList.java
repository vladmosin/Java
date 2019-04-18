package com.hse.threadpool;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MultithreadedArrayList<T> {
    @NotNull private ArrayList<T> list = new ArrayList<>();

    public synchronized void add(T element) {
        list.add(element);
    }

    @NotNull
    public synchronized ArrayList<T> getArrayList() {
        return list;
    }
}
