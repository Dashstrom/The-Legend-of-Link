package fr.dashstrom.controller;

import javafx.scene.input.KeyCode;

import static javafx.scene.input.KeyCode.*;

public interface ControllerConstants {

    double FPS = 60;
    int
        DEPTH = 20,
        FLY_DEPTH = 15,
        HEIGHT = 896,
        WIDTH = 1600,
        MEDIUM_SIZE = 6;

    KeyCode
        UP = Z,
        DOWN = S,
        LEFT = Q,
        RIGHT = D,
        INVENTORY = I,
        ATTACK1 = J,
        ATTACK2 = K,
        ATTACK3 = L,
        INTERACT = H,
        EXIT_GAME = ESCAPE;

}
