package fr.dashstrom.model;

import fr.dashstrom.model.entity.dommageable.fighter.enemy.RedDragon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DragonTest {

    @Test
    void testGetProjectilesDirections() {
        Game game = new Game();
        Place place = new Place(0, 0);
        Land land = new Land(game, place);
        RedDragon dragonTest = new RedDragon(land, 0, 0, 10, 10);
        assertArrayEquals(dragonTest.getProjectilesDirections(), new int[]{0, 1, -1, 1, 1, 1}, "UP");
        dragonTest.setFace(0, -1);
        assertArrayEquals(dragonTest.getProjectilesDirections(), new int[]{0, 1, -1, -1, -1, -1}, "DOWN");
        dragonTest.setFace(1, 0);
        assertArrayEquals(dragonTest.getProjectilesDirections(), new int[]{1, 1, 1, -1, 0, 1}, "RIGHT");
        dragonTest.setFace(-1, 0);
        assertArrayEquals(dragonTest.getProjectilesDirections(), new int[]{-1, -1, -1, -1, 0, 1}, "LEFT");
        dragonTest.setFace(1, -1);
        assertArrayEquals(dragonTest.getProjectilesDirections(), new int[]{0, 1, 1, -1, -1, 0}, "RIGHT UP");
        dragonTest.setFace(1, 1);
        assertArrayEquals(dragonTest.getProjectilesDirections(), new int[]{1, 1, 0, 0, 1, 1}, "RIGHT DOWN");
        dragonTest.setFace(-1, -1);
        assertArrayEquals(dragonTest.getProjectilesDirections(), new int[]{0, -1, -1, -1, -1, 0}, "LEFT UP");
        dragonTest.setFace(-1, 1);
        assertArrayEquals(dragonTest.getProjectilesDirections(), new int[]{-1, -1, 0, 0, 1, 1}, "LEFT DOWN");
    }

}