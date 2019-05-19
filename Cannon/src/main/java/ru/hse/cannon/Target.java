package ru.hse.cannon;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/** Implements target in game */
public class Target implements GameObject {

    /** Target radius */
    public static final double RADIUS = 3;

    /** Position of target */
    @NotNull private DoubleVector2 position;

    /** True if object exists*/
    private boolean isAlive = true;

    /** Draws objects */
    @NotNull private Viewer viewer;

    /** Draws a circle */
    @Override
    public void draw() {
        viewer.drawCircle(position, RADIUS, Color.RED);
    }

    @Override
    public void update(double time) {

    }

    /** Creates target at given position with given viewer */
    public Target(@NotNull DoubleVector2 position, @NotNull Viewer viewer) {
        this.position = position;
        this.viewer = viewer;
    }

    @NotNull
    public DoubleVector2 getPosition() {
        return position;
    }


    /** Destroys target */
    public void destroy() {
        isAlive = false;
    }

    /** Returns true if target is active */
    public boolean isActive() {
        return isAlive;
    }
}
