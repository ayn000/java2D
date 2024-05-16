package application;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import map.TileMap;
import personnages.Player;

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
        if (pressedKeys.contains(KeyCode.UP)) {
            player.move(0, -1);
        }
        if (pressedKeys.contains(KeyCode.DOWN)) {
            player.move(0, 1);
        }
        if (pressedKeys.contains(KeyCode.LEFT)) {
            player.move(-1, 0);
        }
        if (pressedKeys.contains(KeyCode.RIGHT)) {
            player.move(1, 0);
        }
    }
}
