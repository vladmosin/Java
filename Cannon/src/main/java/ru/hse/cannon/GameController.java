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

/** Implements game controller for game */
public class GameController {
    private static final double START_CANNON_X_POSITION = Viewer.WIDTH / 4;
    private static final int FRAMES_AFTER_WIN = 30;
    @NotNull private static final DoubleVector2 START_TARGET_POSITION =
            new DoubleVector2(Viewer.WIDTH / 2, Viewer.HEIGHT / 4);

    @NotNull private Viewer viewer;

    /** Then game ends game controller calls method of this object to show the result for user */
    @NotNull private ScorchedEarth scorchedEarth;

    /** Stores all game objects */
    @NotNull private List<GameObject> gameObjects = new ArrayList<>();

    /** Stores all projectiles */
    @NotNull private List<Projectile> projectiles = new ArrayList<>();

    @NotNull private Cannon cannon;
    @NotNull private Target target;
    @NotNull private LandScape landscape;

    /** If key was pressed, then stores its code, otherwise, stores null */
    private KeyCode pressedKey;

    @NotNull private Timeline timeline = new Timeline();

    /** Frame duration */
    @NotNull private Duration frameDuration = Duration.millis(10);

    /** Stores time of last update */
    private double lastUpdatedNanoTime = System.nanoTime();

    /** Stores number of should be shown frames*/
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

    /** Listens key presses */
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

    /** Checks that key code is in {UP, DOWN, LEFT, RIGHT} */
    private boolean isKeyCodeOfArrow(@NotNull KeyCode keyCode) {
        return keyCode == KeyCode.UP || keyCode == KeyCode.DOWN || keyCode == KeyCode.LEFT || keyCode == KeyCode.RIGHT;
    }

    /** Resets pressed key */
    public void onKeyReleased(@NotNull KeyCode keyCode) {
        if (pressedKey == keyCode) {
            pressedKey = null;
        }
    }

    /** Launches game */
    public void launchGame() {
        timeline.play();
    }

    /** Ends game */
    public void stopGame() {
        timeline.stop();
        scorchedEarth.showScreenWithCongratulations();
    }

    /** Adds new key frame */
    private void addKeyFrame() {
        var keyFrame = new KeyFrame(
               frameDuration,
               event -> {
                    double currentNanoTime = System.nanoTime();
                    double timeIncrease = (currentNanoTime - lastUpdatedNanoTime) * 1e-9;

                    lastUpdatedNanoTime = currentNanoTime;
                    keyCodeMove(timeIncrease);
                    updateState(timeIncrease);

                    if (target.isDestroyed()) {
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

    /** Do action which depends on pressed key*/
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

    /** Clears screen */
    private void clearGraphics() {
        viewer.clearGraphics();
    }

    /** Removes not active game objects */
    private void removeNotActiveGameObjects() {
        gameObjects = gameObjects.stream().filter(GameObject::isActive).collect(Collectors.toList());
        projectiles = projectiles.stream().filter(GameObject::isActive).collect(Collectors.toList());
    }

    /** Updates state of game */
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
