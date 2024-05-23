package objet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.image.Image;
import personnages.Player;

public class Inventory {
    private List<GameObject> items;
    private Map<String, Image> itemImages;

    public Inventory() {
        this.items = new ArrayList<>();
        this.itemImages = new HashMap<>();
    }

    public void addItem(GameObject item) {
        items.add(item);
        String itemName = item.getClass().getSimpleName();
        if (!itemImages.containsKey(itemName)) {
            itemImages.put(itemName, item.getImage());
        }
    }

    public void applyItemEffects(Player player) {
        for (GameObject item : items) {
            item.applyEffect(player);
        }
    }

    public List<GameObject> getItems() {
        return items;
    }

    public Image getItemImage(String itemName) {
        return itemImages.get(itemName);
    }
}
