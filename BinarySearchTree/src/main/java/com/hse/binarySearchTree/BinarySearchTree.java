package com.hse.binarySearchTree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class BinarySearchTree<E extends Comparable<? super E>> extends AbstractSet<E> implements MyTreeSet<E> {
    private static class Node<E> {
        private int size;
        private E value;
        private Node<E> left;
        private Node<E> right;
        private Node<E> parent;

        private Node() {}

        private Node(@NotNull E value) {
            this.value = value;
            size++;
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

        @Nullable
        private Node<E> findNext() {
            if (right != null) {
                return right.goLeft();
            } else {
                Node<E> firstBiggerParent = goUpLeft();

                if ((firstBiggerParent == null)) {
                    return null;
                } else if (firstBiggerParent.right == null) {
                    return firstBiggerParent;
                } else {
                    return firstBiggerParent.right.goLeft();
                }
            }
        }

        @Nullable
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

        @Nullable
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

        private boolean contains(@NotNull E element, @NotNull Comparator<? super E> comparator) {
            return find(element, comparator) != null;
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

        @Nullable
        private Node<E> findHigher(@NotNull E element, @NotNull Comparator<? super E> comparator) {
            Node<E> node = find(element, comparator);

            if (node == null) {
                node = findLower(element, comparator);
            }

            if (node != null) {
                return node.findNext();
            } else {
                return goLeft();
            }
        }

        private void changeParentsSizes(int additional) {
            size += additional;
            if (parent != null) {
                parent.changeParentsSizes(additional);
            }
        }
    }


    public class BinarySearchTreeIterator implements Iterator<E> {
        Node<E> node;
        private boolean isReversed;
        private int version;

        public BinarySearchTreeIterator(@NotNull Node<E> node, boolean isReversed) {
            this.node = node;
            this.isReversed = isReversed;
            version = BinarySearchTree.this.version;
        }

        public boolean hasPrevious() {
            if (isInvalid()) {
                throw new ConcurrentModificationException("tree was modified");
            }

            if (isReversed) {
                return findNext() != null;
            }

            return findPrevious() != null;
        }

        @NotNull public E previous() {
            if (!hasPrevious()) {
                throw new IllegalArgumentException("no previous element");
            }

            E element;

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
            if (isInvalid()) {
                throw new ConcurrentModificationException("tree was modified");
            }

            if (isReversed) {
                return findPrevious() != null;
            }

            return findNext() != null;
        }

        @NotNull public E next() {
            if (!hasNext()) {
                throw new IllegalArgumentException("no next element");
            }

            E element;

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
            if (isInvalid()) {
                throw new ConcurrentModificationException("tree was modified");
            }

            return node.findNext();
        }

        private Node<E> findPrevious() {
            if (isInvalid()) {
                throw new ConcurrentModificationException("tree was modified");
            }

            return node.findPrevious();
        }

        private boolean isInvalid() {
            return version == BinarySearchTree.this.version;
        }
    }

    private boolean isReversed;
    private Node<E> root;
    private Comparator<? super E> comparator;
    private int version = 0;

    public BinarySearchTree(Comparator<? super E> comparator) {
        this.comparator = comparator;
        root = new Node<>();
        isReversed = false;
    }

    public BinarySearchTree() {
        comparator = Comparator.naturalOrder();
        root = new Node<>();
    }

    private BinarySearchTree(BinarySearchTree<E> tree, boolean isReversed) {
        this.root = tree.root;
        this.isReversed = isReversed;
        if (isReversed) {
            this.comparator = (Comparator<E>) (o1, o2) -> -tree.comparator.compare(o1, o2);
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
    @NotNull public E last() {
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
    @Nullable public E lower(@NotNull E element) {
        Node<E> node = root.findLower(element, comparator);

        if (node == null) {
            return null;
        } else {
            return node.value;
        }
    }

    @Override
    @Nullable public E floor(@NotNull E element) {
        Node<E> node = root.find(element, comparator);

        if (node == null) {
            return lower(element);
        } else {
            return node.value;
        }
    }

    @Override
    @Nullable public E ceiling(@NotNull E element) {
        Node<E> node = root.find(element, comparator);

        if (node == null) {
            return higher(element);
        } else {
            return node.value;
        }
    }

    @Override
    @Nullable public E higher(@NotNull E element) {
        Node<E> node = root.findHigher(element, comparator);

        if (node == null) {
            return null;
        } else {
            return node.value;
        }
    }

    @Override
    public int size() {
        return root.size;
    }

    @Override
    public boolean isEmpty() {
        return root.size == 0;
    }

    @Override
    public boolean add(@NotNull E element) {
        if (contains(element)) {
            return false;
        }

        Node<E> futureParent = root.findLower(element, comparator);
        Node<E> newNode = new Node<>(element);

        if (futureParent == null) {
            if (isEmpty()) {
                futureParent = root;
                root.left = newNode;
                root.right = newNode;
            } else {
                futureParent = root.goLeft();
                futureParent.left = newNode;
            }
        } else {
            if (futureParent.right == null) {
                futureParent.right = newNode;
            } else {
                futureParent = futureParent.right.goLeft();
                futureParent.left = newNode;
            }
        }

        newNode.parent = futureParent;
        futureParent.changeParentsSizes(1);
        version++;
        return true;
    }

    public boolean contains(@NotNull E element) {
        return root.contains(element, comparator);
    }

    @SuppressWarnings("ConstantConditions") // NullPointerException is impossible here
    public boolean remove(@NotNull E element) {
        if (!contains(element)) {
            return false;
        }

        Node<E> node = root.find(element, comparator);
        Node<E> removingNode = node;

        // If node has two children, then node with next value has 0 or 1
        if (node.right != null && node.left != null) {
            Node<E> nextNode = node.findNext();

            node.value = nextNode.value;
            removingNode = nextNode;
        }

        removeSingleChildrenNode(removingNode);
        return true;
    }

    private void removeSingleChildrenNode(@NotNull Node<E> node) {
        Node<E> son = node.left == null ? node.right : node.left;

        if (node.parent.left == node) {
            node.parent.left = son;
        }

        if (node.parent.right == node) {
            node.parent.right = son;
        }

        node.parent.changeParentsSizes(-1);
    }

    @Override
    public void clear() {
        root.size = 0;
        root.left = null;
        root.right = null;
    }
}
