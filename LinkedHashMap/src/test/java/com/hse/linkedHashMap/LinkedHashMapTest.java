package com.hse.linkedHashMap;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LinkedHashMapTest {
    private LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

    @BeforeEach
    void clearMap() {
        map = new LinkedHashMap<>();
    }

    @Test
    void testPut() {
        map.put("a", 1);
        assertEquals(1, map.size());

        map.put("b", 2);
        assertEquals(2, map.size());

        map.put("c", 3);
        assertEquals(3, map.size());

        map.put("a", 2);
        assertEquals(3, map.size());
    }

    @Test
    void testContains() {
        map.put("first", 1);
        assertTrue(map.contains("first"));

        map.put("second", 1);
        assertTrue(map.contains("second"));
        assertTrue(map.contains("first"));
        assertFalse(map.contains("third"));

        map.put("first", 2);
        assertTrue(map.contains("second"));
        assertTrue(map.contains("first"));
        assertFalse(map.contains("third"));
    }

    @Test
    void testRemoveByKey() {
        map.put("first", 1);
        assertEquals(Integer.valueOf(1), map.removeByKey("first"));
        assertFalse(map.contains("first"));
        assertEquals(0, map.size());


        map.put("a", 2);
        map.put("b", 3);
        map.put("c", 4);

        assertNull(map.removeByKey("first"));
        assertEquals(Integer.valueOf(2), map.removeByKey("a"));
        assertEquals(Integer.valueOf(4), map.removeByKey("c"));
        assertEquals(Integer.valueOf(3), map.removeByKey("b"));
    }

    @Test
    void testGetValue() {
        map.put("a", 43);
        assertNull(map.getValue("b"));
        assertEquals(Integer.valueOf(43), map.getValue("a"));

        map.put("b", 2);
        map.put("c", 3);
        assertEquals(Integer.valueOf(2), map.getValue("b"));
        assertEquals(Integer.valueOf(3), map.getValue("c"));

        map.put("a", 7);
        assertEquals(Integer.valueOf(7), map.getValue("a"));
    }

    @Test
    void testEntrySetSize() {
        map.put("v", 12);
        assertEquals(1, map.entrySet().size());

        map.put("b", 43);
        map.put("g", 571);
        assertEquals(3, map.entrySet().size());

        map.put("k", 21);
        assertEquals(4, map.entrySet().size());
    }

    @Test
    void testIteratorHasNext() {
        assertFalse(map.entrySet().iterator().hasNext());
        map.put("s", 23);
        assertTrue(map.entrySet().iterator().hasNext());
    }

    @Test
    void testOrderSmall() {
        map.put("a", 1);
        map.put("b", 2);

        var iterator = map.entrySet().iterator();

        assertTrue(iterator.hasNext());
        assertTrue(checkEquality(iterator.next(), "a", 1));
        assertTrue(iterator.hasNext());
        assertTrue(checkEquality(iterator.next(), "b", 2));
        assertFalse(iterator.hasNext());
    }

    @Test
    void testOrderMedium() {
        for (int i = 0; i < 100; i++) {
            map.put("a" + i, i);
        }

        var iterator = map.entrySet().iterator();

        for (int i = 0; i < 100; i++) {
            assertTrue(checkEquality(iterator.next(), "a" + i, i));
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    void testOrderBig() {
        for (int i = 0; i < 100000; i++) {
            map.put("a" + i, i);
        }

        var iterator = map.entrySet().iterator();

        for (int i = 0; i < 100000; i++) {
            assertTrue(checkEquality(iterator.next(), "a" + i, i));
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    void testTimeOfWorking() {
        long startTime = System.nanoTime();

        for (int i = 0; i < 1000000; i++) {
            map.put("a" + i, i);
        }

        for (int i = 0; i < 1000000; i++) {
            map.contains("a" + i);
        }

        for (int i = 0; i < 1000000; i++) {
            map.removeByKey("a" + i);
        }

        long endTime = System.nanoTime();

        assertTrue((endTime - startTime) / 1e9 < 10);
    }

    // Time for 1 million operations is 1.55 sec.

    private boolean checkEquality(@NotNull Map.Entry<String, Integer> node,
                                  @NotNull String key, @NotNull Integer value) {
        return value.equals(node.getValue()) && key.equals(node.getKey());
    }
}