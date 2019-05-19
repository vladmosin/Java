package ru.hse.cannon;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import org.jetbrains.annotations.NotNull;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameController {
    private static final double START_CANNON_X_POSITION = Viewer.WIDTH / 4;
    private static final int FRAMES_AFTER_WIN = 30;
    @NotNull private static final DoubleVector2 START_TARGET_POSITION =
            new DoubleVector2(3 * Viewer.WIDTH / 4, 3 * Viewer.HEIGHT / 4);

    @NotNull private Viewer viewer;
    @NotNull private ScorchedEarth scorchedEarth;

    @NotNull private List<GameObject> gameObjects = new ArrayList<>();
    @NotNull private List<Projectile> projectiles = new ArrayList<>();

    @NotNull private Cannon cannon;
    @NotNull private Target target;
    @NotNull private LandScape landscape;

    private KeyCode pressedKey;

    @NotNull private Timeline timeline = new Timeline();
    @NotNull private Duration frameDuration = Duration.millis(33);

    private double lastUpdatedNanoTime = System.nanoTime();
    private double framesLeft = FRAMES_AFTER_WIN;

    public GameController(@NotNull ScorchedEarth mainGame, @NotNull GraphicsContext graphicsContext) {
        viewer = new Viewer(graphicsContext);
        scorchedEarth = mainGame;

        landscape = new LandScape(viewer);
        target = new Target(START_TARGET_POSITION, viewer);
        cannon = new Cannon(START_CANNON_X_POSITION,landscape, viewer, target);

        gameObjects.add(landscape);
        gameObjects.add(target);
        gameObjects.add(cannon);

        timeline.setCycleCount(Timeline.INDEFINITE);
        addKeyFrame();
    }

    public void onKeyPressed(@NotNull KeyCode keyCode) {
        if (keyCode == KeyCode.SHIFT) {
            cannon.changeProjectile();
        } else if (keyCode == KeyCode.ENTER) {
            var projectile = cannon.fire();
            projectiles.add(projectile);
            gameObjects.add(projectile);
        } else if (isKeyCodeOfArrow(keyCode)) {
            pressedKey = keyCode;
        }
    }

    private boolean isKeyCodeOfArrow(@NotNull KeyCode keyCode) {
        return keyCode == KeyCode.UP || keyCode == KeyCode.DOWN || keyCode == KeyCode.LEFT || keyCode == KeyCode.RIGHT;
    }
    
    public void onKeyReleased(@NotNull KeyCode keyCode) {
        if (pressedKey == keyCode) {
            pressedKey = null;
        }
    }

    public void launchGame() {
        timeline.play();
    }

    public void stopGame() {
        timeline.stop();
        scorchedEarth.showScreenWithCongratulations();
    }

    private void addKeyFrame() {
        var keyFrame = new KeyFrame(
               frameDuration,
               event -> {
                    double currentNanoTime = System.nanoTime();
                    double timeIncrease = (currentNanoTime - lastUpdatedNanoTime) * 1e-9;

                    lastUpdatedNanoTime = currentNanoTime;
                    keyCodeMove(timeIncrease);
                    updateState(timeIncrease);

                    if (!target.isActive()) {
                        if (framesLeft <= 0) {
                            stopGame();
                        } else {
                            framesLeft--;
                        }
                    }
               }
        );

        timeline.getKeyFrames().add(keyFrame);
    }

    private void keyCodeMove(double timeIncrease) {
        if (pressedKey == null) {
            return;
        }

        if (pressedKey == KeyCode.RIGHT) {
            cannon.updatePosition(timeIncrease);
        } else if (pressedKey == KeyCode.LEFT) {
            cannon.updatePosition(-timeIncrease);
        } else if (pressedKey == KeyCode.UP) {
            cannon.updateAngle(timeIncrease);
        } else if (pressedKey == KeyCode.DOWN) {
            cannon.updateAngle(-timeIncrease);
        }
    }

    private void clearGraphics() {
        viewer.clearGraphics();
    }

    private void removeNotActiveGameObjects() {
        gameObjects = gameObjects.stream().filter(GameObject::isActive).collect(Collectors.toList());
        projectiles = projectiles.stream().filter(GameObject::isActive).collect(Collectors.toList());
    }

    private void updateState(double timeIncrease) {
        clearGraphics();
        removeNotActiveGameObjects();

        for (var gameObject : gameObjects) {
            gameObject.update(timeIncrease);
            gameObject.draw();
        }

        for (var projectile : projectiles) {
            if (!projectile.isActive()) {
                gameObjects.add(projectile.explode());
            }
        }
    }
}
