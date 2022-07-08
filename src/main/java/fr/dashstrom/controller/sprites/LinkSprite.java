package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.ControllerConstants;
import fr.dashstrom.controller.Images;
import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.entity.interactable.Npc;

import static fr.dashstrom.model.ModelConstants.*;

public class LinkSprite extends SpriteOriented<Npc> {

    public LinkSprite(ZPane pane, Npc npc) {
        super(pane, npc, -45, -65, Images.LINK.get());
        addAnimLoop(S, ControllerConstants.FPS / 120, 15);
        addAnimLoop(N, ControllerConstants.FPS / 120, 10);
        addAnimLoop(E, ControllerConstants.FPS / 120, 9);
        addAnimLoop(W, ControllerConstants.FPS / 120, 0);
        addAnimLoop(S + MOVING, ControllerConstants.FPS / 15, 16, 17, 18, 19);
        addAnimLoop(N + MOVING, ControllerConstants.FPS / 15, 11, 12, 13, 14);
        addAnimLoop(E + MOVING, ControllerConstants.FPS / 15, 5, 6, 7, 8);
        addAnimLoop(W + MOVING, ControllerConstants.FPS / 15, 1, 2, 3, 4);
        changeOrientation();
    }

}