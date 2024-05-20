package application;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import map.TileMap;
import personnages.Player;
import obstacles.Obstacle;

import java.util.Set;

public class GameLoop extends AnimationTimer {

    private final Canvas canvas;
    private final TileMap tileMap;
    private final Player player;
    private final Set<KeyCode> pressedKeys;

    public GameLoop(Canvas canvas, TileMap tileMap, Player player, Set<KeyCode> pressedKeys) {
        this.canvas = canvas;
        this.tileMap = tileMap;
        this.player = player;
        this.pressedKeys = pressedKeys;
    }

    @Override
    public void handle(long now) {
        // Mettre à jour la carte
        tileMap.update();

        // Mettre à jour la position du joueur en fonction des touches pressées
        updatePlayerPosition();

        // Dessiner la carte
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Effacer le canevas
        tileMap.draw(gc);
    }

    private void updatePlayerPosition() {
        double dx = 0;
        double dy = 0;

        if (pressedKeys.contains(KeyCode.UP)) {
            dy -= player.getSpeed();
        }
        if (pressedKeys.contains(KeyCode.DOWN)) {
            dy += player.getSpeed();
        }
        if (pressedKeys.contains(KeyCode.LEFT)) {
            dx -= player.getSpeed();
        }
        if (pressedKeys.contains(KeyCode.RIGHT)) {
            dx += player.getSpeed();
        }

        // Vérifier les collisions avec les obstacles
        double newX = player.getX()*tileMap.getTileSize() + dx;
        double newY = player.getY()*tileMap.getTileSize() + dy;
        Rectangle2D playerBounds = new Rectangle2D(newX, newY, tileMap.getTileSize(), tileMap.getTileSize());

        boolean collisionDetected = false;
        for (Obstacle obstacle : tileMap.getObstacles()) {
            if (obstacle != null && playerBounds.intersects(obstacle.getBounds())) {
                collisionDetected = true;
                break;
            }
        }

        if (!collisionDetected) {
            player.move(dx, dy);
        }
    }
}
