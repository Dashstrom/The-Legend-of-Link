package fr.dashstrom.model.entity;

import fr.dashstrom.model.assignable.Assignable;
import fr.dashstrom.model.entity.dommageable.fighter.Player;
import fr.dashstrom.model.Land;
import fr.dashstrom.model.UtilsModel;

import java.util.Objects;

public class Pickable extends Entity {

    private Assignable assignable;

    public Pickable(Land land, int x, int y, Assignable assignable) {
        super(land, x, y);
        this.assignable = Objects.requireNonNull(assignable);
    }

    public Assignable getAssignable() {
        return assignable;
    }

    public boolean isTaken() {
        return assignable == null;
    }

    @Override
    public void act() {
        Player p = getLand().getPlayer();
        if (UtilsModel.distance(getX(), getY(), p.getX(), p.getY()) < 48 && assignable != null) {
            assignable.assign();
            assignable = null;
        }
    }

}
