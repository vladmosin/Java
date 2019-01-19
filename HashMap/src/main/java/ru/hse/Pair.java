package ru.hse;

/**
 * Implements key-value pair
 * */
public class Pair {

    /**
     * Key
     * */
    private String key;
    /**
     * Value
     * */
    private String value;

    /**
     * Constructor
     * @param key Key
     * @param value Value
     * */
    public Pair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns key
     * */
    public String key() {
        return key;
    }

    /**
     * Returns value
     * */
    public String value() {
        return value;
    }
}
