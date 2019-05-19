package ru.hse.cannon;

import org.jetbrains.annotations.NotNull;
import static java.lang.Math.*;

public class DoubleVector2 {
    private double x;
    private double y;

    public DoubleVector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public DoubleVector2(DoubleVector2 position) {
        x = position.x;
        y = position.y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void updateX(double increase) {
        x += increase;
    }

    public void updateY(double increase) {
        y += increase;
    }

    public double calculateDistance(@NotNull DoubleVector2 position) {
        return sqrt(pow(position.x - x, 2) + pow(position.y - y, 2));
    }
}
