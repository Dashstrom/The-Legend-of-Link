package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.Images;
import fr.dashstrom.controller.UtilsController;
import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.entity.dommageable.fighter.enemy.BlueDragon;

public class BlueDragonSprite extends SpriteDragonLike<BlueDragon> {

    public BlueDragonSprite(ZPane pane, BlueDragon enemy) {
        super(pane, enemy, -UtilsController.midleX(Images.BLUE_DRAGON.get()), -UtilsController.midleY(Images.BLUE_DRAGON.get()), Images.BLUE_DRAGON.get());
    }

}