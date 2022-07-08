package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.Images;
import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.entity.dommageable.destructible.Hay;

public class HaySprite extends SpriteDommageable<Hay> {

    /**
     * Creates a sprite for a crate
     *
     * @param burnable Crate to anchor
     **/
    public HaySprite(ZPane pane, Hay burnable) {
        super(pane, burnable, -16, -24, Images.HAY.get());
    }

}
