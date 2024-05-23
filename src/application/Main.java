package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import map.World;
import personnages.Player;
import personnages.Enemy;
import objet.GameObject;
import objet.Sword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main extends Application {

    private static final int TILE_SIZE = 32;
    private static final int MAP_WIDTH = 25;
    private static final int MAP_HEIGHT = 18;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private GameLoop gameLoop;
    private Canvas canvas;
    private List<World> worlds;
    private Player player;
    private Label healthLabel;
    private HBox inventoryDisplay;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Créer la racine de la scène
        BorderPane root = new BorderPane();
        canvas = new Canvas(MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE);
        root.setCenter(canvas);

        // Ajouter les éléments d'interface utilisateur pour la vie et l'inventaire
        healthLabel = new Label("Health: 100");
        root.setTop(healthLabel);
        BorderPane.setAlignment(healthLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(healthLabel, new Insets(10));

        inventoryDisplay = new HBox(10);
        root.setTop(inventoryDisplay);
        BorderPane.setAlignment(inventoryDisplay, Pos.TOP_RIGHT);
        BorderPane.setMargin(inventoryDisplay, new Insets(10));

        // Créer la scène
        Scene scene = new Scene(root, MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE);
        primaryStage.setTitle("Tile-based Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Gestion des événements de clavier
        scene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));

        // Initialiser et démarrer le jeu
        initGame();
    }

    private void initGame() {
        double playerSpeed = 0.05; // Ajuste cette valeur pour changer la vitesse (pixels par frame)
        player = new Player(5, 5, TILE_SIZE, "file:tile/personnage.png", playerSpeed);

        // Créer les mondes
        worlds = new ArrayList<>();

        // Monde 1
        int[][] obstacleMatrix1 = {
            // {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        World world1 = new World(MAP_WIDTH, MAP_HEIGHT, TILE_SIZE, player, "file:tile/floor1.png", obstacleMatrix1);
        world1.getTileMap().addEnemy(new Enemy(5, 10, TILE_SIZE, "file:tile/orc.png", 100, 10));
        world1.getTileMap().addGameObject(new Sword(10, 10, TILE_SIZE, "file:tile/sword.png", 10));
        worlds.add(world1);

        // Monde 2
        int[][] obstacleMatrix2 = {
        		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        World world2 = new World(MAP_WIDTH, MAP_HEIGHT, TILE_SIZE, player, "file:tile/floor2.png", obstacleMatrix2);
        world2.getTileMap().addEnemy(new Enemy(15, 15, TILE_SIZE, "file:tile/orc.png", 100, 10));
        world2.getTileMap().addGameObject(new Sword(15, 15, TILE_SIZE, "file:tile/sword.png", 10));
        worlds.add(world2);

        if (gameLoop != null) {
            gameLoop.stop();
        }
        gameLoop = new GameLoop(canvas, worlds, pressedKeys, this::initGame, healthLabel, inventoryDisplay);
        gameLoop.start();

        // Initialiser l'affichage de la vie et de l'inventaire
        updateHealthDisplay();
        updateInventoryDisplay();
    }

    private void updateHealthDisplay() {
        healthLabel.setText("Health: " + player.getHealth());
    }

    private void updateInventoryDisplay() {
        inventoryDisplay.getChildren().clear();
        Map<String, Integer> itemCounts = new HashMap<>();

        for (GameObject item : player.getInventory().getItems()) {
            String itemName = item.getClass().getSimpleName();
            itemCounts.put(itemName, itemCounts.getOrDefault(itemName, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            String itemName = entry.getKey();
            int count = entry.getValue();

            ImageView itemImageView = new ImageView(player.getInventory().getItemImage(itemName));
            itemImageView.setFitWidth(32);
            itemImageView.setFitHeight(32);

            Label itemLabel = new Label("x" + count);
            HBox itemBox = new HBox(5, itemImageView, itemLabel);

            inventoryDisplay.getChildren().add(itemBox);
        }
    }
}
