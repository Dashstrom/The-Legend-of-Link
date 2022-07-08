package fr.dashstrom.model.movement;

import fr.dashstrom.model.UtilsModel;
import fr.dashstrom.model.entity.Entity;
import fr.dashstrom.model.graph.WAStar;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Focus extends FollowPath {

    private final Entity focused;
    private Deque<int[]> path;
    private long lastCompute;

    public Focus(Entity entity, int speed, Entity focused) {
        super(entity, speed, true);
        this.focused = Objects.requireNonNull(focused);
        path = new ArrayDeque<>();
        lastCompute = 0;
    }

    private long agePath() {
        return getEntity().getLand().getTicks() - lastCompute;
    }

    private boolean needUpdatePath() {
        long age = agePath();
        return age > 120
            || (path != null
            && age > 10
            && age > path.size() / 3)
            || (UtilsModel.distance(getEntity().getX(), getEntity().getY(), focused.getX(), focused.getY()) < 500
            && age > 6);
    }

    private Deque<int[]> computePath() {
        return WAStar.subOptimalWalkablePathTo(getWalkbox(), focused, 100000);
    }

    public int[] next() {
        if (getWalkbox() == null) {
            int ax = Integer.compare(focused.getX(), getEntity().getX());
            int ay = Integer.compare(focused.getY(), getEntity().getY());
            return new int[]{getEntity().getX() + ax, getEntity().getY() + ay};
        } else {
            if (needUpdatePath()) {
                path = computePath();
                lastCompute = getEntity().getLand().getTicks();
            }
            if (path == null)
                return null;
            try {
                return path.removeLast();
            } catch (NoSuchElementException err) {
                return null;
            }
        }
    }

}
