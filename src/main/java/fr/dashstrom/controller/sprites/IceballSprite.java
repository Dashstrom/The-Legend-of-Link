package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.Images;
import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.entity.dommageable.projectile.Iceball;
import fr.dashstrom.model.entity.dommageable.projectile.Projectile;

public class IceballSprite extends SpriteOriented<Projectile> {

    /**
     * Creates a sprite for a projectile, a fireball
     *
     * @param iceball entity to anchor
     **/
    public IceballSprite(ZPane pane, Iceball iceball) {
        super(pane, iceball, -16, -16, Images.ICEBALL.get());
    }

    @Override
    public void changeOrientation() {
        setRotate(getEntity().orientation() * 45 - 180);
    }

}
