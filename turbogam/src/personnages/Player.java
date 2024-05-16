package personnages;

import javafx.scene.image.Image;
import application.Main;

public class Player {
    private int x; // Position x en nombre de tuiles
    private int y; // Position y en nombre de tuiles
    private final int tileSize;
    private final Image playerImage;

    public Player(int startX, int startY, int tileSize, String imagePath) {
        this.x = startX;
        this.y = startY;
        this.tileSize = tileSize;
        this.playerImage = new Image(imagePath);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getPlayerImage() {
        return playerImage;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // Déplacer le joueur (exemple, pour l'animation)
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;

        // Assurer que le joueur reste dans les limites de la carte
        if (this.x < 0) {
            this.x = 0;
        } else if (this.x >= Main.getMapWidth()) {
            this.x = Main.getMapWidth() - 1;
        }

        if (this.y < 0) {
            this.y = 0;
        } else if (this.y >= Main.getMapHeight()) {
            this.y = Main.getMapHeight() - 1;
        }
    }

    // Obtenir la position du joueur en pixels
    public double getPixelX() {
        return x * tileSize;
    }

    public double getPixelY() {
        return y * tileSize;
    }
}
