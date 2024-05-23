package personnages;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import objet.GameObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import application.Main;

public class PNJ {
    private double x;
    private double y;
    private javafx.scene.image.Image image;
    private List<GameObject> inventory;
    private int tileSize = Main.TILE_SIZE;

    public PNJ(double x, double y, String imagePath) {
        this.x = x;
        this.y = y;
        this.image = new javafx.scene.image.Image(imagePath);
        this.inventory = new ArrayList<>();
    }

    public double getX() {
        return x;
    }
    
    public Rectangle2D getBounds() {
        return new Rectangle2D(x * tileSize, y * tileSize, tileSize, tileSize);
    }

    public double getY() {
        return y;
    }

    public javafx.scene.image.Image getImage() {
        return image;
    }

    public List<GameObject> getInventory() {
        return inventory;
    }

    public void addItem(GameObject item) {
        inventory.add(item);
    }

    public void interactWithPlayer(Player player) {
        List<GameObject> inventoryCopy = new ArrayList<>(inventory);
        Iterator<GameObject> iterator = inventoryCopy.iterator();
        showNextDialogue(player, iterator);
    }

    private void showNextDialogue(Player player, Iterator<GameObject> iterator) {
        if (!iterator.hasNext()) {
            return;
        }

        GameObject item = iterator.next();
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("PNJ");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous acheter " + item.getClass().getSimpleName() + " pour " + item.getPrice() + " pièces ?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                if (player.spendMoney(item.getPrice())) {
                    player.getInventory().addItem(item);
                    inventory.remove(item);
                } else {
                    Alert noMoneyAlert = new Alert(AlertType.INFORMATION);
                    noMoneyAlert.setTitle("PNJ");
                    noMoneyAlert.setHeaderText(null);
                    noMoneyAlert.setContentText("Vous n'avez pas assez d'argent pour acheter cet objet.");
                    noMoneyAlert.showAndWait();
                }
            }
            showNextDialogue(player, iterator);
        });
    }
}
