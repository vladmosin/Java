package com.hse.trie;

import java.io.*;
import java.util.HashMap;

public class Trie {
    private HashMap<Character, Trie> nextTries;
    private int suffixNumber;
    private boolean isEndOfWord;

    public Trie() {
        nextTries = new HashMap<Character, Trie>();
    }

    public boolean add(String string) throws IllegalArgumentException {
        if (string == null) {
            throw new IllegalArgumentException("added string is null");
        }

        return addFromPosition(string, 0);
    }

    private boolean addFromPosition(String string, int index) {
        suffixNumber++;
        if (index == string.length()) {
            isEndOfWord = true;
            return true;
        }

        char currentSymbol = string.charAt(index);
        if (nextTries.containsKey(currentSymbol)) {
            return nextTries.get(currentSymbol).addFromPosition(string, index + 1);
        } else {
            Trie nextTrie = nextTries.put(currentSymbol, new Trie());
            nextTrie.addFromPosition(string, index + 1);
            return false;
        }
    }

    public boolean contains(String string) throws IllegalArgumentException {
        if (string == null) {
            throw new IllegalArgumentException("string is null");
        }

        return containsFromPosition(string, 0);
    }

    private boolean containsFromPosition (String string, int index) {
        if (index == string.length()) {
            return isEndOfWord;
        }

        Trie nextTrie = nextTries.get(string.charAt(index));

        return nextTrie != null && nextTrie.containsFromPosition(string, index + 1);
    }

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
            return;
        } else {
            nextTrie.removeFromPosition(string, index + 1);
        }
    }

    public int size() {
        return suffixNumber;
    }

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

    private Trie goDownPrefixFromPosition(String prefix, int index) {
        if (index == prefix.length()) {
            return this;
        }

        char currentSymbol = prefix.charAt(index);
        Trie nextNode = nextTries.get(currentSymbol);

        if (nextNode == null) {
            return null;
        } else {
            return nextNode.goDownPrefixFromPosition(prefix, index++);
        }
    }

    public void serialize(OutputStream out) throws IOException {
        try (var dataOut = new DataOutputStream(out)) {
        }
    }

    public void deserialize(InputStream in) throws IOException {
        try (var dataIn = new DataInputStream(in)) {
        }
    }
}
