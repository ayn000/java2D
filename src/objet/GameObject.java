package objet;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;
import personnages.Player;

public abstract class GameObject {
    protected int x;
    protected int y;
    protected int tileSize;
    protected Image image;

    public GameObject(int x, int y, int tileSize, String imagePath) {
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
        this.image = new Image(imagePath);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x * tileSize, y * tileSize, tileSize, tileSize);
    }

    public void draw(GraphicsContext gc) {
        if (image != null) {
            gc.drawImage(image, x * tileSize, y * tileSize, tileSize, tileSize);
        } else {
            System.out.println("Image is null for GameObject at (" + x + ", " + y + ")");
        }
    }

    public abstract void onPlayerContact(Player player);

    public abstract void applyEffect(Player player);
}
