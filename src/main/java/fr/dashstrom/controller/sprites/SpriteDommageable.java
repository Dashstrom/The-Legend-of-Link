package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.entity.dommageable.Dommageable;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SpriteDommageable<E extends Dommageable> extends SpriteOriented<E> {

    /**
     * Add an animation from frames to the sprite
     *
     * @param pane   Pane of sprite
     * @param entity Entity to anchor
     * @param dx     location relative to the image entit in X
     * @param dy     location relative to the image entit in Y
     * @param frames array of images
     **/
    public SpriteDommageable(ZPane pane, E entity, int dx, int dy, Image... frames) {
        super(pane, entity, dx, dy, frames);
        entity.hpProperty().addListener((p, o, n) -> {
            if (o.intValue() != n.intValue())
                colorize(o.intValue() - n.intValue() > 0 ? Color.RED : Color.GREEN, 50, 0.5);
        });
    }

}
