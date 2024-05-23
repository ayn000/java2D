package personnages;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

public class Enemy {
    private int x; // Position x en nombre de tuiles
    private int y; // Position y en nombre de tuiles
    private int tileSize;
    private Image enemyImage;
    private int health;
    private int attack;
    public double range;

    public Enemy(int x, int y, int tileSize, String imagePath, int health, int attack) {
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
        this.enemyImage = new Image(imagePath);
        this.health = health;
        this.attack = attack;
        this.range = 0.5;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }
    
    public Rectangle2D getAttackBounds() {
        return new Rectangle2D(x*tileSize-(tileSize/2-3), y*tileSize-(tileSize/2-3), tileSize+3, tileSize+3);
    }
    public void decreaseHealth(int amount) {
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
        System.out.println("Enemy health decreased by " + amount + ", new health: " + this.health);
    }

    public Image getEnemyImage() {
        return enemyImage;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x * tileSize, y * tileSize, tileSize, tileSize);
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(enemyImage, x * tileSize, y * tileSize, tileSize, tileSize);
    }
}
