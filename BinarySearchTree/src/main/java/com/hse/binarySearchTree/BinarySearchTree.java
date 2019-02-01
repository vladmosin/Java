package com.hse.binarySearchTree;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;

public class BinarySearchTree<E extends Comparable<? super E>> extends AbstractSet<E> implements MyTreeSet<E> {
    private static class Node<E> {
        private int size;
        private E value;
        private Node<E> left;
        private Node<E> right;
        private Node<E> parent;

        private Node() {}

        private Node(@NotNull E value, @NotNull Node<E> parent, Node<E> left, Node<E> right) {
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

        private Node<E> find(@NotNull E element, @NotNull Comparator<? super E> comparator) {
            if (isRoot()) {
                if (left == null) {
                    return null;
                } else {
                    return left.find(element, comparator);
                }
            }

            int resultOfComparison = comparator.compare(element, value);

            if (resultOfComparison == 0) {
                return this;
            } else if (resultOfComparison > 0){
                return right == null ? null : right.find(element, comparator);
            } else {
                return left == null ? null : left.find(element, comparator);
            }
        }

        private Node<E> findLower(@NotNull E element, @NotNull Comparator<? super E> comparator) {
            if (isRoot()) {
                if (left == null) {
                    return null;
                } else {
                    return left.findLower(element, comparator);
                }
            }

            int resultOfComparison = comparator.compare(element, value);

            if (resultOfComparison <= 0) {
                return left == null ? null : left.findLower(element, comparator);
            } else if (right == null) {
                return this;
            } else {
                Node<E> node = right.findLower(element, comparator);
                return node == null ? this : node;
            }
        }

        private Node<E> findHigher(@NotNull E element, @NotNull Comparator<? super E> comparator) {
            if (isRoot()) {
                if (left == null) {
                    return null;
                } else {
                    return left.findHigher(element, comparator);
                }
            }

            int resultOfComparison = comparator.compare(element, value);

            if (resultOfComparison <= 0) {
                return right == null ? null : right.findHigher(element, comparator);
            } else if (left == null) {
                return this;
            } else {
                Node<E> node = left.findHigher(element, comparator);
                return node == null ? this : node;
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
        comparator = (Comparator<E>) (o1, o2) -> o1.compareTo(o2);
        root = new Node<>();
    }

    private BinarySearchTree(BinarySearchTree<E> tree, boolean isReversed) {
        this.root = tree.root;
        this.isReversed = isReversed;
        this.comparator = tree.comparator;
    }

    public E find(E element) {
        Node<E> node = root.find(element, comparator);

        if (node == null) {
            return null;
        } else {
            return node.value;
        }
    }

    @Override
    @NotNull public BinarySearchTreeIterator iterator() {
        return new BinarySearchTreeIterator(root, isReversed);
    }

    @Override
    @NotNull public Iterator<E> descendingIterator() {
        return new BinarySearchTreeIterator(root, !isReversed);
    }

    @Override
    public BinarySearchTree<E> descendingSet() {
        return new BinarySearchTree<>();
    }

    @Override
    @NotNull public E first() {
        if (root.size == 0) {
            throw new IllegalArgumentException("No elements in BST");
        }

        if (isReversed) {
            return root.goRight().value;
        } else {
            return root.goLeft().value;
        }
    }

    @Override
    public E last() {
        if (root.size == 0) {
            throw new IllegalArgumentException("No elements in BST");
        }

        if (isReversed) {
            return root.goLeft().value;
        } else {
            return root.goRight().value;
        }
    }

    @Override
    public E lower(E element) {
        return null;
    }

    public E floor(E element) {
        return null;
    }

    public E ceiling(E element) {
        return null;
    }

    public E higher(E element) {
        return null;
    }

    @Override
    public int size() {
        return root.size;
    }
}
