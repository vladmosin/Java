package com.hse.trie;

import java.io.*;
import java.util.HashMap;

public class Trie {
    private static class TrieNode {
        private HashMap<Character, TrieNode> nextTreeNode;
        private int suffixNumber;
        private boolean isEndOfWord;

        public TrieNode(boolean isEndOfWord) {
            this.isEndOfWord = isEndOfWord;
            suffixNumber = 1;
        }

        public TrieNode() {
        }

        public boolean containsNext(char symbol) {
            return nextTreeNode.containsKey(symbol);
        }

        public TrieNode getNext(char symbol) {
            return nextTreeNode.get(symbol);
        }

        public void addNext(char symbol, TrieNode trieNode) throws IllegalArgumentException {
            if (trieNode == null) {
                throw new IllegalArgumentException("Trie node is null");
            }

            nextTreeNode.put(symbol, trieNode);
        }

        public boolean isEndOfWord() {
            return isEndOfWord;
        }

        public void decrementNumberOfSuffixes() {
            suffixNumber--;
        }

        public int getSuffixNumber() {
            return suffixNumber;
        }

        public void removeNext(char symbol) {
            nextTreeNode.remove(symbol);
        }

        public void setIsNotEndOfWord() {
            isEndOfWord = false;
        }

        public void incrementNumberOfSuffixes() {
            suffixNumber++;
        }

        public void serialize(DataOutputStream dataOut) {

        }

        public void deserialize(DataOutputStream dataIn) {

        }
    }

    private TrieNode root;
    private int size;

    public Trie() {
        root = new TrieNode();
    }

    public boolean add(String string) throws IllegalArgumentException {
        if (string == null) {
            throw new IllegalArgumentException("added string is null");
        }

        TrieNode currentNode = root;
        boolean isNew = false;

        for (int index = 0; index < string.length(); index++) {
            if (!currentNode.containsNext(string.charAt(index))) {
                isNew = true;
                if (index == string.length() - 1) {
                    currentNode.addNext(string.charAt(index), new TrieNode(true));
                } else {
                    currentNode.addNext(string.charAt(index), new TrieNode(false));
                }
            } else {
                currentNode.incrementNumberOfSuffixes();
            }

            currentNode = currentNode.getNext(string.charAt(index));
        }

        return isNew;
    }

    public boolean contains(String string) throws IllegalArgumentException {
        if (string == null) {
            throw new IllegalArgumentException("string is null");
        }

        TrieNode currentNode = root;

        for (int index = 0; index < string.length(); index++) {
            if (!currentNode.containsNext(string.charAt(index))) {
                return false;
            }

            currentNode = currentNode.getNext(string.charAt(index));
        }

        return currentNode.isEndOfWord();
    }

    public boolean remove(String string) throws IllegalArgumentException {
        if (string == null) {
            throw new IllegalArgumentException("removed string is null");
        }

        if (!contains(string)) {
            return false;
        }

        TrieNode currentNode = root;

        for (int index = 0; index < string.length(); index++) {
            currentNode.decrementNumberOfSuffixes();
            if (currentNode.getNext(string.charAt(index)).getSuffixNumber() == 1) {
                currentNode.removeNext(string.charAt(index));
                return true;
            }
            currentNode = currentNode.getNext(string.charAt(index));
        }

        currentNode.setIsNotEndOfWord();
        return true;
    }

    public int size() {
        return root.getSuffixNumber();
    }

    public int howManyStartsWithPrefix(String prefix) throws IllegalArgumentException {
        if (prefix == null) {
            throw new IllegalArgumentException("prefix is null");
        }

        TrieNode currentNode = root;

        for (int index = 0; index < prefix.length(); index++) {
            if (!currentNode.containsNext(prefix.charAt(index))) {
                return 0;
            }

            currentNode = currentNode.getNext(prefix.charAt(index));
        }

        return currentNode.getSuffixNumber();
    }

    public void serialize(OutputStream out) throws IOException {
        try (var dataOut = new DataOutputStream(out)) {
            root.serialize(dataOut);
        }
    }

    public void deserialize(InputStream in) throws IOException {
        try (var dataIn = new DataInputStream(in)) {
            root.deserialize(dataIn);
        }
    }
}
