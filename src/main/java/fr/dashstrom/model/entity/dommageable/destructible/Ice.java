package fr.dashstrom.model.entity.dommageable.destructible;

import fr.dashstrom.model.Element;
import fr.dashstrom.model.Land;

public class Ice extends Destructible {

    public Ice(Land land, int x, int y) {
        super(land, x, y, 50, Element.ICE);
    }

}
