package com.hse.binarySearchTree;

import org.jetbrains.annotations.NotNull;

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

    public class BinarySearchTreeIterator implements Iterator<E> {
        Node<E> node;
        private boolean isReversed;

        public BinarySearchTreeIterator(@NotNull Node<E> node, boolean isReversed) {
            this.node = node;
            this.isReversed = isReversed;
        }

        public boolean hasPrevious() {
            if (isReversed) {
                return findNext() != null;
            }

            return findPrevious() != null;
        }

        @NotNull public E previous() {
            if (!hasPrevious()) {
                throw new IllegalArgumentException("no previous element");
            }

            E element = null;

            if (isReversed) {
                node = findNext();
                element = node.value;
            } else {
                element = node.value;
                node = findPrevious();
            }

            return element;
        }

        public boolean hasNext() {
            if (isReversed) {
                return findPrevious() != null;
            }

            return findNext() != null;
        }

        @NotNull public E next() {
            if (!hasNext()) {
                throw new IllegalArgumentException("no next element");
            }

            E element = null;

            if (isReversed) {
                element = node.value;
                node = findPrevious();
            } else {
                node = findNext();
                element = node.value;
            }

            return element;
        }

        private Node<E> findNext() {
            return node.findNext();
        }

        private Node<E> findPrevious() {
            return node.findPrevious();
        }
    }

    private boolean isReversed;
    private Comparator<? super E> comparator;

    @Override
    public Iterator descendingIterator() {
        return null;
    }

    @Override
    public MyTreeSet descendingSet() {
        return null;
    }

    @Override
    public E first() {
        return null;
    }

    @Override
    public E last() {
        return null;
    }

    @Override
    public E lower(Object o) {
        return null;
    }

    @Override
    public E floor(Object o) {
        return null;
    }

    @Override
    public E ceiling(Object o) {
        return null;
    }

    @Override
    public E higher(Object o) {
        return null;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
