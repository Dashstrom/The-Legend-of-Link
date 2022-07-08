package fr.dashstrom.model.graph;

import fr.dashstrom.model.entity.Entity;
import fr.dashstrom.model.Land;
import fr.dashstrom.model.box.WalkBox;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Predicate;

import static fr.dashstrom.model.ModelConstants.PRIORITY_DEPLACEMENTS;

public class BFS {

    private static final int[] END_PATH = {Integer.MIN_VALUE, Integer.MIN_VALUE};

    private static Deque<int[]> reassemblePath(int[][][] paths, int[] end) {
        Deque<int[]> path;
        if (end != END_PATH) {
            path = new ArrayDeque<>();
            int[] step = end;
            while (step != END_PATH) {
                path.addLast(step);
                step = paths[step[1]][step[0]];
            }
            path.removeLast();
        } else {
            path = null;
        }
        return path;
    }

    private static Deque<int[]> bfs(
        WalkBox box,
        Predicate<WalkBox> invalidatorEmplacement,
        Predicate<WalkBox> validatorEmplacement,
        int depthMax
    ) {
        Land land = box.getAnchor().getLand();
        int xStart = box.anchorX(), yStart = box.anchorY();
        int[] target = END_PATH;

        int width = land.width(), height = land.height();
        int[][][] paths = new int[height][width][];
        paths[yStart][xStart] = END_PATH;
        Deque<int[]> current;
        Deque<int[]> nexts = new LinkedList<>();
        nexts.add(new int[]{xStart, yStart});
        int i = 0;
        do {
            current = nexts;
            nexts = new LinkedList<>();
            while (!current.isEmpty() && target == END_PATH) {
                int[] currentPos = current.remove();
                int x = currentPos[0], y = currentPos[1];
                if (validatorEmplacement.test(box.abs(x, y))) {
                    target = currentPos;
                } else {
                    for (int[] direction : PRIORITY_DEPLACEMENTS) {
                        int nearX = x + direction[0], nearY = y + direction[1];
                        if (0 <= nearY && nearY < height && 0 <= nearX && nearX < width && paths[nearY][nearX] == null) {
                            if (invalidatorEmplacement.test(box.abs(nearX, nearY))) {
                                paths[nearY][nearX] = END_PATH;
                            } else {
                                nexts.addLast(new int[]{nearX, nearY});
                                paths[nearY][nearX] = currentPos;
                            }
                        }
                    }
                }
            }
            i++;
        } while (!nexts.isEmpty() && target == END_PATH && i < depthMax);
        return reassemblePath(paths, target);
    }

    public static Deque<int[]> bestWalkablePathTo(Entity entity, Entity other, int depthMax) {
        return bfs(entity.getWalkbox(), b -> !(b.walkable() || b.intersect(other.getWalkbox())),
            b -> b.intersect(other.getWalkbox()), depthMax);
    }

    public static Deque<int[]> bestWalkablePathTo(WalkBox box, int xb, int yb, int depthMax) {
        return bfs(box, b -> !b.walkable(), b -> b.anchorX() == xb && b.anchorY() == yb, depthMax);
    }

    public static Deque<int[]> bestFreePathTo(WalkBox box, int x, int y, int depthMax) {
        return bfs(box, b -> b.isBarrier() || b.isObstructed(), b1 -> b1.anchorX() == x && b1.anchorY() == y, depthMax);
    }

}
