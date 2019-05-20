package ru.hse.cannon;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/** Implementation of projectile for game */
public class Projectile implements GameObject {
    /** Projectile type */
    public enum Type { SMALL, BIG }

    /** Stores type dependent information */
    private static class Information {
        private double radius;
        private double weight;
        private double explodeRadius;

        private Information(double radius, double weight, double explodeRadius) {
            this.radius = radius;
            this.explodeRadius = explodeRadius;
            this.weight = weight;
        }
    }

    /** Stores type dependent information by type */
    private static Map<Type, Information> informationByType = Map.of(
            Type.SMALL, new Information(0.3, 2, 0.8),
            Type.BIG, new Information(1, 5, 2)
    );

    private static final double accelerationOfGravity = 9.8;

    /** Position */
    @NotNull private DoubleVector2 position;

    /** Velocity */
    @NotNull private DoubleVector2 velocity;

    private boolean isActive = true;

    /** Stores type dependent characteristics */
    @NotNull private  Information projectileInformation;

    @NotNull private Target target;
    @NotNull private Viewer viewer;
    @NotNull private LandScape landScape;


    @Override
    public void draw() {
        viewer.drawCircle(new DoubleVector2(
                position.getX() - projectileInformation.radius / 2, position.getY()),
                projectileInformation.radius, Color.BLUE);
    }

    /** Projectile moving */
    @Override
    public void update(double time) {
        position.updateX(time * velocity.getX());
        position.updateY(time * velocity.getY());
        velocity.updateY(time * accelerationOfGravity);

        if (isNearToTarget() || !landScape.isInLandScape(position)) {
            isActive = false;
        }
    }

    /** Creates new projectile
     * @param momentum momentum of projectile, needed for finding velocity
     * @param angle start angle of moving
     * */
    public Projectile(double momentum, double angle, @NotNull DoubleVector2 position, @NotNull Type projectileType,
                      @NotNull Viewer viewer, @NotNull Target target, @NotNull LandScape landScape) {
        projectileInformation = informationByType.get(projectileType);
        this.position = position;
        this.viewer = viewer;
        this.target = target;
        this.landScape = landScape;

        double velocityModule = momentum / projectileInformation.weight;
        velocity = new DoubleVector2(velocityModule * cos(angle), -velocityModule * sin(angle));
    }

    /** Returns result of explosion */
    @NotNull public Bang explode() {
        if (isNearToTarget()) {
            target.destroy();
        }

        isActive = false;
        return new Bang(projectileInformation.explodeRadius, new DoubleVector2(position), viewer);
    }

    /** Returns true if projectile is active */
    @Override
    public boolean isActive() {
        return isActive;
    }

    /** Returns true if projectile is near to target */
    private boolean isNearToTarget() {
        return projectileInformation.explodeRadius + Target.HIT_RADIUS >=
                position.calculateDistance(target.getPosition());
    }
}
