package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.entity.Entity;
import javafx.scene.image.Image;

import static fr.dashstrom.model.ModelConstants.*;

public class SpriteOriented<E extends Entity> extends Sprite<E> {

    /**
     * Add an animation from frames to the sprite
     *
     * @param entity Entity to anchor
     * @param dx     location relative to the image entit in X
     * @param dy     location relative to the image entit in Y
     * @param frames array of images
     **/
    public SpriteOriented(ZPane pane, E entity, int dx, int dy, Image... frames) {
        super(pane, entity, dx, dy, frames);
        getEntity().faceXProperty().addListener(event -> changeOrientation());
        getEntity().faceYProperty().addListener(event -> changeOrientation());
        getEntity().movingProperty().addListener(event -> changeOrientation());
    }

    public void changeOrientation() {
        Entity e = getEntity();
        int ori = S;
        if (e.getFaceY() == -1)
            ori = N;
        else if (e.getFaceY() == 0) {
            if (e.getFaceX() == 1)
                ori = W;
            else if (e.getFaceX() == -1)
                ori = E;
        }
        if (e.isMoving())
            ori += MOVING;

        if (!playing(ori))
            play(ori);
    }

}
