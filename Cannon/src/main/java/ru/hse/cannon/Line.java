package ru.hse.cannon;

import org.jetbrains.annotations.NotNull;

/** Stores linear function */
public class Line {
    @NotNull private DoubleVector2 firstPoint;
    @NotNull private DoubleVector2 secondPoint;

    public Line(@NotNull DoubleVector2 point1, @NotNull DoubleVector2 point2) {
        if (point1.getX() < point2.getX()) {
            firstPoint = point1;
            secondPoint = point2;
        } else {
            firstPoint = point2;
            secondPoint = point1;
        }
    }

    public boolean isXOnLine(double x) {
        return x >= firstPoint.getX() && x <= secondPoint.getX();
    }

    public double getYByX(double x) {
        if (!isXOnLine(x)) {
            throw new IllegalArgumentException("Given x is not on line");
        }

        double x1 = firstPoint.getX();
        double x2 = secondPoint.getX();

        double y1 = firstPoint.getY();
        double y2 = secondPoint.getY();

        return y1 + (x - x1) / (x2 - x1) * (y2 - y1);
    }
}
