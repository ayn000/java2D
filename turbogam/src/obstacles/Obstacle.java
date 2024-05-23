package obstacles;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

public abstract class Obstacle {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean traversable;
    protected Image image;

    public Obstacle(int x, int y, int width, int height, boolean traversable, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.traversable = traversable;
        this.image = new Image(imagePath);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isTraversable() {
        return traversable;
    }

    public Image getImage() {
        return image;
    }

    public abstract void draw(GraphicsContext gc);
    
    public Rectangle2D getBounds() {
        return new Rectangle2D(x * width, y * height, width, height);
    }
}
