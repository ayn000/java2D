package objet;

import personnages.Player;

public class Sword extends GameObject {
    private int attackBoost;

    public Sword(int x, int y, int tileSize, String imagePath, int attackBoost) {
        super(x, y, tileSize, imagePath,30);
        this.attackBoost = attackBoost;
    }

    @Override
    public void onPlayerContact(Player player) {
        player.getInventory().addItem(this);
        // Logique pour supprimer l'objet de la carte peut être ajoutée ici
    }

    @Override
    public void applyEffect(Player player) {
        player.setAttack(player.getAttack() + attackBoost);
    }

	@Override
	public int getPrice() {
		// TODO Auto-generated method stub
		return this.price;
	}
}

