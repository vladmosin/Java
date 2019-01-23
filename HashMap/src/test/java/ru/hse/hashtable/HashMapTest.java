package ru.hse.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class HashMapTest {
    private HashMap hashMap;

    @BeforeEach
    void initHashMap() {
        hashMap = new HashMap();
    }

    @Test
    void testPut() {
        assertNull(hashMap.put("cat", "melon"));
        assertNull(hashMap.put("dog", "cherry"));
        assertEquals(2, hashMap.size());
        assertNull(hashMap.put("frog", "lime"));
        assertEquals("melon", hashMap.put("cat", "mango"));
        assertEquals(3, hashMap.size());
    }

    @Test
    void testPutInBigHashTable() {
        assertNull(hashMap.put("cat", "melon"));
        assertNull(hashMap.put("dog", "cherry"));
        assertNull(hashMap.put("frog", "lime"));
        assertNull(hashMap.put("snake", "olive"));
        assertNull(hashMap.put("zebra", "pear"));
        assertNull(hashMap.put("tiger", "pomelo"));

        assertEquals(6, hashMap.size());
        assertEquals("lime", hashMap.put("frog", "avocado"));
        assertEquals(6, hashMap.size());
        assertNull(hashMap.put("lion", "tomato"));
        assertEquals(7, hashMap.size());
    }

    @Test
    void testGet() {
        hashMap.put("cat", "melon");
        hashMap.put("dog", "cherry");
        assertNull(hashMap.get("frog"));
        assertNull(hashMap.put("frog", "lime"));
        assertEquals("lime", hashMap.get("frog"));
        hashMap.put("cat", "mango");
        assertEquals(3, hashMap.size());
        assertEquals("mango", hashMap.get("cat"));
        assertEquals(3, hashMap.size());
    }

    @Test
    void testContains() {
        hashMap.put("cat", "melon");
        hashMap.put("horse", "cherry");
        hashMap.put("frog", "lime");
        hashMap.put("cow", "olive");
        hashMap.put("zebra", "pear");
        hashMap.put("goose", "pomelo");

        assertTrue(hashMap.contains("cow"));
        assertFalse(hashMap.contains("bull"));
        assertFalse(hashMap.contains("dog"));
        assertTrue(hashMap.contains("cat"));
        assertTrue(hashMap.contains("horse"));
        assertTrue(hashMap.contains("frog"));
        assertTrue(hashMap.contains("goose"));
        assertTrue(hashMap.contains("zebra"));
    }

    @Test
    void testClear() {
        hashMap.put("cat", "melon");
        hashMap.clear();
        assertEquals(0, hashMap.size());
        assertFalse(hashMap.contains("cat"));

        hashMap.put("horse", "cherry");
        hashMap.put("frog", "lime");
        hashMap.clear();
        assertEquals(0, hashMap.size());

        hashMap.put("cow", "olive");
        hashMap.put("zebra", "pear");
        hashMap.put("goose", "pomelo");
        hashMap.clear();
        assertFalse(hashMap.contains("cow"));
        assertFalse(hashMap.contains("zebra"));
        assertFalse(hashMap.contains("goose"));
    }

    @Test
    void testRemove() {
        hashMap.put("cat", "melon");
        hashMap.put("horse", "cherry");
        hashMap.put("frog", "lime");
        hashMap.put("cow", "olive");
        hashMap.put("zebra", "pear");
        hashMap.put("goose", "pomelo");

        assertEquals("melon", hashMap.remove("cat"));
        assertNull(hashMap.remove("cat"));
        assertNull(hashMap.remove("snake"));
        assertEquals("pear", hashMap.remove("zebra"));
        assertEquals("olive", hashMap.remove("cow"));
        assertEquals("lime", hashMap.remove("frog"));
        assertNull(hashMap.remove("cow"));
        assertEquals("pomelo", hashMap.remove("goose"));
        assertEquals("cherry", hashMap.remove("horse"));
        assertNull(hashMap.remove("cat"));
    }

    @Test
    void testMethodsTogether() {
        assertNull(hashMap.put("cat", "melon"));
        assertTrue(hashMap.contains("cat"));
        assertNull(hashMap.get("dog"));

        assertNull(hashMap.put("goose", "lime"));
        assertNull(hashMap.remove("cow"));
        assertTrue(hashMap.contains("goose"));

        assertNull(hashMap.put("goat", "cabbage"));
        assertFalse(hashMap.contains("snake"));
        assertEquals("lime", hashMap.remove("goose"));

        assertEquals("cabbage", hashMap.put("goat", "carrot"));
        assertEquals(3, hashMap.size());
        assertEquals("carrot", hashMap.remove("goat"));
        assertEquals("melon", hashMap.remove("cat"));
    }
}