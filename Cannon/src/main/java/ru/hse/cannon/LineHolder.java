package ru.hse.cannon;

import org.jetbrains.annotations.NotNull;
import static java.lang.Math.*;

/** Stores linear function */
public class LineHolder {
    private static double EPSILON = 1;

    /** Two points of line */
    @NotNull private DoubleVector2 firstPoint;
    @NotNull private DoubleVector2 secondPoint;

    public LineHolder(@NotNull DoubleVector2 point1, @NotNull DoubleVector2 point2) {
        if (point1.getX() < point2.getX()) {
            firstPoint = point1;
            secondPoint = point2;
        } else {
            firstPoint = point2;
            secondPoint = point1;
        }
    }

    /** Checks that x is on line */
    public boolean isXOnLine(double x) {
        return x >= firstPoint.getX() && x <= secondPoint.getX();
    }

    /** Returns y by x with checking domain */
    public double getYByX(double x) {
        if (!isXOnLine(x)) {
            throw new IllegalArgumentException("Given x is not on line");
        }

        return getYByXWithoutCheckingDomain(x);
    }

    /** Returns y by x */
    private double getYByXWithoutCheckingDomain(double x) {
        double x1 = firstPoint.getX();
        double x2 = secondPoint.getX();

        double y1 = firstPoint.getY();
        double y2 = secondPoint.getY();

        return y1 + (x - x1) / (x2 - x1) * (y2 - y1);
    }

    /** Returns point with smaller x */
    @NotNull
    public DoubleVector2 getFirstPoint() {
        return firstPoint;
    }

    /** Returns point with greater x */
    @NotNull
    public DoubleVector2 getSecondPoint() {
        return secondPoint;
    }

    /** Checks that point is near to line */
    public boolean isNearToLine(@NotNull DoubleVector2 position) {
        if (firstPoint.getX() > position.getX() || secondPoint.getX() < position.getX()) {
            return false;
        }

        return calculateDistanceToPoint(position) < EPSILON;
    }

    /** Returns coefficient k of line (y = kx + b)*/
    public double getAngleOfInclination() {
        double x1 = firstPoint.getX();
        double y1 = firstPoint.getY();
        double x2 = firstPoint.getX();
        double y2 = firstPoint.getY();

        return tan((y2 - y1) / (x2 - x1));
    }

    /** Returns coefficient b of line (y = kx + b)*/
    public double getFreeCoefficient() {
        return getYByXWithoutCheckingDomain(0);
    }

    /** Returns distance from line to point */
    public double calculateDistanceToPoint(@NotNull DoubleVector2 position) {
        double k = getAngleOfInclination();
        double b = getFreeCoefficient();

        double x = position.getX();
        double y = position.getY();

        return (abs(k * x + b - y)) / sqrt(k * k + 1);
    }
}
