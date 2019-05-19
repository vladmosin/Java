package ru.hse.cannon;

import javafx.scene.canvas.GraphicsContext;

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


    @Override
    public void draw(GraphicsContext graphicsContext) {

    }

    @Override
    public void update(double time) {
        position.updateX(time * velocity.getX());
        position.updateY(time * velocity.getY());
        velocity.updateY(time * accelerationOfGravity);
    }

    public Projectile(double momentum, double angle, DoubleVector2 position, Type projectileType) {
        projectileInformation = informationByType.get(projectileType);
        this.position = position;

        double velocityModule = momentum / projectileInformation.weight;
        velocity = new DoubleVector2(velocityModule * cos(angle), velocityModule * sin(angle));
    }

    public Bang explode() {
        isActive = false;
        return new Bang(projectileInformation.explodeRadius, new DoubleVector2(position));
    }

    public boolean isActive() {
        return isActive;
    }
}
