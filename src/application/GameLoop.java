package application;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.geometry.Rectangle2D;
import map.TileMap;
import map.World;
import personnages.Player;
import obstacles.Obstacle;
import objet.GameObject;
import personnages.Enemy;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class GameLoop extends AnimationTimer {

    private final Canvas canvas;
    private final List<World> worlds;
    private int currentWorldIndex;
    private final Set<KeyCode> pressedKeys;
    private long lastHealthUpdate = 0;
    private static final long HEALTH_UPDATE_INTERVAL = 500_000_000;
    private final Runnable onPlayerDeath;
    private Label healthLabel;
    private HBox inventoryDisplay;

    public GameLoop(Canvas canvas, List<World> worlds, Set<KeyCode> pressedKeys, Runnable onPlayerDeath, Label healthLabel, HBox inventoryDisplay) {
        this.canvas = canvas;
        this.worlds = worlds;
        this.currentWorldIndex = 0;
        this.pressedKeys = pressedKeys;
        this.onPlayerDeath = onPlayerDeath;
        this.healthLabel = healthLabel;
        this.inventoryDisplay = inventoryDisplay;
    }

    @Override
    public void handle(long now) {
        // Mettre à jour la carte
        getCurrentTileMap().update();

        // Mettre à jour la position du joueur en fonction des touches pressées
        updatePlayerPosition(now);
        handlePlayerAttack();
        handleObjectCollection();
        handleWorldTransition();

        // Dessiner la carte
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Effacer le canevas
        getCurrentTileMap().draw(gc);

        // Mettre à jour l'interface utilisateur
        updateHealthDisplay();
        updateInventoryDisplay();

        if (getCurrentPlayer().isDead()) {
            stop(); // Arrêter la boucle de jeu
            onPlayerDeath.run(); // Redémarrer le jeu
        }
    }

    private void handleWorldTransition() {
        boolean worldChanged = false;

        // Exemple simple de transition : si le joueur atteint le bord droit de la carte, passer au monde suivant
        if (getCurrentPlayer().getX() >= TileMap.getMapWidth() - 1) {
            currentWorldIndex = (currentWorldIndex + 1) % worlds.size();
            getCurrentPlayer().setX(1); // Réinitialiser la position du joueur
            getCurrentPlayer().setY(1); // Réinitialiser la position du joueur
            worldChanged = true;
        }

        // Exemple simple de retour : si le joueur atteint le bord gauche de la carte, revenir au monde précédent
        if (getCurrentPlayer().getX() < 1) {
            currentWorldIndex = (currentWorldIndex - 1 + worlds.size()) % worlds.size();
            getCurrentPlayer().setX(TileMap.getMapWidth() - 2); // Réinitialiser la position du joueur
            getCurrentPlayer().setY(1); // Réinitialiser la position du joueur
            worldChanged = true;
        }

        if (worldChanged) {
            System.out.println("World changed to: " + currentWorldIndex);
            // Recharger les éléments du nouveau monde
            getCurrentTileMap().update();
        }
    }

    
    private void handlePlayerAttack() {
        if (pressedKeys.contains(KeyCode.E)) {
            System.out.println("Attack key pressed");
            Rectangle2D playerBounds = getCurrentPlayer().getBounds();
            System.out.println("Player bounds: " + playerBounds);

            for (Enemy enemy : getCurrentTileMap().getEnemies()) {
                Rectangle2D enemyBounds = enemy.getBounds();
                System.out.println("Checking collision with enemy at (" + enemy.getX() + ", " + enemy.getY() + ")");
                System.out.println("Enemy bounds: " + enemyBounds);

                if (playerBounds.intersects(enemyBounds)) {
                    System.out.println("Collision detected with enemy");
                    getCurrentPlayer().attackEnemy(enemy);
                    if (enemy.getHealth() <= 0) {
                        System.out.println("Enemy defeated!");
                    } else {
                        System.out.println("Enemy health: " + enemy.getHealth());
                    }
                } else {
                    System.out.println("No collision detected with enemy at (" + enemy.getX() + ", " + enemy.getY() + ")");
                }
            }
            pressedKeys.remove(KeyCode.E); // Empêche l'attaque continue
        }
    }
    private void handleObjectCollection() {
        Iterator<GameObject> iterator = getCurrentTileMap().getGameObjects().iterator();
        while (iterator.hasNext()) {
            GameObject gameObject = iterator.next();
            if (getCurrentPlayer().getBounds().intersects(gameObject.getBounds())) {
                gameObject.onPlayerContact(getCurrentPlayer());
                iterator.remove(); // Supprimer l'objet de la carte après collecte
                getCurrentPlayer().getInventory().applyItemEffects(getCurrentPlayer()); // Appliquer les effets des objets
            }
        }
    }

    private void updatePlayerPosition(long now) {
        double dx = 0;
        double dy = 0;

        if (pressedKeys.contains(KeyCode.UP)) {
            dy -= getCurrentPlayer().getSpeed();
        }
        if (pressedKeys.contains(KeyCode.DOWN)) {
            dy += getCurrentPlayer().getSpeed();
        }
        if (pressedKeys.contains(KeyCode.LEFT)) {
            dx -= getCurrentPlayer().getSpeed();
        }
        if (pressedKeys.contains(KeyCode.RIGHT)) {
            dx += getCurrentPlayer().getSpeed();
        }

        // Vérifier les collisions avec les obstacles
        double newX = getCurrentPlayer().getX() + dx;
        double newY = getCurrentPlayer().getY() + dy;
        Rectangle2D playerBounds = new Rectangle2D(newX * getCurrentTileMap().getTileSize(), newY * getCurrentTileMap().getTileSize(), getCurrentTileMap().getTileSize(), getCurrentTileMap().getTileSize());

        boolean collisionDetected = false;
        for (Obstacle obstacle : getCurrentTileMap().getObstacles()) {
            if (obstacle != null && playerBounds.intersects(obstacle.getBounds())) {
                collisionDetected = true;
                break;
            }
        }
        if (!collisionDetected) {
            for (Enemy enemy : getCurrentTileMap().getEnemies()) {
                if (playerBounds.intersects(enemy.getBounds())) {
                    collisionDetected = true;
                    if (now - lastHealthUpdate >= HEALTH_UPDATE_INTERVAL) {
                        getCurrentPlayer().decreaseHealth(enemy.getAttack());
                        System.out.println("Player health: " + getCurrentPlayer().getHealth());
                        lastHealthUpdate = now;
                    }
                    break;
                }
            }
        }

        if (!collisionDetected) {
            getCurrentPlayer().move(dx, dy);
        }
    }

    private void updateHealthDisplay() {
        healthLabel.setText("Health: " + getCurrentPlayer().getHealth());
    }

    private void updateInventoryDisplay() {
        inventoryDisplay.getChildren().clear();
        Map<String, Integer> itemCounts = new HashMap<>();

        for (GameObject item : getCurrentPlayer().getInventory().getItems()) {
            String itemName = item.getClass().getSimpleName();
            itemCounts.put(itemName, itemCounts.getOrDefault(itemName, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            String itemName = entry.getKey();
            int count = entry.getValue();

            ImageView itemImageView = new ImageView(getCurrentPlayer().getInventory().getItemImage(itemName));
            itemImageView.setFitWidth(32);
            itemImageView.setFitHeight(32);

            Label itemLabel = new Label("x" + count);
            HBox itemBox = new HBox(5, itemImageView, itemLabel);

            inventoryDisplay.getChildren().add(itemBox);
        }
    }

    private TileMap getCurrentTileMap() {
        return worlds.get(currentWorldIndex).getTileMap();
    }

    private Player getCurrentPlayer() {
        return worlds.get(currentWorldIndex).getPlayer();
    }
}
