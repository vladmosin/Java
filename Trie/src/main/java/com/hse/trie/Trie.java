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
    }

    private TrieNode root;
    private int size;

    public Trie() {
        root = new TrieNode();
    }

    public boolean add(String string) {
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

    public boolean contains(String string) {
        TrieNode currentNode = root;

        for (int index = 0; index < string.length(); index++) {
            if (!currentNode.containsNext(string.charAt(index))) {
                return false;
            }

            currentNode = currentNode.getNext(string.charAt(index));
        }

        return currentNode.isEndOfWord();
    }
}
