package com.hse.trie;

import java.io.*;
import java.util.HashMap;

public class Trie implements Serializable {
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
            nextTries.put(currentSymbol, new Trie());
            nextTries.get(currentSymbol).addFromPosition(string, index + 1);
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
            return nextNode.goDownPrefixFromPosition(prefix, index + 1);
        }
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        try (var dataOut = new DataOutputStream(out)) {
            dataOut.writeBoolean(isEndOfWord);
            dataOut.writeInt(suffixNumber);
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

    @Override
    public void deserialize(InputStream in) throws IOException {
        clear();
        try (var dataIn = new DataInputStream(in)) {
            int childrenNumber = dataIn.readInt();
            isEndOfWord = dataIn.readBoolean();

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
