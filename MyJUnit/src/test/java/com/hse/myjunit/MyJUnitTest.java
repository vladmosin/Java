package com.hse.myjunit;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyJUnitTest {
    private TestLauncher launcher = new TestLauncher();

    @Test
    public void runSimpleTests() throws ExecutionException, InterruptedException {
        String result = launcher.runTests(Collections.singletonList(SimpleTests.class));
        var expectedResult = Arrays.asList(
                "throwsUnexpectedTest FAILED Exception java.lang.reflect.InvocationTargetException was thrown instead of java.io.FileNotFoundException",
                "emptyTest PASSED",
                "ignoringTest IGNORED Ignored: hard test",
                "failingTest FAILED Exception java.lang.reflect.InvocationTargetException was thrown instead of com.hse.myjunit.annotations.Test$NoException",
                "throwsExpectedTest PASSED",
                "passingTest PASSED");

        var testResults = result.split("\n");

        assertEquals(expectedResult.size(), testResults.length - 1);

        var foundResult = Arrays.asList(testResults[1], testResults[2], testResults[3], testResults[4], testResults[5], testResults[6]);

        assertArrayEquals(expectedResult.stream().sorted().toArray(), foundResult.stream().sorted().toArray());
    }

    @Test
    public void testBeforeAfter() throws ExecutionException, InterruptedException {
        assertEquals("test PASSED",
                launcher.runTests(Collections.singletonList(BeforeAfterTests.class)).split("\n")[1]);
    }
}
