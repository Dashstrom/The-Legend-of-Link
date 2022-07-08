package fr.dashstrom.model;

import fr.dashstrom.model.entity.Pickable;
import fr.dashstrom.model.entity.dommageable.Dommageable;
import fr.dashstrom.model.entity.dommageable.destructible.Destructible;
import fr.dashstrom.model.entity.dommageable.fighter.Fighter;
import fr.dashstrom.model.entity.dommageable.fighter.Player;
import fr.dashstrom.model.entity.dommageable.fighter.PlayerDeadException;
import fr.dashstrom.model.entity.dommageable.projectile.Projectile;
import fr.dashstrom.model.entity.interactable.Interactable;
import fr.dashstrom.utils.ResourceUtils;
import fr.dashstrom.model.box.WalkBox;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing the game
 **/
public class Game {

    private final ObjectProperty<Land> land; // terrain.get(z).get(order)[y][x]
    private final Player zelda;
    private final HashMap<Place, Land> zones;
    private final StringProperty dialog;
    private long ticks;

    //CONSTRUCTOR
    public Game() {
        zones = new HashMap<>();
        land = new SimpleObjectProperty<>();
        dialog = new SimpleStringProperty("");
        ticks = 0;
        zelda = new Player(null, 750, 750);
        loadLands();
        zelda.setLand(land.get());
    }

    private void loadLands() {
        List<String> paths;
        try {
            paths = ResourceUtils.getResourceListDirectory("/zone");
        } catch (IOException e) {
            throw new LoadError("Can't list dir for load all zones", e);
        }

        Collections.sort(paths);
        Collections.reverse(paths);
        Pattern pattern = Pattern.compile(".+[\\\\/](?<x>-?[0-9]+)_(?<y>-?[0-9]+)");
        for (String path : paths) {
            Matcher m = pattern.matcher(path);
            if (m.matches()) {
                int x = Integer.parseInt(m.group("x")), y = Integer.parseInt(m.group("y"));
                Place place = new Place(x, y);
                Land land = new Land(this, place);
                zones.put(place, land);
            }
        }

        land.set(zones.get(new Place(0, 0)));
        for (Land land : zones.values())
            land.initEntities();
    }

    public ObservableList<Interactable> getInteractables() {
        return land.get().getInteractables();
    }

    public ObservableList<Fighter> getEnemies() {
        return land.get().getEnemies();
    }

    public ObservableList<Projectile> getProjectiles() {
        return land.get().getProjectiles();
    }

    public ObservableList<Pickable> getPickables() {
        return land.get().getPickables();
    }

    public ObservableList<Destructible> getDestructible() {
        return land.get().getDestructibles();
    }

    public long getTicks() {
        return ticks;
    }

    public String getSays() {
        return dialog.get();
    }

    //SETTERS
    public void setSays(String phrase) {
        dialog.set(phrase);
    }

    //PROPERTIES
    public StringProperty saysProperty() {
        return dialog;
    }

    //GETTERS
    public Player getPlayer() {
        return zelda;
    }

    public Land getLand() {
        return land.get();
    }

    public void setLand(int x, int y) {
        Land land = zones.get(new Place(x, y));
        this.land.set(land);
        getPlayer().setLand(land);
    }

    public ObjectProperty<Land> landProperty() {
        return land;
    }

    public void playerChangeLand() {
        int x1, x2, y1, y2, dx, dy;

        if (!getLand().getEnemies().isEmpty())
            return;

        WalkBox wb = zelda.getWalkbox();
        if (wb == null) {
            x1 = zelda.getX();
            x2 = x1;
            y1 = zelda.getY();
            y2 = y1;
            dx = 0;
            dy = 0;
        } else {
            x1 = wb.x1();
            x2 = wb.x2();
            y1 = wb.y1();
            y2 = wb.y2();
            dx = wb.getDx();
            dy = wb.getDy();
        }

        Land l = getLand();
        int nx = l.getX(), ny = l.getY();
        if (x1 <= 0)
            nx--;
        else if (x2 >= l.width() - 1)
            nx++;
        else if (y1 <= 0)
            ny--;
        else if (y2 >= l.height() - 1)
            ny++;

        if (nx != l.getX() || ny != l.getY()) {
            Land land = zones.get(new Place(nx, ny));
            if (land != null) {
                int tempX = zelda.getX(), tempY = zelda.getY();
                if (nx < l.getX())
                    zelda.setX(land.width() + dx - 2);
                else if (nx > l.getX())
                    zelda.setX(-dx + 1);
                else if (ny < l.getY())
                    zelda.setY(land.height() + dy - 2);
                else if (ny > l.getY())
                    zelda.setY(-dy + 1);
                zelda.setLand(land);
                if (wb == null || wb.walkable())
                    setLand(nx, ny);
                else {
                    zelda.setLand(getLand());
                    zelda.setX(tempX);
                    zelda.setY(tempY);
                }
            }
        }
    }

    /**
     * Update the game
     **/
    public void aTurn() throws PlayerDeadException {
        playerChangeLand();
        zelda.act();

        getEnemies().removeIf(Dommageable::isDead);
        ObservableList<Fighter> fighters = getEnemies();
        for (int count = 0; count < fighters.size(); count++) {
            Fighter fighter = fighters.get(count);
            fighter.act();
        }

        for (Interactable interactable : getInteractables()) {
            interactable.act();
        }

        ObservableList<Projectile> listProj = getProjectiles();
        for (int count = 0; count < listProj.size(); count++) {
            Projectile p = listProj.get(count);
            listProj.removeIf(Dommageable::isDead);
            p.act();
        }

        getPickables().forEach(Pickable::act);
        getPickables().removeIf(Pickable::isTaken);

        for (Destructible destructible : getDestructible()) {
            destructible.act();
        }
        getDestructible().removeIf(Destructible::isDead);
        ticks++;
    }

}
