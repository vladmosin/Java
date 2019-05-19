package ru.hse.cannon;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LandScape implements GameObject {
    @NotNull private List<LineHolder> lines = new ArrayList<>();
    @NotNull private Viewer viewer;

    @Override
    public void draw() {
        for (var line : lines) {
            viewer.drawLine(line);
        }
    }

    @Override
    public void update(double time) {

    }

    public double getYByX(double x) {
        for (var line : lines) {
            if (line.isXOnLine(x)) {
                return line.getYByX(x);
            }
        }

        throw new IllegalArgumentException("Illegal x = " + x);
    }

    public LandScape(@NotNull Viewer viewer) {
        lines.add(new LineHolder(new DoubleVector2(0, 10), new DoubleVector2(25, 30)));
        lines.add(new LineHolder(new DoubleVector2(25, 30), new DoubleVector2(50, 15)));
        this.viewer = viewer;
    }
}