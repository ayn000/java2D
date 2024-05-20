package personnages;

import application.Main;
import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

public class Player {
    private double x; // Position x en nombre de tuiles
    private double y; // Position y en nombre de tuiles
    private final int tileSize;
    private final Image playerImage;
    private final double speed; // Nouvelle variable de vitesse 

    public Player(int startX, int startY, int tileSize, String imagePath, double speed) {
        this.x = startX;
        this.y = startY;
        this.tileSize = tileSize;
        this.playerImage = new Image(imagePath);
        this.speed = speed;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Image getPlayerImage() {
        return playerImage;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
    public double getSpeed() {
        return speed;
    }

    // Déplacer le joueur (exemple, pour l'animation)
    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        System.out.println(this.x);
        System.out.println(Main.getMapHeight() * tileSize);

        // Assurer que le joueur reste dans les limites de la carte
        if (this.x < 0) {
            this.x = 0;
        } else if (this.x >= Main.getMapWidth()-1) {
            this.x = Main.getMapWidth()-1;
            System.out.println("sortie de map");
        }

        if (this.y < 0) {
            this.y = 0;
        } else if (this.y >= Main.getMapHeight()-1) {
            this.y = Main.getMapHeight()-1;
        }
    }

    // Obtenir la position du joueur en pixels
    public double getPixelX() {
        return x * tileSize;
    }

    public double getPixelY() {
        return y * tileSize;
    }
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, tileSize, tileSize);
    }
}
