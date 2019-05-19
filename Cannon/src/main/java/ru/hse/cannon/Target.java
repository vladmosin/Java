package ru.hse.cannon;

import javafx.scene.canvas.GraphicsContext;

public class Target implements GameObject {
    private static double radius = 3;

    private DoubleVector2 position;
    private boolean isAlive = true;

    @Override
    public void draw(GraphicsContext graphicsContext) {

    }

    @Override
    public void update(double time) {

    }

    public Target(DoubleVector2 position) {
        this.position = position;
    }

    public DoubleVector2 getPosition() {
        return position;
    }

    public void destroy() {
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }
}
