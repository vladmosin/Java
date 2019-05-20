package ru.hse.cannon;

import org.jetbrains.annotations.NotNull;
import static java.lang.Math.*;

/** Stores coordinates of 2D point */
public class DoubleVector2 {
    private double x;
    private double y;

    public DoubleVector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public DoubleVector2(@NotNull DoubleVector2 position) {
        x = position.x;
        y = position.y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /** Adds increase to x */
    public void updateX(double increase) {
        x += increase;
    }

    /** Adds increase to y */
    public void updateY(double increase) {
        y += increase;
    }

    /** Calculates distance between two points */
    public double calculateDistance(@NotNull DoubleVector2 position) {
        return sqrt(pow(position.x - x, 2) + pow(position.y - y, 2));
    }
}
