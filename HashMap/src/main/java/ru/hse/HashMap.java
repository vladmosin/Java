package ru.hse;

/**
 * Implements HashMap
 */
public class HashMap {

    /**
     * Stores Lists with elements
     */
    private List[] arrayOfLists;
    /**
     * Stores number of elements in HasMap
     */
    private int size;

    /**
     * Constructor
     * The start size of array is 2
     */
    public HashMap() {
        arrayOfLists = new List[2];
        size = 0;
        initializeArrayOfLists();
    }

    /**
     * Size of array
     * @return Returns size of array
     */
    private int getArraySize () {
        return arrayOfLists.length;
    }

    /**
     * Puts value in HashMap
     * @param key Key
     * @param value Value
     * @return Returns what was by key in HashMap before. If there was nothing returns null
     */
    public String put(String key, String value) {
        String outdatedValue = arrayOfLists[getIndex(key)].put(key, value);
        if (outdatedValue == null) {
            size++;
        }
        if (size > getArraySize()) {
            rebuild();
        }
        return outdatedValue;
    }

    /**
     * Number of elements in HashMap
     * @return Returns number of elements in HashMap
     */
    public int size() {
        return size;
    }

    /**
     *  Index of array of lists, where element with such key should locate
     * @param key Key
     * @return Returns index of array, where element with such key should locate
     */
    private int getIndex(String key) {
        return key.hashCode() % getArraySize();
    }

    /**
     * Find value in HashMap by Key
     * @param key Key
     * @return If element with such key can not be found, returns null, otherwise returns value
     */
    public String get(String key) {
        return arrayOfLists[getIndex(key)].findValue(key);
    }

    /**
     * Check existence of element with given key
     * @param key Key
     * @return Returns true if element was found and false otherwise
     * */
    boolean contains(String key) {
        return arrayOfLists[getIndex(key)].contains(key);
    }

    /**
     * Doubles size of array of lists bigger by rebuilding whole HashMap
     */
    private void rebuild() {
        int currentArraySize = getArraySize();
        List listOfAllElements = arrayOfLists[0];

        for (int i = 1; i < currentArraySize; i++) {
            listOfAllElements.merge(arrayOfLists[i]);
        }

        int newArraySize = currentArraySize * 2;
        arrayOfLists = new List[newArraySize];
        Pair keyValueArray[] = listOfAllElements.toArray();

        initializeArrayOfLists();
        size = 0;
        for (int i = 0; i < keyValueArray.length; i++) {
            put(keyValueArray[i].key(), keyValueArray[i].value());
        }
    }

    /**
     * Deletes all elements from HashMap and decreases size of array of lists to 2
     */
    public void clear() {
        arrayOfLists = new List[2];
        size = 0;
        initializeArrayOfLists();
    }

    /**
     * Removes element from HashMap
     * @param key Key
     * @return Returns value of deleting element or null if element with such key was not found in HashMap
     */
    public String remove(String key) {
        return arrayOfLists[getIndex(key)].remove(key);
    }

    private void initializeArrayOfLists() {
        for (int i = 0; i < getArraySize(); i++) {
            arrayOfLists[i] = new List();
        }
    }
}