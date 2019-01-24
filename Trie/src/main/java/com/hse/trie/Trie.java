package com.hse.trie;

import javax.swing.tree.TreeNode;
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

        public void decrement() {
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
            currentNode.decrement();
            if (currentNode.getNext(string.charAt(index)).getSuffixNumber() == 1) {
                currentNode.removeNext(string.charAt(index));
                return true;
            }
            currentNode = currentNode.getNext(string.charAt(index));
        }

        currentNode.setIsNotEndOfWord();
        return true;
    }

    
}
