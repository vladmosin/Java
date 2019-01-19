package ru.hse;

/**
 * Implements List with first auxiliary element
 */
public class List {

    /**
     * Implements element for list
     */
    private class ListElement {

        /**
         * Next element in list
         */
        private ListElement next;
        /**
         * Element's value
         */
        private String value;
        /**
         * Element's key
         */
        private String key;

        /**
         * Constructor
         * @param next Next element
         * @param key Key of new element
         * @param value Value of new element
         */
        public ListElement(ListElement next, String key, String value) {
            this.next = next;
            this.value = value;
            this.key = key;
        }
    }

    /**
     * Construct auxiliary element as a head of list
     */
    public List() {
        head = new ListElement(null, "", "");
        size = 0;
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
     * Adds new element with given key and value to the beginning of the list
     * @param key Key
     * @param value Value
     */
    public void push(String key, String value) {
        head.next = new ListElement(head.next, key, value);
        size++;
    }

    /**
     * Deletes first element of list
     * */
    public void pop() {
        head.next = head.next.next;
        size--;
    }

    /**
     * Returns list size
     * @return list size
     * */
    public int size() {
        return size;
    }

    /**
     * Removes element with given key from list
     * @param key Key
     * @return If element was in list returns it's value, otherwise return null
     * */
    public String remove(String key) {
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
     * @return Returns previous value, which was in list before, or null if there was no element with such key
     * */
    public String put(String key, String newValue) {
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
     * Finds element which is in list before element with given key
     * @param key Key
     * @return Returns element which is before the element with given key, or null if such element was not found
     * */
    private ListElement findPrevious(String key) {
        ListElement currentElement = head.next, previousElement = head;

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
     * @return Returns element with given key, or null if such element was not found
     * */
    private ListElement findElement(String key) {
        ListElement currentElement = findPrevious(key);

        if (currentElement != null) {
            return currentElement.next;
        } else {
            return null;
        }
    }

    /**
     * Finds value of element with given key
     * @param key Key
     * @return Returns value of element with given key, or null if such element was not found
     * */
    public String findValue(String key) {
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
     * @return If list contains element with such key, returns true, otherwise returns false
     * */
    public boolean contains(String key) {
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
    public void merge(List list) {
        ListElement listElement = list.head.next;
        while (listElement != null) {
            ListElement nextElement = listElement.next;
            addElement(listElement);
            listElement = nextElement;
        }
    }

    /**
     * Transforms list to array of string pairs
     * @return Returns array of key-value pairs
     * */
    public Pair[] toArray() {
        if (size == 0) {
            return null;
        } else {
            Pair[] keyValueArray = new Pair[size];
            ListElement currentElement = head.next;
            int counter = 0;

            while (currentElement != null) {
                keyValueArray[counter] = new Pair(currentElement.key, currentElement.value);
                counter++;
                currentElement = currentElement.next;
            }

            return keyValueArray;
        }
    }
}
