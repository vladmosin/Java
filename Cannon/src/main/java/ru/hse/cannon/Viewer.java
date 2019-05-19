package ru.hse.cannon;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

public class Viewer {
    public static final double WIDTH = 50;
    public static final double HEIGHT = 40;

    @NotNull private PixelMeterTranslation pixelMeterTranslation;
    @NotNull private GraphicsContext graphicsContext;

    public Viewer(@NotNull GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
        pixelMeterTranslation = new PixelMeterTranslation(WIDTH, HEIGHT, graphicsContext);
    }

    public void drawLine(@NotNull LineHolder lineHolder, @NotNull Color color) {
        double firstPixelX = pixelMeterTranslation.XToPixels(lineHolder.getFirstPoint().getX());
        double firstPixelY = pixelMeterTranslation.YToPixels(lineHolder.getFirstPoint().getY());
        double secondPixelX = pixelMeterTranslation.XToPixels(lineHolder.getSecondPoint().getX());
        double secondPixelY = pixelMeterTranslation.YToPixels(lineHolder.getSecondPoint().getY());

        graphicsContext.setStroke(color);
        graphicsContext.strokeLine(firstPixelX, firstPixelY, secondPixelX, secondPixelY);
    }

    public void drawCircle(@NotNull DoubleVector2 center, double radius, @NotNull Color color) {
        double pixelCenterX = pixelMeterTranslation.XToPixels(center.getX());
        double pixelCenterY = pixelMeterTranslation.XToPixels(center.getY());
        double pixelRadius = pixelMeterTranslation.XToPixels(radius);

        graphicsContext.setStroke(color);
        graphicsContext.fillOval(pixelCenterX, pixelCenterY, pixelRadius, pixelRadius);
        graphicsContext.strokeOval(pixelCenterX, pixelCenterY, pixelRadius, pixelRadius);
    }

    public void clearGraphics() {
        graphicsContext.clearRect(0, 0, graphicsContext.getCanvas().getWidth(),
                graphicsContext.getCanvas().getHeight());
    }
}
