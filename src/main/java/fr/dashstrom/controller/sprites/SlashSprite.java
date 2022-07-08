package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.Images;
import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.entity.dommageable.projectile.Projectile;
import fr.dashstrom.model.entity.dommageable.projectile.Slash;

public class SlashSprite extends SpriteOriented<Projectile> {

    /**
     * Creates a sprite for a projectile, a fireball
     *
     * @param iceball entity to anchor
     **/
    public SlashSprite(ZPane pane, Slash iceball) {
        super(pane, iceball, -(int) Images.SLASH.get().getWidth() / 2, -(int) Images.SLASH.get().getHeight() / 2, Images.SLASH.get());
    }

    @Override
    public void changeOrientation() {
        setRotate(getEntity().orientation() * 45 - 180);
    }

}
