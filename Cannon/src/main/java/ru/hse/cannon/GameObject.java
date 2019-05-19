package ru.hse.cannon;

import javafx.scene.canvas.GraphicsContext;

public interface GameObject {
    void draw(GraphicsContext graphicsContext);
    void update(double time);
}
