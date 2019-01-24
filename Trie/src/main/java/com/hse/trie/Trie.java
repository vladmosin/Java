package com.hse.trie;

import javax.swing.tree.TreeNode;
import java.util.HashMap;

public class Trie {
    private static class TrieNode {
        private HashMap<Character, TreeNode> nextTreeNode;
        private int suffixNumber;
        private boolean isEndOfWord;

        public TrieNode(boolean isEndOfWord) {
            this.isEndOfWord = isEndOfWord;
            suffixNumber = 1;
        }
    }
}
