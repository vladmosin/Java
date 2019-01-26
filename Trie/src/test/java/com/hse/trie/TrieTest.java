package com.hse.trie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {
    private Trie testTrie;

    @BeforeEach
    void initialize() {
        testTrie = new Trie();
    }

    @Test
    void testAdd() {
        assertTrue(testTrie.add("scala"));
        assertTrue(testTrie.add("python"));

        assertEquals(2, testTrie.size());

        assertFalse(testTrie.add("scala"));
        assertTrue(testTrie.add("java"));

        assertEquals(3, testTrie.size());
    }

    @Test
    void testContains() {
        testTrie.add("java");
        testTrie.add("c++");
        testTrie.add("haskell");
        testTrie.add("ruby");

        assertTrue(testTrie.contains("c++"));
        assertFalse(testTrie.contains("scala"));
        assertTrue(testTrie.contains("java"));
        assertTrue(testTrie.contains("haskell"));
        assertTrue(testTrie.contains("ruby"));
    }

    @Test
    void testRemove() {
        testTrie.add("java");
        testTrie.add("c++");
        testTrie.add("php");

        assertFalse(testTrie.remove("perl"));
        assertEquals(3, testTrie.size());
        assertTrue(testTrie.remove("c++"));
        assertEquals(2, testTrie.size());

        testTrie.add("perl");
        assertTrue(testTrie.remove("java"));
        assertTrue(testTrie.remove("php"));
        assertEquals(1, testTrie.size());
        assertTrue(testTrie.remove("perl"));
    }

    @Test
    void testEmpty() {
        assertEquals(0, testTrie.size());
        assertFalse(testTrie.remove("something"));
        assertFalse(testTrie.contains("something"));
        assertFalse(testTrie.remove(""));
    }

    @Test
    void testWithCommonPrefix() {
        testTrie.add("abc");
        testTrie.add("abc");
        testTrie.add("abcd");
        testTrie.add("ab");
        testTrie.add("ac");
        testTrie.add("a");
        testTrie.add("");

        assertTrue(testTrie.contains("abc"));
        assertTrue(testTrie.contains(""));
        assertEquals(6, testTrie.size());
        assertFalse(testTrie.contains("bcd"));

        testTrie.remove("ab");
        testTrie.remove("abc");
        testTrie.remove("");

        assertEquals(3, testTrie.size());
        assertTrue(testTrie.contains("a"));
        assertTrue(testTrie.contains("abcd"));
        assertFalse(testTrie.contains(""));
    }

    @Test
    void testHowManyStartsWithPrefix() {
        testTrie.add("abc");
        testTrie.add("abce");
        testTrie.add("abcd");
        testTrie.add("ab");
        testTrie.add("ac");
        testTrie.add("a");
        testTrie.add("");
        testTrie.add("e");
        testTrie.add("eabc");

        assertEquals(9, testTrie.howManyStartsWithPrefix(""));
        assertEquals(6, testTrie.howManyStartsWithPrefix("a"));
        assertEquals(0, testTrie.howManyStartsWithPrefix("abe"));

        testTrie.remove("ab");
        testTrie.remove("abcd");

        assertEquals(2, testTrie.howManyStartsWithPrefix("e"));
        assertEquals(7, testTrie.howManyStartsWithPrefix(""));
        assertEquals(4, testTrie.howManyStartsWithPrefix("a"));
    }

    @Test
    void testSerializeDeserialize() throws IOException {
        testTrie.add("java");
        testTrie.add("javascript");
        testTrie.add("c++");
        testTrie.add("c");

        try (var out = new ByteArrayOutputStream()) {
            testTrie.serialize(out);
            try (var in = new ByteArrayInputStream(out.toByteArray())) {
                testTrie.deserialize(in);

                assertEquals(4, testTrie.size());
                assertTrue(testTrie.contains("java"));
                assertTrue(testTrie.contains("javascript"));
                assertTrue(testTrie.contains("c++"));
                assertTrue(testTrie.contains("c"));
            }
        }
    }

}