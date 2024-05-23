package personnages;

import javafx.scene.image.Image;
import objet.Inventory;
import javafx.geometry.Rectangle2D;
import map.TileMap;

public class Player {
    private double x; // Position x en nombre de tuiles
    private double y; // Position y en nombre de tuiles
    private final int tileSize;
    private Image playerImage;
    private final double speed;
    private int health;// Nouvelle variable de vitesse 
    private Image skullImage;
    private int attack;
    private Inventory inventory;

    public String direction;
    private int money;
    

    public Player(int startX, int startY, int tileSize, String imagePath, double speed) {
        this(startX, startY, tileSize, imagePath, speed, 100); // Santé par défaut à 100
    }

    // Constructeur avec santé spécifiée
    public Player(int startX, int startY, int tileSize, String imagePath, double speed, int health) {
        this.x = startX;
        this.y = startY;
        this.tileSize = tileSize;
        this.playerImage = new Image(imagePath);
        this.speed = speed;
        this.health = health;
        this.skullImage = new Image("file:tile/pixelart_skull.png");
        this.setAttack(10);
        this.inventory = new Inventory();
        this.direction = "right";
        this.money = 100;
    }
    
    public int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public boolean spendMoney(int amount) {
        if (money >= amount) {
            money -= amount;
            return true;
        }
        return false;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Image getPlayerImage() {
        return isDead() ? skullImage : playerImage;
    }

    public void setX(double x) {
        this.x = x;
    }
    
    public void decreaseHealth(int amount) {
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
    }
    
    public int getHealth() {
        return health;
    }
    
    public boolean isDead() {
    	return this.health <= 0;
    }
    
   
    
    public void setHealth(int health) {
        this.health = health;
    }

    public void setY(double y) {
        this.y = y;
    }
    public double getSpeed() {
        return speed;
    }

    // Déplacer le joueur (exemple, pour l'animation)
    public void move(double dx, double dy) {
    	if (this.health>0) {
    		this.x += dx;
    	    this.y += dy;
    		
    	}
        

        // Assurer que le joueur reste dans les limites de la carte
        if (this.x < 0 ) {
            this.x = 0;
        } else if (this.x >= TileMap.getMapWidth()-1) {
            this.x = TileMap.getMapWidth()-1;
        }

        if (this.y < 0) {
            this.y = 0;
        } else if (this.y >= TileMap.getMapHeight()-1) {
            this.y = TileMap.getMapHeight()-1;
        }
        
        
    }

    // Obtenir la position du joueur en pixels
    public double getPixelX() {
        return x * tileSize;
    }
    
    public void setDirection (String direction) {
    	this.direction = direction;
    }

    public double getPixelY() {
        return y * tileSize;
    }
    public Rectangle2D getAttackBounds() {
        return new Rectangle2D(x*tileSize-(tileSize/2-3), y*tileSize-(tileSize/2-3), tileSize+3, tileSize+3);
    }
    public Rectangle2D getBounds() {
        return new Rectangle2D(x*tileSize, y*tileSize, tileSize, tileSize);
    }

	public int getAttack() {
		return attack;
	}
	public void attackEnemy(Enemy enemy) {
        if (this.getAttackBounds().intersects(enemy.getAttackBounds())) {
            enemy.decreaseHealth(attack);
        }
        else {
        	System.out.println("No collision detected, enemy not attacked");
        }
    }

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public Inventory getInventory() {
		return inventory;
	}
}
