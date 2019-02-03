package com.hse.binarySearchTree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Implements not balanced BST with auxiliary root
 * */
public class BinarySearchTree<E extends Comparable<? super E>> extends AbstractSet<E> implements MyTreeSet<E> {
    /**
     * Implements node for BST
     * */
    private static class Node<E> {
        /**
         * Size of subtree
         * */
        private int size;
        private E value;
        private Node<E> left;
        private Node<E> right;
        private Node<E> parent;

        /**
         * Constructor especially for auxiliary element, which is root of tree
         * */
        private Node() {}

        private Node(@NotNull E value) {
            this.value = value;
            size++;
        }

        /**
         * Finds Node with element which is previous for this Node's value in sorted order.
         * */
        @Nullable private Node<E> findPrevious() {
            if (left != null) {
                return left.goLeft();
            } else {
                return goUpRight();
            }
        }

        /**
         * Finds Node with element which is next for this Node's value in sorted order.
         * */
        @Nullable
        private Node<E> findNext() {
            if (right != null) {
                return right.goLeft();
            } else {
                return goUpLeft();
            }
        }

        /**
         * Returns first ancestor which is greater than current element.
         * */
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

        /**
         * Returns first ancestor which is less than current element.
         * */
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

        /**
         * Checks if node has a parent, which stores value.
         * */
        private boolean hasNoParent() {
            return parent == null || parent.isRoot();
        }

        /**
         * Checks, if element is auxiliary
         * */
        private boolean isRoot() {
            return value == null;
        }

        /**
         * Returns the most left descendant
         * */
        @NotNull
        private Node<E> goLeft() {
            if (left == null) {
                return this;
            } else {
                return left.goLeft();
            }
        }

        /**
         * Returns the most right descendant
         * */
        @NotNull
        private Node<E> goRight() {
            if (right == null) {
                return this;
            } else {
                return right.goRight();
            }
        }

        /**
         * Finds node, which stores given element as value
         * @param element Element to find in BST
         * @param comparator Compares elements in BST
         * @return Node, which stores element, or null if node with such element was not found
         * */
        @Nullable
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

        /**
         * Checks, if there is a node, which stores given element
         * @param element Element to find in BST
         * @param comparator Compares elements in BST
         * */
        private boolean contains(@NotNull E element, @NotNull Comparator<? super E> comparator) {
            return find(element, comparator) != null;
        }

        /**
         * Finds node, which stores the biggest value among nodes, which store value smaller than element
         * @param comparator Compares elements in BST
         * @return Node, which stores described element, or null if such node was not found
         * */
        @Nullable
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

        /**
         * Finds node, which stores the smallest value among nodes, which store value greater than element
         * @param comparator Compares elements in BST
         * @return Node, which stores described element, or null if such node was not found
         * */
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

        /**
         * Change size of all parents on additional. Additional should be 1 or -1.
         * */
        private void changeParentsSizes(int additional) {
            size += additional;
            if (parent != null) {
                parent.changeParentsSizes(additional);
            }
        }
    }


    /**
     * Implements iterator for BST
     * */
    public class BinarySearchTreeIterator implements Iterator<E> {
        @NotNull Node<E> node;
        private boolean isReversed;
        /**
         * Version of tree for which this iterator is valid
         * */
        private int version;

        /**
         * Special constructor for descending set
         * */
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

