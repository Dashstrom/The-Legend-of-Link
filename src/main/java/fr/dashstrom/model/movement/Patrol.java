package fr.dashstrom.model.movement;

import fr.dashstrom.model.entity.Entity;
import fr.dashstrom.model.graph.BFS;

import java.util.*;

public class Patrol extends FollowPath {

    private final ArrayList<int[]> path;
    private Deque<int[]> tempPath;
    private int index;
    private long ageTempPath;

    public Patrol(Entity entity, int speed) {
        super(entity, speed, false);
        path = new ArrayList<>();
        path.add(new int[]{entity.getWalkbox().centerX(), entity.getWalkbox().centerY()});
        tempPath = new ArrayDeque<>();
        index = 0;
        ageTempPath = 0;
    }

    private int nextIndex() {
        index = (index + 1) % (path.size() * 2);
        return index();
    }

    private int index() {
        return index < path.size() ? index : path.size() * 2 - index - 1;
    }

    public void addPoint(int[] point) {
        if (point.length != 2)
            throw new MoveException("Patrol points must be in 2 dimension, got " + point.length + " : " + Arrays.toString(point));
        int[] pt = path.get(path.size() - 1);
        Deque<int[]> paths = BFS.bestFreePathTo(getWalkbox().abs(pt[0], pt[1]), point[0], point[1], Integer.MAX_VALUE);
        if (paths == null)
            throw new MoveException("Can't find path to " + Arrays.toString(point));

        // reverse path
        Iterator<int[]> iter = paths.descendingIterator();
        while (iter.hasNext())
            path.add(iter.next());
    }

    public int[] next() {
        int lastIndex = index();
        if (tempPath == null || tempPath.isEmpty() && ageTempPath % 60 == 0) {
            int[] pt;
            do {
                pt = path.get(nextIndex());
            } while (!getWalkbox().abs(pt[0], pt[1]).walkable());
            tempPath = BFS.bestWalkablePathTo(getWalkbox(), pt[0], pt[1], 256);
            if (tempPath == null) {
                while (nextIndex() != lastIndex) ;
                do {
                    ageTempPath++;
                    pt = path.get(nextIndex());
                } while (!getWalkbox().abs(pt[0], pt[1]).walkable());
                tempPath = BFS.bestWalkablePathTo(getWalkbox(), pt[0], pt[1], 256);
                if (tempPath == null) {
                    return null;
                } else {
                    ageTempPath = 0;
                    return tempPath.removeLast();
                }
            } else if (tempPath.isEmpty()) {
                ageTempPath = 0;
                return pt;
            }

        }
        return tempPath.removeLast();
    }

}
