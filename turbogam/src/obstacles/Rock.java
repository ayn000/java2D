package obstacles;

import javafx.scene.canvas.GraphicsContext;

public class Rock extends Obstacle {
    public Rock(int x, int y, String imagePath) {
        super(x, y, 32, 32, false, imagePath); // Exemple de taille fixe et non traversable
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(image, x * width, y * height, width, height);
    }
}
