package com.hse.linkedHashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Implements HashMap
 */
public class LinkedHashMap<K, V> extends AbstractMap<K, V>{
    private List<K, V>[] arrayOfLists;
    private int size;
    private List.ListElement<K, V> first;
    private List.ListElement<K, V> end;

    /**
     * Creates LinkedHashMap with number of lists given as argument
     * */
    @SuppressWarnings("unchecked")
    public LinkedHashMap(int size) {
        arrayOfLists = (List<K, V>[]) Array.newInstance(List.class, size);
        initializeArrayOfLists();
    }

    /**
     * Creates LinkedHashMap with two lists
     * */
    public LinkedHashMap() {
        this(2);
    }

    /**
     * Puts element to LinkedHashMap, if element with such key already exists changes its value
     * @return If element with such key exists returns its value, otherwise returns null
     * */
    @Nullable
    public V put(@NotNull K key, @NotNull V value) throws IllegalArgumentException {
        V outdatedValue = arrayOfLists[getIndex(key)].put(key, value, end);

        if (outdatedValue == null) {
            size++;
            end = arrayOfLists[getIndex(key)].first();
        }

        if (size == 1) {
            first = end;
        }

        if (size > getArraySize()) {
            rebuild();
        }

        return outdatedValue;
    }

    /**
     * Number of elements in HashMap
     */
    public int size() {
        return size;
    }

    /**
     * Find value in HashMap by Key
     * @param key Key
     * @return if element with such key can not be found, returns null, otherwise returns value
     */
    @Nullable
    public V getValue(@NotNull K key) {
        return arrayOfLists[getIndex(key)].findValue(key);
    }

    /**
     * Check existence of element with given key
     * @param key Key
     * @return true if element was found and false otherwise
     * */
    public boolean contains(@NotNull K key) {
        return arrayOfLists[getIndex(key)].contains(key);
    }

    /**
     * Create clear LinkedHashMap with number of lists given as argument
     * */
    @SuppressWarnings("unchecked")
    public void clear(int newSize) {
        arrayOfLists = (List<K, V>[]) Array.newInstance(List.class, newSize);
        size = 0;
        first = null;
        end = null;
        initializeArrayOfLists();
    }

    /**
     * Returns entrySet with iterator() and size() methods
     * */
    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return new AbstractSet<>() {
            @NotNull
            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new Iterator<>() {
                    private List.ListElement<K, V> currentNode = first;
                    private int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index != size;
                    }

                    @Override
                    @NotNull
                    public Entry<K, V> next() {
                        Entry<K, V> result = currentNode;

                        currentNode = currentNode.getAfter();
                        index++;
                        return result;
                    }
                };
            }

            @Override
            public int size() {
                return size;
            }
        };
    }

    /**
     * Removes element from HashMap
     * @param key Key
     * @return value of deleting element or null if element with such key was not found in HashMap
     */
    @Nullable public V removeByKey(@NotNull K key) {
        if (key.equals(first.getKey())) {
            first = first.getAfter();
        }

        V value = arrayOfLists[getIndex(key)].remove(key);

        if (value != null) {
            size--;
        }

        return value;
    }

    /**
     * Doubles size of array of lists by rebuilding whole HashMap
     */
    private void rebuild() {
        int newArraySize = getArraySize() * 2;
        var iterator = entrySet().iterator();
        var list = new ArrayList<Entry<K, V>>();

        while (iterator.hasNext()) {
            list.add(iterator.next());
        }

        clear(newArraySize);
        fillFromList(list);
    }

    /**
     * Fills LinkedHashMap from given list of entries
     * */
    private void fillFromList(@NotNull ArrayList<Entry<K, V>> list) {
        for (var element : list) {
            put(element.getKey(), element.getValue());
        }
    }

    /**
     *  Index of array of lists, where element with such key should locate
     */
    private int getIndex(@NotNull K key) {
        return Math.abs(key.hashCode() % getArraySize());
    }

    /**
     * Size of array
     */
    private int getArraySize () {
        return arrayOfLists.length;
    }

    /**
     * Initialize all lists in LinkedHashMap
     * */
    private void initializeArrayOfLists() {
        for (int i = 0; i < getArraySize(); i++) {
            arrayOfLists[i] = new List<>();
        }
    }
}