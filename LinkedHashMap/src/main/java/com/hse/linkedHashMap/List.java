package com.hse.linkedHashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Implements List with first auxiliary element
 */
public class List<K, V> {

    /**
     * Implements element for list
     */
    protected static class ListElement<K, V> implements Map.Entry<K, V> {

        /**
         * Next element in list
         */
        private ListElement<K, V> next;
        private V value;
        private K key;

        /**
         * Element which was added exactly after current and still in map
         * */
        private ListElement<K, V> after;

        /**
         * Element which was added exactly before current and still in map
         * */
        private ListElement<K, V> before;

        public ListElement(ListElement<K, V> next, K key, V value, ListElement<K, V> after, ListElement<K, V> before) {
            this.next = next;
            this.value = value;
            this.key = key;
            this.after = after;
            this.before = before;
        }

        @Nullable
        protected ListElement<K, V> getAfter() {
            return after;
        }

        @Nullable
        protected ListElement<K, V> getBefore() {
            return before;
        }

        @Override
        @Nullable
        public K getKey() {
            return key;
        }

        @Override
        @Nullable
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            var result = this.value;

            this.value = value;
            return result;
        }
    }

    /**
     * Head element of list
     */
    @NotNull private ListElement<K, V> head;

    /**
     * Size of list
     */
    private int size;

    /**
     * Construct auxiliary element as a head of list
     */
    public List() {
        head = new ListElement<>(null, null, null, null, null);
    }

    /**
     * Adds new element with given key and value to the beginning of the list
     */
    public void push(@NotNull K key, @NotNull V value, @Nullable ListElement<K, V> lastElement) {
        head.next = new ListElement<K, V>(head.next, key, value, null, lastElement);
        size++;

        if (lastElement != null) {
            lastElement.after = head.next;
        }
    }

    /**
     * Deletes first element of list
     * */
    public void pop() throws IllegalStateException {
        if (head.next == null) {
            throw new IllegalStateException("list is empty");
        }

        changeBeforeAfterLinks(head.next);

        head.next = head.next.next;
        size--;
    }

    /**
     * Before removing element from list
     * */
    private void changeBeforeAfterLinks(@NotNull ListElement<K, V> node) {
        var beforeElement = node.before;
        var afterElement = node.after;

        if (beforeElement != null) {
            beforeElement.after = afterElement;
        }

        if (afterElement != null) {
            afterElement.before = beforeElement;
        }
    }

    public int size() {
        return size;
    }

    /**
     * Removes element with given key from list
     * @param key Key
     * @return If element was in list returns it's value, otherwise return null
     * */
    @Nullable public V remove(@NotNull K key) {
        var previousElement = findPrevious(key);

        if (previousElement == null) {
            return null;
        } else {
            V returnValue = previousElement.next.value;

            changeBeforeAfterLinks(previousElement.next);
            previousElement.next = previousElement.next.next;
            return returnValue;
        }
    }

    /**
     * Puts element with given key and value
     * @param key Key
     * @param newValue New value of element with given key
     * @return previous value, which was in list before, or null if there was no element with such key
     * */
    @Nullable public V put(@NotNull K key, @NotNull V newValue, ListElement<K, V> lastElement) {
        var currentElement = findElement(key);

        /*Put do not change position in order of existing element*/
        if (currentElement != null) {
            V returnValue = currentElement.value;
            currentElement.value = newValue;
            return returnValue;
        } else {
            push(key, newValue, lastElement);
            return null;
        }
    }

    /**
     * Finds value of element with given key
     * @param key Key
     * @return value of element with given key, or null if such element was not found
     * */
    @Nullable
    public V findValue(@NotNull K key) {
        var currentElement = findElement(key);

        if (currentElement != null) {
            return currentElement.value;
        } else {
            return null;
        }
    }

    /**
     * Checks, does list have element with such key
     * @param key Key
     * @return if list contains element with such key, returns true, otherwise returns false
     * */
    public boolean contains(@NotNull K key) {
        return findElement(key) != null;
    }


    /**
     * Adds element to list
     * @param listElement Element, adding in list
     * */
    private void addElement(@NotNull ListElement<K, V> listElement) {
        listElement.next = head.next;
        head.next = listElement;
        size++;
    }

    /**
     * Merges to lists, current and given as argument to function
     * @param list Adding list
     * */
    public void merge(@NotNull List<K, V> list) {
        var listElement = list.head.next;
        while (listElement != null) {
            var nextElement = listElement.next;
            addElement(listElement);
            listElement = nextElement;
        }
    }

    /**
     * Transforms list to array of string pairs
     * @return array of key-value pairs
     * */
    @Nullable
    public Pair[] toArray() {
        if (size == 0) {
            return null;
        }

        var keyValueArray = new Pair[size];
        var currentElement = head.next;
        int counter = 0;

        while (currentElement != null) {
            keyValueArray[counter] = new Pair<K, V>(currentElement.key, currentElement.value);
            counter++;
            currentElement = currentElement.next;
        }

        return keyValueArray;
    }

    /**
     * Finds element which is in list before element with given key
     * @param key Key
     * @return element which is before the element with given key, or null if such element was not found
     * */
    @Nullable
    private ListElement<K, V> findPrevious(@NotNull K key) {
        var currentElement = head.next;
        var previousElement = head;

        while (currentElement != null) {
            if (currentElement.key.equals(key)) {
                return previousElement;
            } else {
                previousElement = currentElement;
                currentElement = currentElement.next;
            }
        }

        return null;
    }

    /**
     * Finds element with given key
     * @param key Key
     * @return element with given key, or null if such element was not found
     * */
    @Nullable
    private ListElement<K, V> findElement(@NotNull K key) {
        var currentElement = findPrevious(key);

        if (currentElement != null) {
            return currentElement.next;
        } else {
            return null;
        }
    }

    @Nullable
    public ListElement<K, V> first() {
        return head.next;
    }
}
