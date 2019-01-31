package com.hse.binarySearchTree;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;

public class BinarySearchTree<E extends Comparable<? super E>> extends AbstractSet implements MyTreeSet {
    private static class Node<E> {
        private int size;
        private E value;
        private Node<E> left;
        private Node<E> right;
        private Node<E> parent;

        private Node(E value, Node<E> left, Node<E> right, Node<E> parent) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }
    }

    private boolean isReversed;
    private Comparator<? super E> comparator;

    public Iterator descendingIterator() {
        return null;
    }

    public MyTreeSet descendingSet() {
        return null;
    }

    public E first() {
        return null;
    }

    public E last() {
        return null;
    }

    public E lower(Object o) {
        return null;
    }

    public E floor(Object o) {
        return null;
    }

    public E ceiling(Object o) {
        return null;
    }

    public E higher(Object o) {
        return null;
    }

    public Iterator iterator() {
        return null;
    }

    public int size() {
        return 0;
    }
}
