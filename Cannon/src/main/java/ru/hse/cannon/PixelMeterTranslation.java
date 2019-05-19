package ru.hse.cannon;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.jetbrains.annotations.NotNull;

public class PixelMeterTranslation {
    private double width;
    private double height;
    private GraphicsContext graphicsContext;

    public PixelMeterTranslation(double width, double height, @NotNull GraphicsContext graphicsContext) {
        this.width = width;
        this.height = height;
    }

    private double getPixelHeight() {
        return graphicsContext.getCanvas().getHeight();
    }

    private double getPixelWidth() {
        return graphicsContext.getCanvas().getWidth();
    }

    public double pixelsToY(double pixels) {
        return getPixelHeight() / pixels * height;
    }

    public double pixelsToX(double pixels) {
        return getPixelWidth() / pixels * width;
    }

    public double XToPixels(double x) {
        return x / width * getPixelWidth();
    }

    public double YToPixels(double y) {
        return y / height * getPixelHeight();
    }
}
