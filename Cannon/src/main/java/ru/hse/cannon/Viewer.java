package ru.hse.cannon;

import javafx.scene.canvas.GraphicsContext;
import org.jetbrains.annotations.NotNull;

public class Viewer {
    private static final double width = 50;
    private static final double height = 40;

    private PixelMeterTranslation pixelMeterTranslation;
    private GraphicsContext graphicsContext;

    public Viewer(@NotNull GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
        pixelMeterTranslation = new PixelMeterTranslation(width, height, graphicsContext);
    }

    public void drawLine(@NotNull LineHolder lineHolder) {
        double firstPixelX = pixelMeterTranslation.XToPixels(lineHolder.getFirstPoint().getX());
        double firstPixelY = pixelMeterTranslation.YToPixels(lineHolder.getFirstPoint().getY());
        double secondPixelX = pixelMeterTranslation.XToPixels(lineHolder.getSecondPoint().getX());
        double secondPixelY = pixelMeterTranslation.YToPixels(lineHolder.getSecondPoint().getY());

        graphicsContext.strokeLine(firstPixelX, firstPixelY, secondPixelX, secondPixelY);
    }

    public void drawCircle(@NotNull DoubleVector2 center, double radius) {
        double pixelCenterX = pixelMeterTranslation.XToPixels(center.getX());
        double pixelCenterY = pixelMeterTranslation.XToPixels(center.getY());
        double pixelRadius = pixelMeterTranslation.XToPixels(radius);

        graphicsContext.fillOval(pixelCenterX, pixelCenterY, pixelRadius, pixelRadius);
    }
}