        /**
         * Moves iterator to previous position and returns value of element located between old and new positions
         * */
        @SuppressWarnings("ConstantConditions") // NullPointerException is impossible here
        @NotNull
        public E previous() {
            if (!hasPrevious()) { // if iterator is invalid hasPrevious throws exception
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

        /**
         * Moves iterator to next position and returns value of element located between old and new positions
         * */
        @SuppressWarnings("ConstantConditions") // NullPointerException is impossible here
        @NotNull
        public E next() {
            if (!hasNext()) { // if iterator is invalid hasNext throws exception
                throw new IllegalArgumentException("no next element");
            }

            // if current node is root, then the next element is the smallest or biggest in BST
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

        /**
         * Finds next element
         * */
        @Nullable
        private Node<E> findNext() {
            if (isInvalid()) {
                throw new ConcurrentModificationException("tree was modified");
            }

            return node.findNext();
        }

        /**
         * Finds previous element
         * */
        @Nullable
        private Node<E> findPrevious() {
            if (isInvalid()) {
                throw new ConcurrentModificationException("tree was modified");
            }

            return node.findPrevious();
        }

        /**
         * Checks, if iterator is invalid.
         * */
        private boolean isInvalid() {
            return version != BinarySearchTree.this.version;
        }
    }

    private Node<E> root;
    private Comparator<? super E> comparator;

    /**
     * Version of tree
     * */
    private Integer version = 0;

    /**
     * Order of traversal
     * */
    private boolean isReversed;

    public BinarySearchTree(@NotNull Comparator<? super E> comparator) {
        this.comparator = comparator;
        isReversed = false;
        root = new Node<>();
    }

    public BinarySearchTree() {
        comparator = Comparator.naturalOrder();
        isReversed = false;
        root = new Node<>();
    }

    /**
     * Constructor for descending set
     * */
    private BinarySearchTree(@NotNull BinarySearchTree<E> tree) {
        this.root = tree.root;
        version = tree.version;
        isReversed = !tree.isReversed;
        this.comparator = tree.comparator;
    }

    @Override
    /**
     * Returns iterator pointing the first element of BST
     * */
    @NotNull
    public BinarySearchTreeIterator iterator() {
        return new BinarySearchTreeIterator(root, isReversed);
    }

    @Override
    /**
     * Returns reversed iterator pointing the first element of BST
     * */
    @NotNull
    public BinarySearchTreeIterator descendingIterator() {
        return new BinarySearchTreeIterator(root, !isReversed);
    }

    /**
     * Reverse order of element in BST. Does not create a new BST. Complexity O(1).
     * */
    @Override
    public BinarySearchTree<E> descendingSet() {
        return new BinarySearchTree<>(this);
    }

    /**
     * Returns first element of BST
     * */
    @Override
    @NotNull
    public E first() {
        if (root.size == 0) {
            throw new IllegalArgumentException("No elements in BST");
        }

        return root.goLeft().value;
    }

    /**
     * Returns first element of BST
     * */
    @Override
    @NotNull
    public E last() {
        if (root.size == 0) {
            throw new IllegalArgumentException("No elements in BST");
        }

        return root.goRight().value;
    }

    /**
     * Finds the greatest element in BST, which is less than given, or null if such element does not exists
     * */
    @Override
    @Nullable
    public E lower(@NotNull E element) {
        Node<E> node = root.findLower(element, comparator);

        if (node == null) {
            return null;
        } else {
            return node.value;
        }
    }

    /**
     * Finds the greatest element in BST less than or equal to the given element,
     * or null if such element does not exists
     */
    @Override
    @Nullable
    public E floor(@NotNull E element) {
        Node<E> node = root.find(element, comparator);

        if (node == null) {
            return lower(element);
        } else {
            return node.value;
        }
    }

    /**
     * Finds the smallest element in BST greater than or equal to the given element,
     * or null if such element does not exists
     */
    @Override
    @Nullable
    public E ceiling(@NotNull E element) {
        Node<E> node = root.find(element, comparator);

        if (node == null) {
            return higher(element);
        } else {
            return node.value;
        }
    }

    /**
     * Finds the greatest element in BST, which is less than given, or null if such element does not exists
     * */
    @Override
    @Nullable
    public E higher(@NotNull E element) {
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

    /**
     * Adds element to BST
     * @return If element already exists in BST returns false, otherwise returns true.
     * */
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

    /**
     * Removes element from BST
     * @return If element does not exist in BST returns false, otherwise returns true.
     * */
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


    /**
     * Removes node which has less than two children
     * */
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
