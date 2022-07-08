package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.ControllerConstants;
import fr.dashstrom.controller.Images;
import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.entity.interactable.Npc;

import static fr.dashstrom.model.ModelConstants.*;

public class NpcSprite extends SpriteOriented<Npc> {

    public NpcSprite(ZPane pane, Npc npc) {
        super(pane, npc, -16, -32, Images.NPC.get());
        addAnimLoop(S, ControllerConstants.FPS / 120, 0, 6, 0, 9);
        addAnimLoop(N, ControllerConstants.FPS / 120, 3, 9, 3, 6);
        addAnimLoop(E, ControllerConstants.FPS / 120, 6, 3, 6, 0);
        addAnimLoop(W, ControllerConstants.FPS / 120, 9, 0, 9, 3);
        addAnimLoop(S + MOVING, ControllerConstants.FPS / 15, 0, 1, 2);
        addAnimLoop(N + MOVING, ControllerConstants.FPS / 15, 3, 4, 5);
        addAnimLoop(E + MOVING, ControllerConstants.FPS / 15, 6, 7, 8);
        addAnimLoop(W + MOVING, ControllerConstants.FPS / 15, 9, 10, 11);
        changeOrientation();
    }

}
