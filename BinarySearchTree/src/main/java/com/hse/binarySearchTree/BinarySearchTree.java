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

        private Node() {}

        private Node(@NotNull E value, Node<E> left, Node<E> right, Node<E> parent) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.parent = parent;
            size = 0;
        }

        private Node<E> findPrevious() {
            if (left != null) {
                return left.goLeft();
            } else {
                Node<E> firstSmallerParent = goUpRight();

                if ((firstSmallerParent == null) || (firstSmallerParent.left == null)) {
                    return null;
                } else {
                    return firstSmallerParent.left.goRight();
                }
            }
        }

        private Node<E> findNext() {
            if (right != null) {
                return right.goLeft();
            } else {
                Node<E> firstBiggerParent = goUpLeft();

                if ((firstBiggerParent == null) || (firstBiggerParent.right == null)) {
                    return null;
                } else {
                    return firstBiggerParent.right.goLeft();
                }
            }
        }

        private Node<E> goUpLeft() {
            if (hasNoParent()) {
                return null;
            }

            if (parent.right == this) {
                return parent.goUpLeft();
            } else {
                return parent;
            }
        }

        private Node<E> goUpRight() {
            if (hasNoParent()) {
                return null;
            }

            if (parent.left == this) {
                return parent.goUpRight();
            } else {
                return parent;
            }
        }

        private boolean hasNoParent() {
            return parent == null || parent.isRoot();
        }

        private boolean isRoot() {
            return value == null;
        }

        @NotNull private Node<E> goLeft() {
            if (left == null) {
                return this;
            } else {
                return left.goLeft();
            }
        }

        @NotNull private Node<E> goRight() {
            if (right == null) {
                return this;
            } else {
                return right.goRight();
            }
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
    private Node<E> root;
    private Comparator<? super E> comparator;

    public BinarySearchTree(Comparator<? super E> comparator) {
        this.comparator = comparator;
        root = new Node<>();
        isReversed = false;
    }

    public BinarySearchTree() {
        comparator = new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                return o1.compareTo(o2);
            }
        };
        root = new Node<>();
    }

    @Override
    @NotNull public BinarySearchTreeIterator iterator() {
        return new BinarySearchTreeIterator(root, isReversed);
    }

    @Override
    @NotNull public Iterator descendingIterator() {
        return new BinarySearchTreeIterator(root, !isReversed);
    }

    @Override
    public BinarySearchTree descendingSet() {
        return new BinarySearchTree();
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
    public int size() {
        return 0;
    }
}
