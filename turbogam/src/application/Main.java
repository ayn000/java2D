package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import map.TileMap;
import personnages.Enemy;
import personnages.Player;

import java.util.HashSet;
import java.util.Set;

public class Main extends Application {

    private static final int TILE_SIZE = 32;
    private static final int MAP_WIDTH = 25;
    private static final int MAP_HEIGHT = 18;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private GameLoop gameLoop;
    private Canvas canvas;
    private TileMap tileMap;
    private Player player;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Créer la racine de la scène
        StackPane root = new StackPane();
        canvas = new Canvas(MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE);
        root.getChildren().add(canvas);

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
        // Créer et initialiser la carte des tuiles
        tileMap = new TileMap(MAP_WIDTH, MAP_HEIGHT, TILE_SIZE);
        tileMap.loadTileImages("file:tile/floor1.png");

        // Matrice représentant les obstacles (1 = obstacle, 0 = pas d'obstacle)
        int[][] obstacleMatrix = {
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

        // Ajouter des obstacles à partir de la matrice
        tileMap.initObstacles(obstacleMatrix, "file:tile/pierre.png");

        // Ajouter des ennemis à la carte
        Enemy enemy1 = new Enemy(5, 10, TILE_SIZE, "file:tile/orc.png", 100, 10);
        Enemy enemy2 = new Enemy(15, 15, TILE_SIZE, "file:tile/orc.png", 100, 10);
        tileMap.addEnemy(enemy1);
        tileMap.addEnemy(enemy2);

        // Créer et ajouter le joueur avec une vitesse de déplacement (la santé est initialisée par défaut)
        double playerSpeed = 0.05; // Ajuste cette valeur pour changer la vitesse (pixels par frame)
        player = new Player(5, 5, TILE_SIZE, "file:tile/personnage.png", playerSpeed);
        tileMap.addPlayer(player);

        // Démarrer la boucle de jeu
        if (gameLoop != null) {
            gameLoop.stop();
        }
        gameLoop = new GameLoop(canvas, tileMap, player, pressedKeys, this::initGame);
        gameLoop.start();
    }
}
