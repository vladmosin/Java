package com.hse.myjunit;

import com.hse.myjunit.annotations.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTests {
    @Test
    public void emptyTest() {}

    @Test
    public void passingTest() {
        int x = 5 + 4;
        assertEquals(9, x);
    }

    @Test
    public void failingTest() {
        assertEquals(5, 3);
    }

    @Test(ignore = "hard test")
    public void ignoringTest() {}

    @Test(expected = IllegalArgumentException.class)
    public void throwsExpectedTest() {
        throw new IllegalArgumentException("Expected");
    }

    @Test(expected = FileNotFoundException.class)
    public void throwsUnexpectedTest() {
        throw new IllegalArgumentException("Unexpected");
    }
}
