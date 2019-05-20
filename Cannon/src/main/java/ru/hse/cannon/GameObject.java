package ru.hse.cannon;

public interface GameObject {
    /** Draws object */
    void draw();

    /** Updates object */
    void update(double time);

    /** Checks that object is alive */
    boolean isActive();
}
