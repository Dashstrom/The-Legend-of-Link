package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.Images;
import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.entity.dommageable.projectile.Fireball;
import fr.dashstrom.model.entity.dommageable.projectile.Projectile;

public class FireballSprite extends SpriteOriented<Projectile> {

    /**
     * Creates a sprite for a projectile, a fireball
     *
     * @param fireball entity to anchor
     **/
    public FireballSprite(ZPane pane, Fireball fireball) {
        super(pane, fireball, -16, -16, Images.FIREBALL.get());
    }

    @Override
    public void changeOrientation() {
        setRotate(getEntity().orientation() * 45 - 180);
    }

}
