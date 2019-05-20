package ru.hse.cannon;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import static java.lang.Math.*;

/** Implementation of cannon for game */
public class Cannon implements GameObject {
    /** Shot power */
    private static final double PROJECTILE_MOMENTUM = 100;

    /** Speed of cannon movement */
    private static final double VELOCITY = 10;

    /** Speed of cannon gun rotation */
    private static final double ROTATION = 1;

    @NotNull private DoubleVector2 position;

    /** Start angle */
    private double gunAngle = PI / 2;

    /** Current projectile type */
    @NotNull private Projectile.Type projectileType = Projectile.Type.SMALL;

    @NotNull private LandScape landScape;
    @NotNull private Target target;
    @NotNull private Viewer viewer;

    public Cannon(double positionX, @NotNull LandScape landScape, @NotNull Viewer viewer, @NotNull Target target) {
        this.position = new DoubleVector2(positionX, landScape.getYByX(positionX));
        this.viewer = viewer;
        this.landScape = landScape;
        this.target = target;
    }

    /** Creates new projectile */
    @NotNull public Projectile fire() {
        return new Projectile(PROJECTILE_MOMENTUM, gunAngle, new DoubleVector2(position),
                projectileType, viewer, target, landScape);
    }

    /** Updates angle */
    public void updateAngle(double time) {
        gunAngle += time * ROTATION;
        angleToAppropriateForm();
    }

    /** Changes projectile type */
    public void changeProjectile() {
        if (projectileType == Projectile.Type.SMALL) {
            projectileType = Projectile.Type.BIG;
        } else {
            projectileType = Projectile.Type.SMALL;
        }
    }

    /** Angle should be in [0, PI], if not transforms angle to nearest value from [0, PI] */
    private void angleToAppropriateForm() {
        if (gunAngle < 0) {
            gunAngle = 0;
        }

        if (gunAngle > Math.PI) {
            gunAngle = Math.PI;
        }
    }

    /** Updates cannon position */
    public void updatePosition(double time) {
        double increase = time * VELOCITY;
        double positionX = position.getX();

        positionX += increase;

        positionX = positionXToNormalForm(positionX);
        double positionY = landScape.getYByX(positionX);

        position = new DoubleVector2(positionX, positionY);
    }

    /** Cannon should be in landscape */
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
        double endOfGunY = -lengthOfGun * sin(gunAngle) + position.getY();

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
