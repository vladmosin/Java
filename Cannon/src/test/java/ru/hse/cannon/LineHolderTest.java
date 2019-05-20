package ru.hse.cannon;

import org.junit.jupiter.api.Test;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static org.junit.jupiter.api.Assertions.*;

class LineHolderTest {
    private static double EPSILON = 1e-8;

    @Test
    public void testDistanceToLine() {
        var line = new LineHolder(new DoubleVector2(0, 0), new DoubleVector2(100, 100));

        var point1 = new DoubleVector2(0, 2);
        var point2 = new DoubleVector2(1, 1);

        assertTrue(abs(line.calculateDistanceToPoint(point1) - sqrt(2)) < EPSILON);
        assertTrue(abs(line.calculateDistanceToPoint(point2)) < EPSILON);
    }

    @Test
    public void testAngleOfInclination() {
        var line1 = new LineHolder(new DoubleVector2(0, 0), new DoubleVector2(100, 100));
        var line2 = new LineHolder(new DoubleVector2(1, 2), new DoubleVector2(4, 8));

        assertTrue(abs(line1.getAngleOfInclination() - 1) < EPSILON);
        assertTrue(abs(line2.getAngleOfInclination() - 2) < EPSILON);
    }

    @Test
    public void testGetFreeCoefficient() {
        var line1 = new LineHolder(new DoubleVector2(0, 0), new DoubleVector2(100, 100));
        var line2 = new LineHolder(new DoubleVector2(0, 2), new DoubleVector2(4, 8));

        assertTrue(abs(line1.getFreeCoefficient()) < EPSILON);
        assertTrue(abs(line2.getFreeCoefficient() - 2) < EPSILON);
    }
}