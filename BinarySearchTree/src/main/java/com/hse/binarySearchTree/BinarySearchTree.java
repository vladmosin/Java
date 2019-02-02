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

        @Nullable private Node<E> findPrevious() {
            if (left != null) {
                return left.goLeft();
            } else {
                return goUpRight();
            }
        }

        @Nullable
        private Node<E> findNext() {
            if (right != null) {
                return right.goLeft();
            } else {
                return goUpLeft();
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
        @NotNull Node<E> node;
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

            if (node.isRoot() && size() != 0) {
                return false;
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

            E element = node.value;

            if (isReversed) {
                node = findNext();
            } else {
                node = findPrevious();
            }

            return element;
        }

        public boolean hasNext() {
            if (isInvalid()) {
                throw new ConcurrentModificationException("tree was modified");
            }

            if (node.isRoot() && size() != 0) {
                return true;
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

            if (node.isRoot()) {
                if (isReversed) {
                    node = node.goRight();
                } else {
                    node = node.goLeft();
                }

                return node.value;
            }

            if (isReversed) {
                node = findPrevious();
            } else {
                node = findNext();
            }

            return node.value;
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
            return version != BinarySearchTree.this.version;
        }
    }

    private Node<E> root;
    private Comparator<? super E> comparator;
    private Integer version = 0;
    private boolean isReversed;

    public BinarySearchTree(Comparator<? super E> comparator) {
        this.comparator = comparator;
        isReversed = false;
        root = new Node<>();
    }

    public BinarySearchTree() {
        comparator = Comparator.naturalOrder();
        isReversed = false;
        root = new Node<>();
    }

    private BinarySearchTree(BinarySearchTree<E> tree) {
        this.root = tree.root;
        version = tree.version;
        isReversed = !tree.isReversed;
        this.comparator = tree.comparator;
    }

    @Override
    @NotNull public BinarySearchTreeIterator iterator() {
        return new BinarySearchTreeIterator(root, isReversed);
    }

    @Override
    @NotNull public BinarySearchTreeIterator descendingIterator() {
        return new BinarySearchTreeIterator(root, !isReversed);
    }

    @Override
    public BinarySearchTree<E> descendingSet() {
        return new BinarySearchTree<>(this);
    }

    @Override
    @NotNull public E first() {
        if (root.size == 0) {
            throw new IllegalArgumentException("No elements in BST");
        }

        return root.goLeft().value;
    }

    @Override
    @NotNull public E last() {
        if (root.size == 0) {
            throw new IllegalArgumentException("No elements in BST");
        }

        return root.goRight().value;
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
        version++;
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
