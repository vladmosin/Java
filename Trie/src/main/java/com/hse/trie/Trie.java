package com.hse.trie;

import java.io.*;
import java.util.HashMap;

/**
 * Implements recursive trie
 * */
public class Trie implements Serializable {
    /**
     * Stores next tries as values by char as key they are connected with current trie
     * */
    private HashMap<Character, Trie> nextTries;

    /**
     * Stores number of strings in trie
     * */
    private int suffixNumber;

    /**
     * Stores if there is an element which ends in current tree
     * */
    private boolean isEndOfWord;

    public Trie() {
        nextTries = new HashMap<Character, Trie>();
    }

    /**
     * Added new element to trie
     * @param string added element
     * @return If there was no given string in trie returns true, otherwise returns false
     * */
    public boolean add(String string) throws IllegalArgumentException {
        if (string == null) {
            throw new IllegalArgumentException("added string is null");
        }

        if (contains(string)) {
            return false;
        } else {
            addFromPosition(string, 0);
            return true;
        }
    }

    /**
     * Adds to trie suffix of string starts from index
     * */
    private void addFromPosition(String string, int index) {
        suffixNumber++;
        if (index == string.length()) {
            isEndOfWord = true;
            return;
        }

        char currentSymbol = string.charAt(index);

        if (nextTries.containsKey(currentSymbol)) {
            nextTries.get(currentSymbol).addFromPosition(string, index + 1);
        } else {
            nextTries.put(currentSymbol, new Trie());
            nextTries.get(currentSymbol).addFromPosition(string, index + 1);
        }
    }

    /**
     * Returns true if string contains in trie
     * */
    public boolean contains(String string) throws IllegalArgumentException {
        if (string == null) {
            throw new IllegalArgumentException("string is null");
        }

        return containsFromPosition(string, 0);
    }

    /**
     * Returns true if suffix of string starts from index contains in trie
     * */
    private boolean containsFromPosition (String string, int index) {
        if (index == string.length()) {
            return isEndOfWord;
        }

        Trie nextTrie = nextTries.get(string.charAt(index));

        return nextTrie != null && nextTrie.containsFromPosition(string, index + 1);
    }

    /**
     * Removes string from trie
     * @return If it was no string in trie returns false, otherwise true
     * */
    public boolean remove(String string) throws IllegalArgumentException {
        if (string == null) {
            throw new IllegalArgumentException("removed string is null");
        }

        if (!contains(string)) {
            return false;
        } else {
            removeFromPosition(string, 0);
            return true;
        }
    }

    /**
     * Removes suffix of string starts from index from trie
     * */
    private void removeFromPosition(String string, int index) {
        suffixNumber--;
        if (index == string.length()) {
            isEndOfWord = false;
            return;
        }

        char currentSymbol = string.charAt(index);
        Trie nextTrie = nextTries.get(currentSymbol);

        if (nextTrie.suffixNumber == 1) {
            nextTries.remove(currentSymbol);
        } else {
            nextTrie.removeFromPosition(string, index + 1);
        }
    }

    /**
     * Returns number of string in trie
     * */
    public int size() {
        return suffixNumber;
    }

    /**
     * Returns number of strings in trie start with given prefix
     * */
    public int howManyStartsWithPrefix(String prefix) throws IllegalArgumentException {
        if (prefix == null) {
            throw new IllegalArgumentException("prefix is null");
        }

        Trie trieDownPrefix = goDownPrefixFromPosition(prefix, 0);

        if (trieDownPrefix == null) {
            return 0;
        } else {
            return trieDownPrefix.suffixNumber;
        }
    }

    /**
     * Goes down trie
     * @return If there is no string with such prefix returns null, otherwise returns trie, which is prefix-down
     * current element
     * */
    private Trie goDownPrefixFromPosition(String prefix, int index) {
        if (index == prefix.length()) {
            return this;
        }

        char currentSymbol = prefix.charAt(index);
        Trie nextNode = nextTries.get(currentSymbol);

        if (nextNode == null) {
            return null;
        } else {
            return nextNode.goDownPrefixFromPosition(prefix, index + 1);
        }
    }

    /**
     * Converts trie to byte sequence and writes to OutputStream given as argument
     * Format:
     * 1. isEndOfWord
     * 2. number of children
     * 3. symbol
     * 4. description of trie which is connected by symbol from 3.
     */
    @Override
    public void serialize(OutputStream out) throws IOException {
        try (var dataOut = new DataOutputStream(out)) {
            dataOut.writeBoolean(isEndOfWord);
            dataOut.writeInt(nextTries.size());
            for (char symbol : nextTries.keySet()) {
                dataOut.writeChar(symbol);
                nextTries.get(symbol).serialize(out);
            }
        }
    }

    private void clear() {
        nextTries.clear();
        isEndOfWord = false;
        suffixNumber = 0;
    }

    /**
     * Replace old tree with new trie given in InputStream
     * */
    @Override
    public void deserialize(InputStream in) throws IOException {
        clear();
        try (var dataIn = new DataInputStream(in)) {
            isEndOfWord = dataIn.readBoolean();
            int childrenNumber = dataIn.readInt();

            if (isEndOfWord) {
                suffixNumber = 1;
            }

            for (int index = 0; index < childrenNumber; index++) {
                char newSymbol = dataIn.readChar();

                var newTrie = new Trie();

                newTrie.deserialize(in);
                suffixNumber += newTrie.suffixNumber;
                nextTries.put(newSymbol, newTrie);
            }
        }
    }
}
