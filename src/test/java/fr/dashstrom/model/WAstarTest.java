package fr.dashstrom.model;

import fr.dashstrom.model.box.WalkBox;
import fr.dashstrom.model.entity.dommageable.fighter.Player;
import fr.dashstrom.model.entity.dommageable.fighter.enemy.Bug;
import org.junit.jupiter.api.Test;

import static fr.dashstrom.model.graph.WAStar.subOptimalWalkablePathTo;
import static org.junit.jupiter.api.Assertions.*;

public class WAstarTest {

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

    public int coord(int a) {
        return (a * 32) + 16;
    }

    @Test
    void testWAstar() {
        clear();
        int[] j = null;
        p.setX(coord(20));
        p.setY(coord(20));
        Bug bug1 = new Bug(g.getLand(), coord(10), coord(19));
        assertNull(subOptimalWalkablePathTo(bug1.getWalkbox(), p, Integer.MAX_VALUE), "cas : Chemin impossible");
        bug1.setX(coord(24));
        bug1.setY(coord(11));
        assertNotNull(subOptimalWalkablePathTo(bug1.getWalkbox(), p, Integer.MAX_VALUE), "cas : Chemin possible");
        for (int[] i : subOptimalWalkablePathTo(bug1.getWalkbox(), p, Integer.MAX_VALUE)) {
            assertEquals(2, i.length, "cas : Points connexes");
            if (j != null) {
                int distance = UtilsModel.distance(i[0], i[1], j[0], j[1]);
                assertEquals(1, distance,
                    "cas : tt les steps sont connexes (distance de 1 entre chaque points)");
            }
            j = i;
        }
        bug1.setX(coord(20));
        bug1.setY(coord(20));
        assertNotNull(subOptimalWalkablePathTo(bug1.getWalkbox(), p, Integer.MAX_VALUE), "cas : Chemin possible");
        assertEquals(subOptimalWalkablePathTo(bug1.getWalkbox(), p, Integer.MAX_VALUE).size(), 0, "cas : nombre de step = 0");
        bug1.setX(coord(45));
        bug1.setY(coord(24));
        assertNull(subOptimalWalkablePathTo(bug1.getWalkbox(), p, Integer.MAX_VALUE), "cas : Chemin impossible Car dans le mur");

    }

}
