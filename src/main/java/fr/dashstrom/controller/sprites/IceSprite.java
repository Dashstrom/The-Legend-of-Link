package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.Images;
import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.entity.dommageable.destructible.Ice;

public class IceSprite extends SpriteDommageable<Ice> {

    /**
     * Add an animation from frames to the sprite
     *
     * @param pane   Pane of sprite
     * @param entity Entity to anchor
     **/
    public IceSprite(ZPane pane, Ice entity) {
        super(pane, entity, -16, -40, Images.ICE.get());
    }

}
