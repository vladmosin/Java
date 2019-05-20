package ru.hse.cannon;

import javafx.scene.canvas.GraphicsContext;
import org.jetbrains.annotations.NotNull;

/** Translates pixels to meters and conversely */
public class PixelMeterTranslation {
    /** Width in meters */
    private double width;

    /** Height in meters*/
    private double height;

    /** Graphics context */
    @NotNull private GraphicsContext graphicsContext;

    public PixelMeterTranslation(double width, double height, @NotNull GraphicsContext graphicsContext) {
        this.width = width;
        this.height = height;
        this.graphicsContext = graphicsContext;
    }

    private double getPixelHeight() {
        return graphicsContext.getCanvas().getHeight();
    }

    private double getPixelWidth() {
        return graphicsContext.getCanvas().getWidth();
    }

    /** Convert Y position in pixels to Y position in meters */
    public double pixelsToY(double pixels) {
        return getPixelHeight() / pixels * height;
    }

    /** Convert X position in pixels to X position in meters */
    public double pixelsToX(double pixels) {
        return getPixelWidth() / pixels * width;
    }

    /** Convert X position in meters to Y position in pixels */
    public double XToPixels(double x) {
        return x / width * getPixelWidth();
    }

    /** Convert Y position in meters to Y position in pixels */
    public double YToPixels(double y) {
        return y / height * getPixelHeight();
    }
}
