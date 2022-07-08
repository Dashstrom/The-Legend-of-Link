package fr.dashstrom.model.box;

import fr.dashstrom.model.entity.Entity;
import fr.dashstrom.model.entity.dommageable.destructible.Destructible;
import fr.dashstrom.model.entity.dommageable.fighter.Fighter;
import fr.dashstrom.model.entity.interactable.Interactable;
import fr.dashstrom.model.Land;

public class WalkBox extends Box {

    public WalkBox(Entity anchor, int dx, int dy, int width, int height) {
        this(anchor, 0, 0, dx, dy, width, height);
    }

    public WalkBox(Entity anchor, int shiftX, int shiftY, int dx, int dy, int width, int height) {
        super(anchor, shiftX, shiftY, dx, dy, width, height);
    }

    public boolean isObstructed() {
        Land land = getAnchor().getLand();
        for (Interactable interactable : land.getInteractables()) {
            if (hasNotAnchor(interactable) && intersect(interactable.getWalkbox())) {
                return true;
            }
        }
        for (Destructible destructible : land.getDestructibles()) {
            if (hasNotAnchor(destructible) && intersect(destructible.getWalkbox())) {
                return true;
            }
        }
        return false;
    }

    public WalkBox rel(int dx, int dy) {
        return new WalkBox(getAnchor(), getShiftX() + dx, getShiftY() + dy, getDx(), getDy(), getWidth(), getHeight());
    }

    /**
     * Obtaining an absolute place box
     *
     * @param x x position
     * @param y y position
     * @return A box placed in x and y always anchored to its entity
     */
    public WalkBox abs(int x, int y) {
        return new WalkBox(
            getAnchor(),
            x - getAnchor().getX(),
            y - getAnchor().getY(),
            getDx(),
            getDy(),
            getWidth(),
            getHeight()
        );
    }

    public boolean walkable() {
        Land land = getAnchor().getLand();
        if (!isBarrier() && !isObstructed()) {
            if (hasNotAnchor(land.getPlayer()) && intersect(land.getPlayer().getWalkbox()))
                return false;
            for (Fighter enemy : land.getEnemies())
                if (hasNotAnchor(enemy) && intersect(enemy.getWalkbox()))
                    return false;
            return true;
        }
        return false;
    }

    public boolean isBarrier() {
        return getAnchor().getLand().isBarrier(this);
    }

}
