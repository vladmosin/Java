package ru.hse;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ListTest {

    @Test
    void testPush() {
        List list = new List();

        assertEquals(0, list.size());
        list.push("firstKey", "firstValue");
        assertEquals(1, list.size());
        list.push("secondKey", "secondValue");
        assertEquals(2, list.size());
    }

    @Test
    void testPop() {
        List list = new List();

        list.push("firstKey", "firstValue");
        list.push("secondKey", "secondValue");

        list.pop();
        assertEquals(1, list.size());
        list.push("thirdKey", "thirdValue");
        assertEquals(2, list.size());
        list.pop();
        list.pop();
        assertEquals(0, list.size());
    }

    @Test
    void testRemove() {
        List list = new List();

        assertNull(list.remove("key"));
        list.push("banana", "apple");
        assertNull(list.remove("banan"));
        list.push("orange", "lemon");
        assertEquals("apple", list.remove("banana"));
        assertEquals("lemon", list.remove("orange"));
    }

    @Test
    void testPut() {
        List list = new List();

        assertNull(list.put("whatever", "someValue"));
        list.push("cat", "apple");
        assertNull(list.put("dog", "orange"));
        assertEquals("orange", list.put("dog", "apple"));
        list.push("dog", "orange");
        assertEquals("orange", list.put("dog", "melon"));
        assertEquals("melon", list.put("dog", "melon"));
        assertEquals("apple", list.put("cat", "coconut"));
    }

    @Test
    void testFindValue() {
        List list = new List();

        list.push("cat", "melon");
        list.push("dog", "coconut");
        list.push("mouse", "coconut");
        assertEquals("coconut", list.findValue("dog"));
        assertEquals("coconut", list.findValue("mouse"));
        assertEquals("melon", list.findValue("cat"));
        list.remove("dog");
        assertNull(list.findValue("dog"));
    }

    @Test
    void testContains() {
        List list = new List();

        list.push("cat", "melon");
        list.push("dog", "coconut");
        list.push("mouse", "orange");

        assertTrue(list.contains("cat"));
        list.push("horse", "apple");
        assertTrue(list.contains("horse"));
        assertFalse(list.contains("snake"));
        assertFalse(list.contains("bear"));
        assertTrue(list.contains("dog"));
        assertFalse(list.contains("frog"));
        assertTrue(list.contains("mouse"));
    }

    @Test
    void testMerge() {
        List list1 = new List();
        List list2 = new List();

        list1.push("cat", "melon");
        list1.push("dog", "coconut");
        list1.push("mouse", "orange");

        list2.push("black", "banana");
        list2.push("green", "cherry");

        list1.merge(list2);
        assertEquals(5, list1.size());
        assertTrue(list1.contains("green"));
        assertTrue(list1.contains("dog"));
        assertTrue(list1.contains("green"));
        assertFalse(list1.contains("yellow"));
    }

    @Test
    void testToArray() {
        List list = new List();

        list.push("cat", "melon");
        list.push("dog", "coconut");
        list.push("mouse", "orange");

        Pair[] array = list.toArray();
        assertEquals(3, array.length);
    }

    @Test
    void testToArrayEmpty() {
        List list = new List();

        assertNull(list.toArray());
    }
}