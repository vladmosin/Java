package ru.hse.cannon;

import javafx.scene.canvas.Canvas;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LandScapeTest {
    private static final double EPSILON = 1e-8;
    private final LandScape landScape = new LandScape(new Viewer((new Canvas()).getGraphicsContext2D()));

    @Test
    public void testGetXByY() {
        assertTrue(Math.abs(25 - landScape.getYByX(15)) < EPSILON);
        assertTrue(Math.abs(22 - landScape.getYByX(40)) < EPSILON);
    }

    @Test
    public void testIsInLandScape() {
        assertTrue(landScape.isInLandScape(new DoubleVector2(12, 36)));
        assertFalse(landScape.isInLandScape(new DoubleVector2(112, 6)));
    }
}