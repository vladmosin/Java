package ru.hse.cannon;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/** Implementation of landscape */
public class LandScape implements GameObject {
    /** Ground lines */
    @NotNull private List<LineHolder> lines = new ArrayList<>();

    /** Viewer */
    @NotNull private Viewer viewer;

    /** Draws object */
    @Override
    public void draw() {
        for (var line : lines) {
            viewer.drawLine(line, Color.GREEN);
        }
    }

    /** Checks that point is near to landscape */
    public boolean isNearToLandScape(@NotNull DoubleVector2 position) {
        for (var line : lines) {
            if (line.isNearToLine(position)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void update(double time) {

    }

    @Override
    public boolean isActive() {
        return true;
    }

    /** Returns y position by x */
    public double getYByX(double x) {
        for (var line : lines) {
            if (line.isXOnLine(x)) {
                return line.getYByX(x);
            }
        }

        throw new IllegalArgumentException("Illegal x = " + x);
    }

    public LandScape(@NotNull Viewer viewer) {
        lines.add(new LineHolder(new DoubleVector2(0, 35), new DoubleVector2(10, 25)));
        lines.add(new LineHolder(new DoubleVector2(10, 25), new DoubleVector2(20, 25)));
        lines.add(new LineHolder(new DoubleVector2(20, 25), new DoubleVector2(30, 38)));
        lines.add(new LineHolder(new DoubleVector2(30, 38), new DoubleVector2(40, 22)));
        lines.add(new LineHolder(new DoubleVector2(40, 22), new DoubleVector2(50, 30)));
        this.viewer = viewer;
    }

    /** Checks that point is in landscape */
    public boolean isInLandScape(@NotNull DoubleVector2 position) {
        return !(position.getX() < 0 || position.getX() > Viewer.WIDTH
                || position.getY() < 0 || position.getY() > Viewer.HEIGHT);
    }
}
