package map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import personnages.Player;

public class TileMap {

    private final int width;
    private final int height;
    private final int tileSize;
    private final int[][] map;
    private Image tileImage;
    private Player player;

    public TileMap(int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.map = new int[width][height];

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

    // Ajouter un joueur à la carte
    public void addPlayer(Player player) {
        this.player = player;
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

        // Dessiner le joueur
        if (player != null) {
            gc.drawImage(player.getPlayerImage(), player.getPixelX(), player.getPixelY(), tileSize, tileSize);
        }
    }

    // Mettre à jour la carte (par exemple, déplacer des tuiles, ajouter des animations, etc.)
    public void update() {
        // Logique de mise à jour de la carte
        if (player != null) {
            // Exemple de déplacement du joueur (à remplacer par la logique de contrôle)
            player.move(0, 0); // Pour l'instant, on ne déplace pas le joueur
        }
    }
}
