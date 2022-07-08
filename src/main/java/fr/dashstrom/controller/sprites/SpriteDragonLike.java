package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.entity.dommageable.fighter.Fighter;
import javafx.scene.image.Image;

import static fr.dashstrom.controller.ControllerConstants.FPS;
import static fr.dashstrom.model.ModelConstants.*;

public class SpriteDragonLike<E extends Fighter> extends SpriteDommageable<E> {

    /**
     * Add an animation from frames to the sprite
     *
     * @param pane   Pane of sprite
     * @param entity Entity to anchor
     * @param dx     location relative to the image entit in X
     * @param dy     location relative to the image entit in Y
     * @param frames array of images
     **/
    public SpriteDragonLike(ZPane pane, E entity, int dx, int dy, Image... frames) {
        super(pane, entity, dx, dy, frames);
        addAnimLoop(N, FPS / 20, 0, 1, 2, 1);
        addAnimLoop(W, FPS / 20, 3, 4, 5, 4);
        addAnimLoop(S, FPS / 20, 6, 7, 8, 7);
        addAnimLoop(E, FPS / 20, 9, 10, 11, 10);
        addAnimLoop(N + MOVING, FPS / 10, 0, 1, 2, 1);
        addAnimLoop(W + MOVING, FPS / 10, 3, 4, 5, 4);
        addAnimLoop(S + MOVING, FPS / 10, 6, 7, 8, 7);
        addAnimLoop(E + MOVING, FPS / 10, 9, 10, 11, 10);
        changeOrientation();
    }

}
