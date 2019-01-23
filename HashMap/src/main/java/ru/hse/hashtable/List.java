package ru.hse.hashtable;

/**
 * Implements List with first auxiliary element
 */
public class List {

    /**
     * Implements element for list
     */
    private static class ListElement {

        /**
         * Next element in list
         */
        private ListElement next;
        private String value;
        private String key;

        public ListElement(ListElement next, String key, String value) throws IllegalArgumentException {
            if (key == null) {
                throw new IllegalArgumentException("key is null");
            }

            if (value == null) {
                throw new IllegalArgumentException("value is null");
            }

            this.next = next;
            this.value = value;
            this.key = key;
        }
    }

    /**
     * Head element of list
     */
    private ListElement head;

    /**
     * Size of list
     */
    private int size;

    /**
     * Construct auxiliary element as a head of list
     */
    public List() {
        head = new ListElement(null, "", "");
    }

    /**
     * Adds new element with given key and value to the beginning of the list
     */
    public void push(String key, String value) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }

        if (value == null) {
            throw new IllegalArgumentException("value is null");
        }

        head.next = new ListElement(head.next, key, value);
        size++;
    }

    /**
     * Deletes first element of list
     * */
    public void pop() throws IllegalStateException {
        if (head.next == null) {
            throw new IllegalStateException("list is empty");
        }

        head.next = head.next.next;
        size--;
    }

    public int size() {
        return size;
    }

    /**
     * Removes element with given key from list
     * @param key Key
     * @return If element was in list returns it's value, otherwise return null
     * */
    public String remove(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }

        ListElement previousElement = findPrevious(key);

        if (previousElement == null) {
            return null;
        } else {
            String returnValue = previousElement.next.value;
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
    public String put(String key, String newValue) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }

        if (newValue == null) {
            throw new IllegalArgumentException("value is null");
        }

        ListElement currentElement = findElement(key);

        if (currentElement != null) {
            String returnValue = currentElement.value;
            currentElement.value = newValue;
            return returnValue;
        } else {
            push(key, newValue);
            return null;
        }
    }

    /**
     * Finds value of element with given key
     * @param key Key
     * @return value of element with given key, or null if such element was not found
     * */
    public String findValue(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }

        ListElement currentElement = findElement(key);

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
    public boolean contains(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }

        return findElement(key) != null;
    }


    /**
     * Adds element to list
     * @param listElement Element, adding in list
     * */
    private void addElement(ListElement listElement) {
        listElement.next = head.next;
        head.next = listElement;
        size++;
    }

    /**
     * Merges to lists, current and given as argument to function
     * @param list Adding list
     * */
    public void merge(List list) throws IllegalArgumentException {
        if (list == null) {
            throw new IllegalArgumentException("given list is null");
        }

        ListElement listElement = list.head.next;
        while (listElement != null) {
            ListElement nextElement = listElement.next;
            addElement(listElement);
            listElement = nextElement;
        }
    }

    /**
     * Transforms list to array of string pairs
     * @return array of key-value pairs
     * */
    public Pair[] toArray() {
        if (size == 0) {
            return null;
        }

        var keyValueArray = new Pair[size];
        ListElement currentElement = head.next;
        int counter = 0;

        while (currentElement != null) {
            keyValueArray[counter] = new Pair(currentElement.key, currentElement.value);
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
    private ListElement findPrevious(String key) {
        ListElement currentElement = head.next;
        ListElement previousElement = head;

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
    private ListElement findElement(String key) {
        ListElement currentElement = findPrevious(key);

        if (currentElement != null) {
            return currentElement.next;
        } else {
            return null;
        }
    }
}
