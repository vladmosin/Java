package ru.hse.cannon;

import javafx.scene.canvas.GraphicsContext;

public interface GameObject {
    void draw();
    void update(double time);
    boolean isActive();
}
