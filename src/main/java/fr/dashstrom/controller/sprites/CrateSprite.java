package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.Images;
import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.entity.interactable.Crate;

public class CrateSprite extends Sprite<Crate> {

    /**
     * Creates a sprite for a crate
     *
     * @param crate Crate to anchor
     **/
    public CrateSprite(ZPane pane, Crate crate) {
        super(pane, crate, -16, -40, Images.CRATE.get());
    }

}
