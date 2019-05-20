package ru.hse.cannon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleVector2Test {
    private static double EPSILON = 1e-8;
    @Test
    public void testCalculateDistance() {
        var point1 = new DoubleVector2(1, 2);
        var point2 = new DoubleVector2(6, -10);
        assertTrue(Math.abs(13 - point1.calculateDistance(point2)) < EPSILON);
        assertTrue(Math.abs(13 - point2.calculateDistance(point1)) < EPSILON);

        point1 = new DoubleVector2(6, 12);
        point2 = new DoubleVector2(6, 8);

        assertTrue(Math.abs(4 - point1.calculateDistance(point2)) < EPSILON);
        assertTrue(Math.abs(4 - point2.calculateDistance(point1)) < EPSILON);
    }
}