package ru.hse.cannon;

import com.intellij.util.io.Compressor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static java.lang.Math.*;

public class Projectile implements GameObject {
    public enum Type { SMALL, BIG }

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

    private static Map<Type, Information> informationByType = Map.of(
            Type.SMALL, new Information(1, 5, 2),
            Type.BIG, new Information(2, 10, 3)
    );

    private static final double accelerationOfGravity = -9.8;

    private DoubleVector2 position;
    private DoubleVector2 velocity;
    private boolean isActive = true;
    private  Information projectileInformation;

    @NotNull private Target target;
    @NotNull private Viewer viewer;
    @NotNull LandScape landScape;


    @Override
    public void draw() {
        viewer.drawCircle(position, projectileInformation.radius, Color.BLUE);
    }

    @Override
    public void update(double time) {
        position.updateX(time * velocity.getX());
        position.updateY(time * velocity.getY());
        velocity.updateY(time * accelerationOfGravity);

        if (isNearToTarget() || landScape.isNearToLandScape(position) || !landScape.isInLandScape(position)) {
            isActive = false;
        }
    }

    public Projectile(double momentum, double angle, @NotNull DoubleVector2 position, @NotNull Type projectileType,
                      @NotNull Viewer viewer, @NotNull Target target, @NotNull LandScape landScape) {
        projectileInformation = informationByType.get(projectileType);
        this.position = position;
        this.viewer = viewer;
        this.target = target;
        this.landScape = landScape;

        double velocityModule = momentum / projectileInformation.weight;
        velocity = new DoubleVector2(velocityModule * cos(angle), velocityModule * sin(angle));
    }

    public Bang explode() {
        if (isNearToTarget()) {
            target.destroy();
        }

        isActive = false;
        return new Bang(projectileInformation.explodeRadius, new DoubleVector2(position), viewer);
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    private boolean isNearToTarget() {
        if (projectileInformation.explodeRadius + Target.RADIUS >= position.calculateDistance(target.getPosition())) {
            System.out.println(projectileInformation.explodeRadius);
            System.out.println(Target.RADIUS);
            System.out.println(position.getX() + " " + position.getY());
            System.out.println(target.getPosition().getX() + " " + target.getPosition().getY());
            System.out.println(position.calculateDistance(target.getPosition()));
        }
        return projectileInformation.explodeRadius + Target.RADIUS >= position.calculateDistance(target.getPosition());
    }
}
