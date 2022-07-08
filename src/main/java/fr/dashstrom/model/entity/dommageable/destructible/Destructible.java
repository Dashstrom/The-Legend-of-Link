package fr.dashstrom.model.entity.dommageable.destructible;

import fr.dashstrom.model.Element;
import fr.dashstrom.model.Land;
import fr.dashstrom.model.entity.dommageable.Dommageable;

public abstract class Destructible extends Dommageable {

    public Destructible(Land land, int x, int y, int pv, Element weakness) {
        super(land, x, y, pv, -16, -8, 32, 16, weakness);
        setWalkbox(32, 16);
        setMove(null);
    }

}