package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.Images;
import fr.dashstrom.controller.UtilsController;
import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.entity.dommageable.fighter.enemy.RedDragon;

public class RedDragonSprite extends SpriteDragonLike<RedDragon> {

    public RedDragonSprite(ZPane pane, RedDragon enemy) {
        super(pane, enemy, -UtilsController.midleX(Images.RED_DRAGON.get()), -UtilsController.midleY(Images.RED_DRAGON.get()), Images.RED_DRAGON.get());
    }

}