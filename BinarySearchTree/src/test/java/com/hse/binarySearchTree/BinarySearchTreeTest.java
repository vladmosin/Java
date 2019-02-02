package com.hse.binarySearchTree;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeTest {
    private Comparator<Integer> comparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer a, Integer b) {
            return  Math.abs(a % 4) - Math.abs(b % 4);
        }
    };

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
    void testFirstLast() {
        var bst = new BinarySearchTree<Integer>();

        assertThrows(IllegalArgumentException.class, bst::first);
        assertThrows(IllegalArgumentException.class, bst::last);

        bst.add(6);
        assertEquals(Integer.valueOf(6), bst.first());
        assertEquals(Integer.valueOf(6), bst.last());
        bst.add(8);
        bst.add(1);
        assertEquals(Integer.valueOf(1), bst.first());
        assertEquals(Integer.valueOf(8), bst.last());
        bst.remove(8);
        assertEquals(Integer.valueOf(6), bst.last());
    }

    @Test
    void testLowerHigher() {
        var bst = new BinarySearchTree<Integer>();

        assertNull(bst.lower(0));
        assertNull(bst.higher(90));

        bst.add(1000);
        bst.add(32);
        bst.add(-40);

        assertEquals(Integer.valueOf(1000), bst.lower(20000));
        assertEquals(Integer.valueOf(32), bst.lower(1000));
        assertEquals(Integer.valueOf(1000), bst.higher(45));
        assertNull(bst.higher(1000));
    }

    @Test
    void testFloorCeiling() {
    }

    @Test
    void isEmpty() {
    }

    @Test
    void iterator() {
    }

    @Test
    void descendingIterator() {
    }

    @Test
    void descendingSet() {
    }
}