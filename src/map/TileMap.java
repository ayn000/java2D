package map;

import javafx.scene.canvas.GraphicsContext;
import objet.*;
import personnages.Enemy;
import obstacles.*;
import javafx.scene.image.Image;
import personnages.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class TileMap {

    private final int width;
    private final int height;
    private static final int MAP_WIDTH = 25;
    private static final int MAP_HEIGHT = 18;
    private final int tileSize;
    private final int[][] map;
    private final Obstacle[][] obstacles;
    private final List<Enemy> enemies;
    private final List<GameObject> gameObjects;
    
    private Image tileImage;
    private Player player;

    public TileMap(int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.map = new int[width][height];
        this.obstacles = new Obstacle[width][height];
        this.enemies = new ArrayList<>();
        this.gameObjects = new ArrayList<>();

        // Initialiser la carte avec des valeurs par défaut (0)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = 0;
            }
        }
    }

    // Charger l'image de la tuile
    public void loadTileImages(String imagePath) {
        try {
            this.tileImage = new Image(imagePath);
            if (this.tileImage == null) {
                throw new IllegalArgumentException("Image not found at " + imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load tile image", e);
        }
    }
    
    
    public void initObstacles(int[][] obstacleMatrix, String obstacleImagePath) {
        for (int x = 0; x < obstacleMatrix.length; x++) {
            for (int y = 0; y < obstacleMatrix[x].length; y++) {
                if (obstacleMatrix[x][y] == 1) {
                    Rock rock = new Rock(x, y, obstacleImagePath);
                    setObstacle(rock);
                }
            }
        }
    }

    
    public void setObstacle(Obstacle obstacle) {
        if (obstacle.getX() >= 0 && obstacle.getX() < map.length &&
            obstacle.getY() >= 0 && obstacle.getY() < map[0].length) {
            obstacles[obstacle.getX()][obstacle.getY()] = obstacle;
        }
    }

    // Ajouter un joueur à la carte
    public void addPlayer(Player player) {
        this.player = player;
    }
    
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }
    
    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }
    public void removeDeadEnemies() {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (enemy.getHealth() <= 0) {
                iterator.remove();
            }
        }
    }
    
    public List<Enemy> getEnemies() {
        return enemies;
    }
    public List<GameObject> getGameObjects() {
        return gameObjects;
    }
    
    
    
    public boolean isTraversable(int x, int y) {
        if (x < 0 || x >= map.length || y < 0 || y >= map[0].length) {
            return false;
        }
        return obstacles[x][y] == null || obstacles[x][y].isTraversable();
    }
    public List<Obstacle> getObstacles() {
        List<Obstacle> obstacleList = new ArrayList<>();
        for (int x = 0; x < obstacles.length; x++) {
            for (int y = 0; y < obstacles[x].length; y++) {
                if (obstacles[x][y] != null) {
                    obstacleList.add(obstacles[x][y]);
                }
            }
        }
        return obstacleList;
    }

    // Dessiner la carte
    public void draw(GraphicsContext gc) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map[x][y] == 0) { // Par exemple, si la valeur est 0, dessine la tuile
                    gc.drawImage(tileImage, x * tileSize, y * tileSize, tileSize, tileSize);
                }
                // Tu pourras ajouter des conditions ici pour d'autres types de tuiles
            }
        }
        
        for (Obstacle obstacle : getObstacles()) {
            obstacle.draw(gc);
        }
        for (Enemy enemy : enemies ) {
        	enemy.draw(gc);
        }
        for (GameObject gameObject : gameObjects) {
            gameObject.draw(gc);
        }
        
        

        // Dessiner le joueur
        if (player != null) {
            gc.drawImage(player.getPlayerImage(), player.getPixelX(), player.getPixelY(), tileSize, tileSize);
        }
    }

    // Mettre à jour la carte (par exemple, déplacer des tuiles, ajouter des animations, etc.)
    public void update() {
    	
    	removeDeadEnemies();
    	
        // Logique de mise à jour de la carte
        if (player != null) {
            // Exemple de déplacement du joueur (à remplacer par la logique de contrôle)
            player.move(0, 0); // Pour l'instant, on ne déplace pas le joueur
        }
    }

	public double getTileSize() {
		// TODO Auto-generated method stub
		return tileSize;
	}
	public static int getMapHeight(){
		return MAP_HEIGHT;
	}
	public static int getMapWidth(){
		return MAP_WIDTH;
	}
	
}
