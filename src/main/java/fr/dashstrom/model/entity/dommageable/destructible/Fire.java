package fr.dashstrom.model.entity.dommageable.destructible;

import fr.dashstrom.model.Element;
import fr.dashstrom.model.Land;

public class Fire extends Destructible {

    public Fire(Land land, int x, int y) {
        super(land, x, y, 1, Element.FIRE);
    }

}
