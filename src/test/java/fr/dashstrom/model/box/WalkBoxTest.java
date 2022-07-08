package fr.dashstrom.model.box;

import fr.dashstrom.model.Game;
import fr.dashstrom.model.Land;
import fr.dashstrom.model.entity.dommageable.destructible.Ice;
import fr.dashstrom.model.entity.dommageable.fighter.Player;
import fr.dashstrom.model.entity.dommageable.fighter.enemy.Bug;
import fr.dashstrom.model.entity.interactable.Crate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WalkBoxTest {

    private static final Game g = new Game();
    private static final Player p = g.getPlayer();
    private static final WalkBox wb = p.getWalkbox();

    private void clear() {
        g.getProjectiles().clear();
        g.getEnemies().clear();
        g.getPickables().clear();
        g.getInteractables().clear();
        g.getDestructible().clear();
        g.setLand(51, 42);
        g.getPlayer().setX(32);
        g.getPlayer().setY(32);
    }

    @Test
    final void testWalkboxPlayerNotNull() {
        clear();
        assertNotNull(wb, "walkbox of player is null");
    }

    @Test
    final void testAnchorX() {
        clear();
        assertEquals(wb.anchorX(), p.getX(), "cas bon anchorX");
        assertNotEquals(wb.rel(1, 1).anchorX(), p.getX(), "cas mauvais anchorX");

        p.setX(100);
        assertEquals(wb.anchorX(), p.getX(), "cas move player");
    }

    @Test
    final void testAnchorY() {
        clear();
        assertEquals(wb.anchorY(), p.getY(), "cas bon anchorY");
        assertNotEquals(wb.rel(1, 1).anchorY(), p.getY(), "cas mauvais anchorY");

        p.setY(100);
        assertEquals(wb.anchorY(), p.getY(), "cas move player");
    }

    @Test
    final void testAbs() {
        clear();

        WalkBox wb1 = wb.abs(20, 30);
        assertEquals(wb1.getAnchor().getX(), p.getX(), "axis x of player change with abs");
        assertEquals(wb1.getAnchor().getY(), p.getY(), "axis y of player change with abs");
        assertEquals(wb1.anchorX(), 20, "wrong final position in x");
        assertEquals(wb1.anchorY(), 30, "wrong final position in y");
        WalkBox wb2 = wb1.abs(800, 700);
        WalkBox wb3 = wb2.abs(100, 120);
        assertEquals(wb3.getAnchor().getX(), p.getX(), "axis x of player change with abs many time");
        assertEquals(wb3.getAnchor().getY(), p.getY(), "axis y of player change with abs many time");
        assertEquals(wb3.anchorX(), 100, "wrong final position in x after many abs");
        assertEquals(wb3.anchorY(), 120, "wrong final position in y after many abs");
    }

    @Test
    final void testRel() {
        clear();
        final int X = 5, Y = 9;

        WalkBox wb1 = wb.rel(X, Y);
        assertEquals(wb1.getAnchor().getX(), p.getX(), "axis x of player change with abs");
        assertEquals(wb1.getAnchor().getY(), p.getY(), "axis y of player change with abs");
        assertEquals(wb1.anchorX(), p.getX() + X, "wrong final position in x");
        assertEquals(wb1.anchorY(), p.getY() + Y, "wrong final position in y");
        WalkBox wb2 = wb1.rel(1, 1);
        WalkBox wb3 = wb2.rel(2, 2);
        assertEquals(wb3.getAnchor().getX(), p.getX(), "axis x of player change with abs many time");
        assertEquals(wb3.getAnchor().getY(), p.getY(), "axis y of player change with abs many time");
        assertEquals(p.getX() + X + 3, wb3.anchorX(), "wrong final position in x after many abs");
        assertEquals(p.getY() + Y + 3, wb3.anchorY(), "wrong final position in y after many abs");
    }

    @Test
    final void testIsObstructed() {
        clear();
        Land l = g.getLand();

        p.setX(144);
        p.setY(48);
        assertFalse(wb.isObstructed(), "cas non obstruer");

        g.getInteractables().add(new Crate(l, 144, 48));

        assertTrue(wb.isObstructed(), "cas obstruer par interactable");
        g.getInteractables().clear();

        g.getDestructible().add(new Ice(l, 144, 48));
        assertTrue(wb.isObstructed(), "cas obstruer par destructible");
        g.getDestructible().clear();

        g.getDestructible().add(new Ice(l, 144, 48));
        assertTrue(wb.isObstructed(), "cas obstruer par destructible");
        g.getDestructible().clear();
    }

    @Test
    final void testIsBarrier() {
        clear();

        p.setX(40);
        p.setY(56);
        assertFalse(wb.isBarrier(), "aucune collision");

        p.setX(40);
        p.setY(57);
        assertTrue(wb.isBarrier(), "cas collision en haut");

        p.setX(40);
        p.setY(55);
        assertTrue(wb.isBarrier(), "cas collision en bas");

        p.setX(39);
        p.setY(56);
        assertTrue(wb.isBarrier(), "cas collision à gauche");

        p.setX(57);
        p.setY(56);
        assertTrue(wb.isBarrier(), "cas collision à droite");
    }

    @Test
    final void testWalkable() {
        clear();

        p.setX(16);
        p.setY(16);
        assertFalse(wb.walkable(), "cas collision complete");

        p.setX(144);
        p.setY(48);
        assertTrue(wb.walkable(), "cas aucune collision");

        p.setX(40);
        p.setY(56);
        assertTrue(wb.walkable(), "aucune collision");

        p.setX(40);
        p.setY(57);
        assertFalse(wb.walkable(), "cas collision en haut");

        p.setX(40);
        p.setY(55);
        assertFalse(wb.walkable(), "cas collision en bas");

        p.setX(39);
        p.setY(56);
        assertFalse(wb.walkable(), "cas collision à gauche");

        p.setX(57);
        p.setY(56);
        assertFalse(wb.walkable(), "cas collision à droite");
    }

    @Test
    final void testCenterX() {
        clear();
        assertEquals(wb.anchorY(), p.getY(), "cas bon anchorY");
        assertNotEquals(wb.rel(1, 1).anchorY(), p.getY(), "cas mauvais anchorY");
    }

    @Test
    final void testCenterY() {
        clear();
        assertEquals(wb.centerX(), p.getX(), "cas bon centerX");
        assertNotEquals(wb.rel(1, 1).centerX(), p.getX(), "cas centerX rel");
    }

    @Test
    final void testX1() {
        clear();
        p.setX(0);
        p.setY(0);

        assertEquals(-8, wb.x1(), "cas bon x1");
        assertEquals(-7, wb.rel(1, 1).x1(), "cas x1 rel");
    }

    @Test
    final void testX2() {
        clear();
        p.setX(0);
        p.setY(0);

        assertEquals(8, wb.x2(), "cas bon x2");
        assertEquals(9, wb.rel(1, 1).x2(), "cas x2 rel");
    }

    @Test
    final void testY1() {
        clear();
        p.setX(0);
        p.setY(0);

        assertEquals(-8, wb.y1(), "cas bon y1");
        assertEquals(-7, wb.rel(1, 1).y1(), "cas y1 rel");
    }

    @Test
    final void TestY2() {
        clear();
        p.setX(0);
        p.setY(0);

        assertEquals(8, wb.y2(), "cas bon y2");
        assertEquals(9, wb.rel(1, 1).y2(), "cas y2 rel");
    }

    @Test
    final void TestGetDx() {
        clear();
        p.setX(0);
        p.setY(0);

        assertEquals(-8, wb.getDx(), "cas bon dx");
        assertEquals(-8, wb.rel(1, 1).getDx(), "cas dx rel");
    }

    @Test
    final void TestGetDy() {
        clear();
        assertEquals(-8, wb.getDy(), "cas bon dy");
        assertEquals(-8, wb.rel(1, 1).getDy(), "cas dy rel");
    }

    @Test
    final void TestGetShiftX() {
        clear();
        assertEquals(0, wb.getShiftX(), "cas non abs or rel");
        assertEquals(10, wb.rel(10, 10).getShiftX(), "cas rel");

        p.setX(0);
        p.setY(0);
        assertEquals(100, wb.abs(100, 100).getShiftX(), "cas abs");

    }

    @Test
    final void TestGetShiftY() {
    }

    @Test
    final void TestGetWidth() {
        clear();
        assertEquals(16, wb.getWidth(), "cas width");
        assertEquals(16, wb.rel(1, 1).getWidth(), "cas width rel");
    }

    @Test
    final void TestGetHeight() {
        clear();
        assertEquals(16, wb.getHeight(), "cas height");
        assertEquals(16, wb.rel(1, 1).getHeight(), "cas height rel");
    }

    @Test
    final void TestIntersect() {
        clear();
        assertTrue(wb.intersect(wb.rel(1, 1)), "cas self intersection");

        p.setX(32);
        p.setY(32);
        Bug b = new Bug(g.getLand(), 32, 32);

        g.getEnemies().add(b);
        assertTrue(wb.intersect(b.getWalkbox()), "cas intersection other");
        g.getEnemies().clear();
    }

    @Test
    final void TestIn() {
        clear();

        p.setX(16);
        p.setY(16);
        assertTrue(wb.in(0, 0, 32, 32), "cas vrai");

        p.setX(100);
        p.setY(100);
        assertFalse(wb.in(0, 0, 32, 32), "cas en dehors");

        p.setX(32);
        p.setY(32);
        assertFalse(wb.in(0, 0, 32, 32), "cas limite");
    }

    @Test
    final void TestHasAnchor() {
        clear();
        assertFalse(wb.hasNotAnchor(p), "cas meme anchor");
        assertTrue(wb.hasNotAnchor(new Bug(g.getLand(), 0, 0)), "cas other anchor");
    }

    @Test
    final void TestGetAnchor() {
        clear();
        assertEquals(p, wb.getAnchor(), "cas joueur");
    }

}
