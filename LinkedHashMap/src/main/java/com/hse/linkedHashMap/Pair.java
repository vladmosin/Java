package com.hse.linkedHashMap;

import org.jetbrains.annotations.NotNull;

/**
 * Implements key-value pair
 * */
public class Pair<K, V> {
    @NotNull private K key;
    @NotNull private V value;

    public Pair(@NotNull K key, @NotNull V value) {
        this.key = key;
        this.value = value;
    }

    @NotNull
    public K key() {
        return key;
    }

    @NotNull
    public V value() {
        return value;
    }
}
