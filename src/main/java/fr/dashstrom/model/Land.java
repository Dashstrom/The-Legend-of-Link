package fr.dashstrom.model;

import fr.dashstrom.controller.Log;
import fr.dashstrom.model.assignable.Heart;
import fr.dashstrom.model.assignable.consumable.Booster;
import fr.dashstrom.model.assignable.consumable.Potion;
import fr.dashstrom.model.assignable.weapon.FireStaff;
import fr.dashstrom.model.assignable.weapon.IceStaff;
import fr.dashstrom.model.assignable.weapon.MasterAxe;
import fr.dashstrom.model.box.Box;
import fr.dashstrom.model.box.WalkBox;
import fr.dashstrom.model.entity.Pickable;
import fr.dashstrom.model.entity.dommageable.destructible.Destructible;
import fr.dashstrom.model.entity.dommageable.destructible.Fire;
import fr.dashstrom.model.entity.dommageable.destructible.Hay;
import fr.dashstrom.model.entity.dommageable.destructible.Ice;
import fr.dashstrom.model.entity.dommageable.fighter.Fighter;
import fr.dashstrom.model.entity.dommageable.fighter.Player;
import fr.dashstrom.model.entity.dommageable.fighter.enemy.BlueDragon;
import fr.dashstrom.model.entity.dommageable.fighter.enemy.Bug;
import fr.dashstrom.model.entity.dommageable.fighter.enemy.Hydra;
import fr.dashstrom.model.entity.dommageable.fighter.enemy.RedDragon;
import fr.dashstrom.model.entity.dommageable.projectile.Projectile;
import fr.dashstrom.model.entity.interactable.Crate;
import fr.dashstrom.model.entity.interactable.Interactable;
import fr.dashstrom.model.entity.interactable.Npc;
import fr.dashstrom.utils.ResourceUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Land {

    private final Place place;
    private final Tile[][] tiles; // tiles[y][x]
    private final Game game;

    private final ObservableList<Pickable> pickables;
    private final ObservableList<Interactable> interactables;
    private final ObservableList<Destructible> destructibles;
    private final ObservableList<Projectile> projectiles;
    private final ObservableList<Fighter> enemies;

    /**
     * Create a Land with positions, find dir on format X_Y in /main/resources/zone/
     *
     * @param game  Land Game, not null
     * @param place Place of Land, not null
     * @throws LoadError if can't load tiles
     */
    public Land(Game game, Place place) {
        this(game, place, loadTiles(place));
        initEntities();
    }

    /**
     * Create a Land with no entities with tiles
     *
     * @param game  Land Game
     * @param place Place of Land
     * @param tiles Tiles of land
     */
    public Land(Game game, Place place, Tile[][] tiles) {
        this.place = place;
        this.tiles = checkTiles(tiles);
        this.game = game;
        this.pickables = FXCollections.observableArrayList();
        this.interactables = FXCollections.observableArrayList();
        this.destructibles = FXCollections.observableArrayList();
        this.projectiles = FXCollections.observableArrayList();
        this.enemies = FXCollections.observableArrayList();
    }

    /**
     * Check if tiles are valid, same width for each line, not null ...
     *
     * @param tiles Tiles to check
     * @return Tiles
     * @throws LoadError if they are not valid
     */
    private static Tile[][] checkTiles(Tile[][] tiles) {
        if (tiles == null)
            throw new LoadError("Tiles is null");
        else if (tiles.length != ModelConstants.TILES_LINES)
            throw new LoadError("Wrong number of lines expected " + ModelConstants.TILES_LINES + " but receives " + tiles.length);
        for (int y = 0; y < tiles.length; y++) {
            Tile[] line = tiles[y];
            if (line == null)
                throw new LoadError("Tiles have null line at " + y);
            else if (line.length != ModelConstants.TILES_ROWS)
                throw new LoadError("Line of tile at y=" + y + " does not have the same size as the others excepted " + ModelConstants.TILES_ROWS);
            for (int x = 0; x < line.length; x++) {
                Tile t = line[x];
                if (t.getX() != x || t.getY() != y)
                    throw new LoadError("Tile at {" + x + ", " + y + "} lie on this position : " + t);
            }
        }
        return tiles;
    }

    /**
     * load tile for dir /main/resources/zone
     *
     * @param place place of the area to be loaded
     * @return Loaded tiles
     * @throws LoadError if something gone wrong with file or format
     */
    private static Tile[][] loadTiles(Place place) {
        if (place == null)
            throw new LoadError("Place can't be null");
        int x = place.getX(), y = place.getY();
        String pathDir = x + "_" + y;
        Log.info("Loading tile : " + pathDir);
        List<String> paths;
        try {
            paths = ResourceUtils.getResourceListDirectory("/zone/" + pathDir); // Files.list(UtilsModel.path("zone/" + pathDir));
        } catch (IOException e) {
            throw new LoadError("Can't list dir for Land name : " + x + " " + y, e);
        }

        Collections.sort(paths);
        Collections.reverse(paths);
        Pattern pattern = Pattern.compile(".+[\\\\/]zone_" + x + "_" + y + "_(?:z=([0-9]+)_.+|hitbox).csv");
        List<List<int[][]>> rawLand = new ArrayList<>();
        int[][] collisionMap = null;
        for (String path : paths) {
            Matcher m = pattern.matcher(path);
            if (m.matches()) {
                int[][] layer = loadLayer(path);
                if (layer.length != ModelConstants.TILES_LINES)
                    throw new LoadError(String.format("Layer '%s' have invalid height for Land at %s : must be %d got %d",
                        path, place, ModelConstants.TILES_LINES, layer.length));
                for (int i = 0; i < ModelConstants.TILES_LINES; i++) {
                    if (layer[i].length != ModelConstants.TILES_ROWS)
                        throw new LoadError(String.format("Layer '%s' have invalid width at line '%d' for Land at %s: must be %d got %d",
                            path, i, place, ModelConstants.TILES_ROWS, layer[i].length));
                }

                if (m.group(1) == null) {
                    collisionMap = layer;
                } else {
                    int z = Integer.parseInt(m.group(1));
                    for (int zBis = rawLand.size() - 1; zBis < z; zBis++)
                        rawLand.add(new ArrayList<>());
                    rawLand.get(z).add(0, layer);
                }
            }
        }

        if (collisionMap == null && rawLand.isEmpty()) // can't deduce loaded
            throw new LoadError("Nothing to Load at " + place);

        if (collisionMap == null) { // resolve empty collisionMap
            collisionMap = new int[ModelConstants.TILES_LINES][ModelConstants.TILES_ROWS];
            for (int[] line : collisionMap)
                Arrays.fill(line, -1);
        }

        Tile[][] tiles = new Tile[ModelConstants.TILES_LINES][ModelConstants.TILES_ROWS];
        for (int yt = 0; yt < ModelConstants.TILES_LINES; yt++) {
            for (int xt = 0; xt < ModelConstants.TILES_ROWS; xt++) {
                tiles[yt][xt] = new Tile(xt, yt);
                tiles[yt][xt].set(collisionMap, rawLand);
            }
        }
        return checkTiles(tiles);
    }

    /**
     * Load layer
     *
     * @param path Path to the cvs files
     * @throws LoadError if can't find or read file
     **/
    private static int[][] loadLayer(String path) {
        // Create a reader for read the file
        Log.info("Loading layer : " + path);
        InputStream is = ResourceUtils.getResourceAsStream(path);
        InputStreamReader isr = new InputStreamReader(is);
        Reader reader = new BufferedReader(isr);

        ArrayList<int[]> tempLines;
        try {
            // Use a buffer for the file
            BufferedReader br = new BufferedReader(reader);
            // Read the first line
            String line = br.readLine();
            // Create a temporary ArrayList because we don't know the length before reading
            tempLines = new ArrayList<>();
            while (line != null) {
                // Create an array by splitting the ","
                String[] numbers = line.split(",");
                // Convert the string containing numbers into array of int
                int[] tilesLine = new int[numbers.length];
                for (int x = 0; x < numbers.length; x++)
                    tilesLine[x] = Integer.parseInt(numbers[x]);

                tempLines.add(tilesLine);
                // Read the next line
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new LoadError("Can't read layer file : '" + path + "'", e);
        }
        // Convert the ArrayList into an array
        int[][] layer = new int[tempLines.size()][];
        tempLines.toArray(layer);
        return layer;
    }

    /**
     * Match Land with is pos for set
     */
    public void initEntities() {
        pickables.clear();
        enemies.clear();
        interactables.clear();
        projectiles.clear();
        destructibles.clear();
        if (place.isAt(0, 0)) { // spawn
            interactables.addAll(
                new Crate(this, 720, 56),
                new Crate(this, 752, 56),
                new Npc(this, 820, 120, Dialogs.CRATE.get(), "villager")
            );
            if (ResourceUtils.DEBUG) {
                enemies.addAll(
                    new Bug(this, 752, 332),
                    new Bug(this, 656, 332, new int[][]{{752, 272}, {840, 332}}),
                    new Bug(this, 543, 272, new int[][]{{951, 260}}),
                    new Bug(this, 959, 176, new int[][]{{499, 112}})
                );
            }
        } else if (place.isAt(0, -1)) { // central zone
            interactables.addAll(
                new Npc(this, 380, 810, Dialogs.TEST.get()),
                new Npc(this, 495, 800, Dialogs.CLUE.get(), new int[][]{{675, 850}, {771, 729}, {732, 500}}),
                new Npc(this, 900, 270, Dialogs.ICE_FIRE.get(), "villager")

            );
            destructibles.addAll(
                new Ice(this, 784, 216),
                new Ice(this, 816, 216),
                new Fire(this, 784, 64),
                new Fire(this, 816, 64)
            );

            if (ResourceUtils.DEBUG) {
                enemies.addAll(
                    new Bug(this, 145, 206, new int[][]{{157, 674}}),
                    new Bug(this, 732, 142, new int[][]{{956, 166}}),
                    new Bug(this, 1408, 118, new int[][]{{189, 176}}),
                    new Bug(this, 1165, 432, new int[][]{{1557, 684}}),
                    new Bug(this, 1109, 600, new int[][]{{433, 412}})
                );
            }

        } else if (place.isAt(1, -1)) {// ice_fire biome
            enemies.addAll(
                new Bug(this, 789, 800, new int[][]{{615, 417}}),
                new Bug(this, 560, 173, new int[][]{{1392, 75}}),
                new RedDragon(this, 1305, 582, 40, 2)
            );

            pickables.addAll(
                new Pickable(this, 440, 727, new Potion(getPlayer(), 7))
            );
        } else if (place.isAt(2, -1)) { // staff ice_fire zone
            pickables.addAll(
                new Pickable(this, 496, 448, new FireStaff(getPlayer())),
                new Pickable(this, 528, 260, new Heart(getPlayer()))
            );
        } else if (place.isAt(-1, -1)) { // ice biome
            pickables.addAll(
                new Pickable(this, 1329, 661, new Potion(getPlayer(), 10))
            );

            enemies.addAll(
                new Bug(this, 1295, 767, new int[][]{{846, 426}}),
                new Bug(this, 903, 779, new int[][]{{471, 815}, {160, 760}}),
                new BlueDragon(this, 100, 400, 40, 3)
            );
        } else if (place.isAt(-2, -1)) { // ice zone staff
            pickables.addAll(
                new Pickable(this, 848, 256, new IceStaff(getPlayer())),
                new Pickable(this, 880, 520, new Heart(getPlayer()))
            );
        } else if (place.isAt(0, -2)) { // zone before boss
            pickables.addAll(
                new Pickable(this, 144, 768, new MasterAxe(getPlayer())),
                new Pickable(this, 1392, 716, new Booster(getPlayer())),
                new Pickable(this, 1456, 746, new Potion(getPlayer(), 14))
            );

            interactables.addAll(
                new Npc(this, 820, 520, Dialogs.BEFORE_BOSS.get(), "villager")
            );

            destructibles.addAll(
                new Hay(this, 744, 264),
                new Hay(this, 772, 280),
                new Hay(this, 800, 272),
                new Hay(this, 836, 276),
                new Hay(this, 856, 284),
                new Hay(this, 929, 232),
                new Hay(this, 941, 220),
                new Hay(this, 961, 212),
                new Hay(this, 977, 204),
                new Hay(this, 989, 188),
                new Hay(this, 679, 232),
                new Hay(this, 631, 192),
                new Hay(this, 647, 222)
            );
        } else if (place.isAt(0, -3)) { // boss zone
            enemies.addAll(
                new Hydra(this, 800, 450, 120)
            );
        }
    }

    /**
     * Get Player from Game
     *
     * @return PLayer in Game
     */
    public Player getPlayer() {
        return game.getPlayer();
    }

    /**
     * Get game ticks
     *
     * @return ticks > 0
     */
    public long getTicks() {
        return game.getTicks();
    }

    /**
     * Set the dialogue of the game
     *
     * @param sentence text to show
     */
    public void setSays(String sentence) {
        game.setSays(sentence);
    }

    public ObservableList<Interactable> getInteractables() {
        return interactables;
    }

    public ObservableList<Fighter> getEnemies() {
        return enemies;
    }

    public ObservableList<Projectile> getProjectiles() {
        return projectiles;
    }

    public ObservableList<Pickable> getPickables() {
        return pickables;
    }

    public ObservableList<Destructible> getDestructibles() {
        return destructibles;
    }

    public int getX() {
        return place.getX();
    }

    public int getY() {
        return place.getY();
    }

    /**
     * Test if a walkbox and on a collision zone
     *
     * @param box box to test
     * @return true if collide else false
     */
    public boolean isBarrier(WalkBox box) {
        if (contains(box)) {
            for (int x = box.x1() / ModelConstants.TILE_SIZE; x <= (box.x2() - 1) / ModelConstants.TILE_SIZE; x++) {
                for (int y = box.y1() / 32; y <= (box.y2() - 1) / ModelConstants.TILE_SIZE; y++) {
                    boolean[][] pixelsCollide = tiles[y][x].getCollideMap();
                    if (pixelsCollide != null) {
                        int detY = pixelsCollide.length, stepY = (ModelConstants.TILE_SIZE / detY);
                        int endY = Math.min((box.y2() - 1) / stepY - y * detY + 1, detY);
                        for (int y2 = Math.max(box.y1() / stepY - y * detY, 0); y2 < endY; y2++) {
                            int detX = pixelsCollide[0].length, stepX = (ModelConstants.TILE_SIZE / detX);
                            int endX = Math.min((box.x2() - 1) / stepX - x * detX + 1, detX);
                            for (int x2 = Math.max(box.x1() / stepX - x * detX, 0); x2 < endX; x2++)
                                if (pixelsCollide[y2][x2])
                                    return true;
                        }
                    }
                }
            }
            return false;
        } else {
            return true;
        }
    }

    public boolean contains(Box box) {
        return box.in(0, 0, tiles[0].length * ModelConstants.TILE_SIZE, tiles.length * ModelConstants.TILE_SIZE);
    }

    /**
     * Get Tile at x and y.
     *
     * @param x x pos
     * @param y y pos
     * @return Tile at pos x and y
     * @throws ArrayIndexOutOfBoundsException if x and y out of width and height
     */
    public Tile get(int x, int y) {
        return tiles[y][x];
    }

    /**
     * Get width of land.
     *
     * @return width
     */
    public int width() {
        return tiles[0].length * ModelConstants.TILE_SIZE;
    }

    /**
     * Get height of land.
     *
     * @return height
     */
    public int height() {
        return tiles.length * ModelConstants.TILE_SIZE;
    }

    /**
     * Get tiles.
     *
     * @return 2d array of Tile
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    @Override
    public String toString() {
        return String.format("%s(%s, pickables: %d, interactables: %d, enemies: %d, projectiles: %d, destructibles: %d)",
            getClass().getSimpleName(), place, pickables.size(), interactables.size(), enemies.size(), projectiles.size(), destructibles.size());
    }

}
