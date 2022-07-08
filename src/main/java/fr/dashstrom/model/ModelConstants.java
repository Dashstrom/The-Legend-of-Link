package fr.dashstrom.model;

public interface ModelConstants {

    int
        N = 0,
        NE = 1,
        E = 2,
        SE = 3,
        S = 4,
        SW = 5,
        W = 6,
        NW = 7,
        MOVING = 8,
        TOO_FAR = Integer.MAX_VALUE,
        PLAYER_DEFAULT_HP = 30,
        PLAYER_HEAD = -20,
        TILE_SIZE = 32,
        TILES_ROWS = 50,
        TILES_LINES = 28;

    int[][] PRIORITY_DEPLACEMENTS = {
        {0, -1},
        {1, 0},
        {0, 1},
        {-1, 0},
        {-1, -1},
        {1, -1},
        {1, 1},
        {-1, 1}
    };

    int[] POS_CONVERT = {NW, N, NE, W, S, E, SW, S, SE};

}
