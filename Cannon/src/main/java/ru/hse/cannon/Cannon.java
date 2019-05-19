package ru.hse.cannon;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import static java.lang.Math.*;

public class Cannon implements GameObject {
    private static final double PROJECTILE_MOMENTUM = 100;
    private static final double VELOCITY = 10;
    private static final double ROTATION = 1;

    private DoubleVector2 position;
    private double gunAngle = 0;
    private Projectile.Type projectileType = Projectile.Type.SMALL;
    private LandScape landScape;
    private Target target;
    @NotNull private Viewer viewer;

    public Cannon(double positionX, @NotNull LandScape landScape, @NotNull Viewer viewer, @NotNull Target target) {
        this.position = new DoubleVector2(positionX, landScape.getYByX(positionX));
        this.viewer = viewer;
        this.landScape = landScape;
        this.target = target;
    }

    @NotNull public Projectile fire() {
        return new Projectile(PROJECTILE_MOMENTUM, gunAngle, new DoubleVector2(position),
                projectileType, viewer, target, landScape);
    }

    public void updateAngle(double time) {
        gunAngle += time * ROTATION;
        angleToAppropriateForm();
    }

    public void changeProjectile() {
        if (projectileType == Projectile.Type.SMALL) {
            projectileType = Projectile.Type.BIG;
        } else {
            projectileType = Projectile.Type.SMALL;
        }
    }

    private void angleToAppropriateForm() {
        if (gunAngle < 0) {
            gunAngle = 0;
        }

        if (gunAngle > Math.PI) {
            gunAngle = Math.PI;
        }
    }

    public void updatePosition(double time) {
        double increase = time * VELOCITY;
        double positionX = position.getX();

        positionX += increase;

        positionX = positionXToNormalForm(positionX);
        double positionY = landScape.getYByX(positionX);

        position = new DoubleVector2(positionX, positionY);
    }

    private double positionXToNormalForm(double positionX) {
        if (positionX < 0) {
            positionX = 0;
        }

        if (positionX > Viewer.WIDTH) {
            positionX = Viewer.WIDTH;
        }

        return positionX;
    }

    @Override
    public void draw() {
        viewer.drawLine(new LineHolder(new DoubleVector2(position.getX() - 1, position.getY()),
                                       new DoubleVector2(position.getX() + 1, position.getY())),
                        Color.BLACK);
        double lengthOfGun = 2;
        double endOfGunX = lengthOfGun * cos(gunAngle) + position.getX();
        double endOfGunY = lengthOfGun * sin(gunAngle) + position.getY();

        viewer.drawLine(new LineHolder(position, new DoubleVector2(endOfGunX, endOfGunY)), Color.BLACK);
    }

    @Override
    public void update(double time) {

    }

    @Override
    public boolean isActive() {
        return true;
    }
}
