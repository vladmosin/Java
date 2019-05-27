package com.hse.myjunit;

import com.hse.myjunit.annotations.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeforeAfterTests {
    private int N = 0;

    @BeforeClass
    public void setN() {
        N = 10;
    }

    @Before
    public void increaseN() {
        N++;
    }

    @After
    public void decreaseN() {
        N--;
    }

    @AfterClass
    public void nullifyN() {
        N = 0;
    }

    @Test
    public void test() {
        assertEquals(11, N);
    }
}
