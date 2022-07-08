package fr.dashstrom.model.entity.dommageable.projectile;

import fr.dashstrom.model.Element;
import fr.dashstrom.model.Land;
import fr.dashstrom.model.entity.dommageable.Dommageable;

public class Slash extends Projectile {

    public Slash(Land land, int x, int y, int ax, int ay, int atk) {
        super(land, x, y, ax, ay, 15, 150, atk, true, false, Element.NORMAL);
    }

    @Override
    public boolean hit(Dommageable entity) {
        return true;
    }

}
