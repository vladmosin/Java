package ru.hse.cannon;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

public class Bang implements GameObject {
    private double radius;
    private DoubleVector2 position;
    @NotNull private Viewer viewer;

    @Override
    public void draw() {
        viewer.drawCircle(position, radius, Color.RED);
    }

    @Override
    public void update(double time) {

    }

    @Override
    public boolean isActive() {
        return true;
    }

    public Bang(double radius, DoubleVector2 position, @NotNull Viewer viewer) {
        this.radius = radius;
        this.position = position;
        this.viewer = viewer;
    }

    public double getRadius() {
        return radius;
    }

    public DoubleVector2 getPosition() {
        return position;
    }
}
