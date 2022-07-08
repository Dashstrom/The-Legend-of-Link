package fr.dashstrom.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Tile {

    private static final boolean[][][] pixelMap = computePixelMap();

    private final int x, y;
    private final ObservableList<ObservableList<Integer>> layers;
    private boolean[][] collideMap;

    /**
     * Represent tile at x and y.
     *
     * @param x x pos
     * @param y y pos
     */
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        collideMap = null;
        layers = FXCollections.observableArrayList();
    }

    /**
     * Builds the under tiles for more precise collisions.
     *
     * @return masks of boolean for collisions boolean[idTile][y][x]
     */
    private static boolean[][][] computePixelMap() {
        boolean[][][] mask = new boolean[17][32][32];
        mask[4] = new boolean[][]{{false}, {true}};
        mask[5] = new boolean[][]{{false, true}};
        mask[6] = new boolean[][]{{true}, {false}};
        mask[7] = new boolean[][]{{true, false}};
        mask[16] = new boolean[][]{{true}};

        for (int y = 0; y < ModelConstants.TILE_SIZE; y++) {
            for (int x = 0; x < ModelConstants.TILE_SIZE; x++) {
                boolean b1 = 30 - x < y;
                mask[8][y][x] = b1;
                mask[9][31 - y][x] = b1;
                mask[10][31 - y][31 - x] = b1;
                mask[11][y][31 - x] = b1;
                boolean b2 = 46 - x < y;
                mask[0][y][x] = b2;
                mask[1][31 - y][x] = b2;
                mask[2][31 - y][31 - x] = b2;
                mask[3][y][31 - x] = b2;
                boolean b3 = 15 - x < y;
                mask[12][y][x] = b3;
                mask[13][31 - y][x] = b3;
                mask[14][31 - y][31 - x] = b3;
                mask[15][y][31 - x] = b3;
            }
        }
        return mask;
    }

    /**
     * Set new layers using generic array of collisions and layers.
     *
     * @param collisionMap 2d array of all collisions on format [y][x]
     * @param rawLand      layers of all tiles, on format [z][priority][y][x] -> tile, y and x must be the same size as collisionMap
     * @throws NullPointerException if collisionMap or rawLand is null
     */
    public void set(int[][] collisionMap, List<List<int[][]>> rawLand) {
        int code = collisionMap[y][x];
        if (0 <= code && code <= 16)
            collideMap = pixelMap[code];
        else
            collideMap = new boolean[][]{{false}};
        layers.forEach(List::clear);
        layers.clear();
        for (int z = 0; z < rawLand.size(); z++) {
            layers.add(FXCollections.observableArrayList());
            for (int[][] orderLayer : rawLand.get(z))
                layers.get(z).add(orderLayer[y][x]);
        }
    }

    /**
     * Get X
     *
     * @return x position,
     */
    public int getX() {
        return x;
    }

    /**
     * Get Y
     *
     * @return y position
     */
    public int getY() {
        return y;
    }

    /**
     * Get the collide map of this tile.
     *
     * @return 2d array of boolean, with first index y and second x, true if pixel not traversable
     */
    public boolean[][] getCollideMap() {
        return collideMap;
    }

    /**
     * Get every layers.
     *
     * @return Layers on format [z][priority] -> id
     */
    public ObservableList<ObservableList<Integer>> getLayers() {
        return layers;
    }

    @Override
    public String toString() {
        return "Tile{" + x + ", " + y + "}";
    }

}
