package ru.hse.cannon;

public class Cannon {
    private static double projectileMomentum = 100;
    private static double velocity = 10;
    private static double rotation = 1;

    private DoubleVector2 position;
    private double gunAngle;
    private Projectile.Type projectileType = Projectile.Type.SMALL;
    private LandScape landScape;

    public Projectile fire() {
        return new Projectile(projectileMomentum, gunAngle, position, projectileType);
    }

    public void updateAngle(double time) {
        gunAngle += time * rotation;
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

    private void updatePosition(double time) {

    }
}
