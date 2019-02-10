package com.hse.binarySearchTree;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.ConcurrentModificationException;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeTest {
    private Comparator<Integer> comparator = (a, b) -> Math.abs(a % 4) - Math.abs(b % 4);

    @Test
    void testAddInteger() {
        var bst = new BinarySearchTree<Integer>();

        assertEquals(0, bst.size());
        assertTrue(bst.add(5000));
        assertTrue(bst.add(8));
        assertEquals(2, bst.size());
        assertTrue(bst.add(-129));
        assertFalse(bst.add(8));
        assertEquals(3, bst.size());
    }

    @Test
    void testAddString() {
        var bst = new BinarySearchTree<String>();

        assertEquals(0, bst.size());
        assertTrue(bst.add("first"));
        assertTrue(bst.add("second"));
        assertEquals(2, bst.size());
        assertTrue(bst.add("third"));
        assertFalse(bst.add("second"));
        assertEquals(3, bst.size());
    }

    @Test
    void testAddWithComparator() {
        var bst = new BinarySearchTree<Integer>(comparator);

        assertEquals(0, bst.size());
        assertTrue(bst.add(5000));
        assertFalse(bst.add(8));
        assertEquals(1, bst.size());
        assertTrue(bst.add(-129));
        assertTrue(bst.add(3));
        assertEquals(3, bst.size());
    }

    @Test
    void testContains() {
        var bst = new BinarySearchTree<String>();

        bst.add("aaaa");
        bst.add("hbd");
        bst.add("cau");

        assertFalse(bst.contains("aaa"));
        assertTrue(bst.contains("hbd"));
        assertTrue(bst.contains("cau"));
        assertEquals(3, bst.size());
    }

    @Test
    void testRemoveWithComparator() {
        var bst = new BinarySearchTree<Integer>(comparator);

        bst.add(53);
        bst.add(78);
        bst.add(88);
        bst.add(3);

        assertEquals(4, bst.size());
        assertTrue(bst.remove(5));
        assertEquals(3, bst.size());
        assertFalse(bst.remove(9));
        assertEquals(3, bst.size());
        assertTrue(bst.remove(1000));
        assertTrue(bst.remove(2));
        assertEquals(1, bst.size());
    }

    @Test
    void testRemove() {
        var bst = new BinarySearchTree<Character>();

        bst.add('v');
        bst.add('a');
        bst.add('y');
        bst.add('j');

        assertTrue(bst.remove('v'));
        assertEquals(3, bst.size());
        assertTrue(bst.remove('y'));
        assertFalse(bst.remove('v'));
        assertEquals(2, bst.size());
    }

    @Test
    void testEmpty() {
        var bst = new BinarySearchTree<String>();

        assertFalse(bst.remove("some"));
        assertTrue(bst.add("first"));
        assertTrue(bst.remove("first"));
        assertEquals(0, bst.size());
        assertTrue(bst.add("first"));
        assertFalse(bst.isEmpty());
        bst.clear();
        assertTrue(bst.isEmpty());
        assertEquals(0, bst.size());
        assertThrows(IllegalArgumentException.class, () -> {bst.add(null);});
    }

    @Test
    void testFirstAssertion() {
        var bst = new BinarySearchTree<Integer>();

        assertThrows(IllegalStateException.class, bst::first);
    }

    @Test
    void testLastAssertion() {
        var bst = new BinarySearchTree<Integer>();

        assertThrows(IllegalStateException.class, bst::last);
    }

    @Test
    void testFirst() {
        var bst = new BinarySearchTree<Integer>();

        bst.add(6);
        assertEquals(Integer.valueOf(6), bst.first());
        bst.add(8);
        bst.add(1);
        assertEquals(Integer.valueOf(1), bst.first());
        bst.remove(8);
        assertEquals(Integer.valueOf(1), bst.first());
    }

    @Test
    void testLast() {
        var bst = new BinarySearchTree<Integer>();

        bst.add(6);
        assertEquals(Integer.valueOf(6), bst.last());
        bst.add(8);
        bst.add(1);
        assertEquals(Integer.valueOf(8), bst.last());
        bst.remove(8);
        assertEquals(Integer.valueOf(6), bst.last());
    }

    @Test
    void testLower() {
        var bst = new BinarySearchTree<Integer>();

        assertNull(bst.lower(0));

        bst.add(1000);
        bst.add(32);
        bst.add(-40);

        assertEquals(Integer.valueOf(1000), bst.lower(20000));
        assertEquals(Integer.valueOf(32), bst.lower(1000));
        assertNull(bst.higher(1000));
    }

    @Test
    void testHigher() {
        var bst = new BinarySearchTree<Integer>();

        assertNull(bst.higher(90));

        bst.add(1000);
        bst.add(32);
        bst.add(-40);

        assertEquals(Integer.valueOf(1000), bst.higher(45));
        assertNull(bst.higher(1000));
    }

    @Test
    void testFloor() {
        var bst = new BinarySearchTree<Integer>();

        assertNull(bst.floor(0));

        bst.add(1000);
        bst.add(321);
        bst.add(-445);
        bst.add(68);

        assertEquals(Integer.valueOf(1000), bst.floor(20000));
        assertEquals(Integer.valueOf(1000), bst.floor(1000));
    }

    @Test
    void testCeiling() {
        var bst = new BinarySearchTree<Integer>();

        assertNull(bst.ceiling(90));

        bst.add(1000);
        bst.add(321);
        bst.add(-445);
        bst.add(68);

        assertEquals(Integer.valueOf(1000), bst.ceiling(1000));
        assertEquals(Integer.valueOf(321), bst.ceiling(151));
        assertNull(bst.ceiling(10000));
    }

    @Test
    void testIterator() {
        var bst = new BinarySearchTree<String>();

        bst.add("abcd");
        bst.add("klmn");
        bst.add("efgh");

        var bstIterator = bst.iterator();
        assertTrue(bstIterator.hasNext());
        assertFalse(bstIterator.hasPrevious());
        assertEquals("abcd", bstIterator.next());
        assertEquals("efgh", bstIterator.next());
        assertEquals("klmn", bstIterator.next());
        assertEquals("klmn", bstIterator.previous());
    }

    @Test
    void descendingIterator() {
        var bst = new BinarySearchTree<String>();

        bst.add("abcd");
        bst.add("klmn");
        bst.add("efgh");

        var bstIterator = bst.descendingIterator();
        assertTrue(bstIterator.hasNext());
        assertFalse(bstIterator.hasPrevious());
        assertEquals("klmn", bstIterator.next());
        assertEquals("efgh", bstIterator.next());
        assertEquals("abcd", bstIterator.next());
        assertEquals("abcd", bstIterator.previous());
    }

    @Test
    void testDescendingSet() {
        var bst = new BinarySearchTree<Integer>();

        bst.add(7);
        bst.add(-90);
        bst.add(34);
        bst.add(67);

        var descendingBST = bst.descendingSet();
        var descendingIterator = bst.descendingIterator();
        var descendingBSTIterator = descendingBST.iterator();

        assertEquals(descendingIterator.next(), descendingBSTIterator.next());
        assertEquals(descendingIterator.next(), descendingBSTIterator.next());
        assertEquals(descendingIterator.next(), descendingBSTIterator.next());
        assertEquals(descendingIterator.next(), descendingBSTIterator.next());
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void testIteratorInvalidation() {
        var bst = new BinarySearchTree<String>();
        var iterator = bst.iterator();

        bst.add("something");
        assertThrows(ConcurrentModificationException.class, iterator::hasNext);

        var descendingIterator = bst.descendingIterator();
        bst.remove("something");

        assertThrows(ConcurrentModificationException.class, iterator::hasNext);
        assertThrows(ConcurrentModificationException.class, descendingIterator::hasNext);
    }

    private static class Number {
        private int value;

        private Number(int value) {
            this.value = value;
        }
    }

    @Test
    void testWithoutComparator() {
        var bst = new BinarySearchTree<Number>();

        bst.add(new Number(4));
        assertThrows(ClassCastException.class, () -> {bst.add(new Number(5));});
    }

    @Test
    void testNumberWithComparator() {
        Comparator<Number> comparator = (o1, o2) -> o1.value - o2.value;
        var bst = new BinarySearchTree<>(comparator);

        bst.add(new Number(4));
        bst.add(new Number(50));
        bst.add(new Number(7));

        assertTrue(bst.contains(new Number(50)));
        assertTrue(bst.contains(new Number(7)));
        assertFalse(bst.contains(new Number(745)));

        assertTrue(bst.remove(new Number(4111)));
        assertFalse(bst.remove(new Number(51)));
    }
}