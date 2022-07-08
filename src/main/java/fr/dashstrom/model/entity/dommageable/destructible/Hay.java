package fr.dashstrom.model.entity.dommageable.destructible;

import fr.dashstrom.model.Element;
import fr.dashstrom.model.Land;

public class Hay extends Destructible {

    public Hay(Land land, int x, int y) {
        super(land, x, y, 20, Element.ICE);
    }

}