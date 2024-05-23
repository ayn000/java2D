package map;

import personnages.Player;

public class World {
    private TileMap tileMap;
    private Player player;

    public World(int width, int height, int tileSize, Player player, String tileImagePath, int[][] obstacleMatrix) {
        this.tileMap = new TileMap(width, height, tileSize);
        this.tileMap.loadTileImages(tileImagePath);
        this.tileMap.initObstacles(obstacleMatrix, "file:tile/pierre.png");
        this.player = player;
        this.tileMap.addPlayer(player);
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public Player getPlayer() {
        return player;
    }
}
