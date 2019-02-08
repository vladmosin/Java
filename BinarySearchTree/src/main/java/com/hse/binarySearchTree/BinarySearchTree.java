package com.hse.binarySearchTree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Implements not balanced BST
 * */
public class BinarySearchTree<E> extends AbstractSet<E> implements MyTreeSet<E> {
    /**
     * Implements node for BST
     * */
    private class Node {
        /**
         * Size of subtree
         * */
        private int size;
        @NotNull private E value;
        @Nullable private Node left;
        @Nullable private Node right;
        @Nullable private Node parent;

        private Node(@NotNull E value) {
            this.value = value;
            size++;
        }

        /**
         * Finds Node with element which is previous for this Node's value in sorted order.
         * */
        @Nullable private Node findPrevious() {
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
        private Node findNext() {
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
        private Node goUpLeft() {
            if (parent == null) {
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
        private Node goUpRight() {
            if (parent == null) {
                return null;
            }

            if (parent.left == this) {
                return parent.goUpRight();
            } else {
                return parent;
            }
        }

        /**
         * Returns the most left descendant
         * */
        @NotNull
        private Node goLeft() {
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
        private Node goRight() {
            if (right == null) {
                return this;
            } else {
                return right.goRight();
            }
        }

        /**
         * Finds node, which stores given element as value
         * @param element Element to find in BST
         * @return Node, which stores element, or null if node with such element was not found
         * */
        @Nullable
        private Node find(@NotNull Object element) {
            int resultOfComparison = compareElement(element, value);

            if (resultOfComparison == 0) {
                return this;
            } else if (resultOfComparison > 0){
                return right == null ? null : right.find(element);
            } else {
                return left == null ? null : left.find(element);
            }
        }

        /**
         * Checks, if there is a node, which stores given element
         * @param element Element to find in BST
         * */
        private boolean contains(@NotNull Object element) {
            return find(element) != null;
        }

        /**
         * Finds node, which stores the biggest value among nodes, which store value smaller than element
         * @return Node, which stores described element, or null if such node was not found
         * */
        @Nullable
        private Node findLower(@NotNull Object element) {
            int resultOfComparison = compareElement(element, value);

            if (resultOfComparison <= 0) {
                return left == null ? null : left.findLower(element);
            } else if (right == null) {
                return this;
            } else {
                Node node = right.findLower(element);
                return node == null ? this : node;
            }
        }

        /**
         * Finds node, which stores the smallest value among nodes, which store value greater than element
         * @return Node, which stores described element, or null if such node was not found
         * */
        @Nullable
        private Node findHigher(@NotNull Object element) {
            Node node = find(element);

            if (node == null) {
                node = findLower(element);
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
        @Nullable Node node;
        private boolean isReversed;
        /**
         * Version of tree for which this iterator is valid
         * */
        private int version;

        /**
         * Special constructor for descending set.
         * */
        public BinarySearchTreeIterator(boolean isReversed) {
            this.node = null;
            this.isReversed = isReversed;
            version = BinarySearchTree.this.version;
        }

        public boolean hasPrevious() {
            if (isInvalid()) {
                throw new ConcurrentModificationException("tree was modified");
            }

            if (node == null) {
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

            if (node == null && size != 0) {
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

            // if current node is null, then the next element is the smallest or biggest in BST
            if (node == null) {
                if (isReversed) {
                    node = root.goRight();
                } else {
                    node = root.goLeft();
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
        private Node findNext() {
            if (isInvalid()) {
                throw new ConcurrentModificationException("tree was modified");
            }

            if (node == null) {
                if (root == null) {
                    return null;
                } else {
                    return root.goLeft();
                }
            }

            return node.findNext();
        }

        /**
         * Finds previous element
         * */
        @Nullable
        private Node findPrevious() {
            if (isInvalid()) {
                throw new ConcurrentModificationException("tree was modified");
            }

            if (node == null) {
                return null;
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

    @Nullable private Node root;
    @Nullable private Comparator<? super E> comparator;
    private int size;

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
    }

    public BinarySearchTree() {
        isReversed = false;
    }

    @SuppressWarnings("unchecked")
    private int compareElement(@NotNull Object o1, @NotNull E o2) {
        if (comparator != null) {
            return comparator.compare((E) o1, o2);
        } else {
            return ((Comparable<? super E>)o1).compareTo(o2);
        }
    }

    /**
     * Constructor for descending set
     * */
    private BinarySearchTree(@NotNull BinarySearchTree<E> tree) {
        this.root = tree.root;
        version = tree.version;
        size = tree.size();
        isReversed = !tree.isReversed;
        this.comparator = tree.comparator;
    }

    /**
     * Returns iterator pointing the first element of BST
     * */
    @Override
    @NotNull
    public BinarySearchTreeIterator iterator() {
        return new BinarySearchTreeIterator(isReversed);
    }

    /**
     * Returns reversed iterator pointing the first element of BST
     * */
    @Override
    @NotNull
    public BinarySearchTreeIterator descendingIterator() {
        return new BinarySearchTreeIterator(!isReversed);
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
        if (root == null) {
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
        if (root == null) {
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
        if (root == null) {
            return null;
        }

        Node node = root.findLower(element);

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
        if (root == null) {
            return null;
        }

        Node node = root.find(element);

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
        if (root == null) {
            return null;
        }

        Node node = root.find(element);

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
        if (root == null) {
            return null;
        }

        Node node = root.findHigher(element);

        if (node == null) {
            return null;
        } else {
            return node.value;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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

        version++;

        if (root == null) {
            root = new Node(element);
            size = 1;
            return true;
        }

        Node futureParent = root.findLower(element);
        Node newNode = new Node(element);

        if (futureParent == null) {
            futureParent = root.goLeft();
            futureParent.left = newNode;
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
        size++;
        return true;
    }

    @Override
    public boolean contains(@NotNull Object element) {
        if (root == null) {
            return false;
        } else {
            return root.contains(element);
        }
    }

    /**
     * Removes element from BST
     * @return If element does not exist in BST returns false, otherwise returns true.
     * */
    @SuppressWarnings("ConstantConditions") // NullPointerException is impossible here
    @Override
    public boolean remove(@NotNull Object element) {
        if (!contains(element)) {
            return false;
        }

        Node node = root.find(element);
        Node removingNode = node;

        // If node has two children, then node with next value has 0 or 1
        if (node.right != null && node.left != null) {
            Node nextNode = node.findNext();

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
    private void removeSingleChildrenNode(@NotNull Node node) {
        Node son = node.left == null ? node.right : node.left;

        if (node.parent == null) {
            root = son;
            size--;
            return;
        }

        if (node.parent.left == node) {
            node.parent.left = son;
        }

        if (node.parent.right == node) {
            node.parent.right = son;
        }

        node.parent.changeParentsSizes(-1);
        size--;
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }
}
