package ru.hse.cannon;

import javafx.scene.canvas.GraphicsContext;

public class Bang implements GameObject {
    private double radius;
    private DoubleVector2 position;

    @Override
    public void draw(GraphicsContext graphicsContext) {

    }

    @Override
    public void update(double time) {

    }

    public Bang(double radius, DoubleVector2 position) {
        this.radius = radius;
        this.position = position;
    }

    public double getRadius() {
        return radius;
    }

    public DoubleVector2 getPosition() {
        return position;
    }
}
