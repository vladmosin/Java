package ru.hse.cannon;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

public class Target implements GameObject {
    public static final double RADIUS = 3;

    @NotNull private DoubleVector2 position;
    private boolean isAlive = true;
    @NotNull private Viewer viewer;

    @Override
    public void draw() {
        viewer.drawCircle(position, RADIUS, Color.RED);
    }

    @Override
    public void update(double time) {

    }

    public Target(@NotNull DoubleVector2 position, @NotNull Viewer viewer) {
        this.position = position;
        this.viewer = viewer;
    }

    @NotNull
    public DoubleVector2 getPosition() {
        return position;
    }

    public void destroy() {
        isAlive = false;
    }

    public boolean isActive() {
        return isAlive;
    }
}
