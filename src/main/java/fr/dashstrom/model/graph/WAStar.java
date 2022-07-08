package fr.dashstrom.model.graph;

import fr.dashstrom.model.entity.Entity;
import fr.dashstrom.model.Land;
import fr.dashstrom.model.UtilsModel;
import fr.dashstrom.model.box.WalkBox;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.function.Predicate;

import static fr.dashstrom.model.ModelConstants.PRIORITY_DEPLACEMENTS;

public class WAStar {

    private static double heuristique(int x1, int y1, int x2, int y2) {
        return UtilsModel.distance(x1, y1, x2, y2) * 4.0;
    }

    private static Deque<int[]> reassemblePath(Node end) {
        Deque<int[]> path = new ArrayDeque<>();
        Node step = end;
        while (step.from != null) {
            path.addLast(new int[]{step.x, step.y});
            step = step.from;
        }
        return path;
    }

    private static Deque<int[]> awstar(
        WalkBox box,
        Predicate<WalkBox> invalidatorEmplacement,
        Predicate<WalkBox> validatorEmplacement,
        int x,
        int y,
        int max
    ) {
        int sx = box.centerX(), sy = box.centerY();
        int cpt = 0;
        Land land = box.getAnchor().getLand();

        int width = land.width(), height = land.height();
        boolean[][] closed = new boolean[height][width];
        Node[][] openArrray = new Node[height][width];
        PriorityQueue<Node> openList = new PriorityQueue<>();

        Node s = new Node(sx, sy, 0, heuristique(sx, sy, x, y), null);
        openList.add(s);
        openArrray[sy][sx] = s;
        Deque<int[]> path = null;

        while (!openList.isEmpty() && path == null && cpt < max) {
            Node node = openList.remove();
            openArrray[node.y][node.x] = null;
            if (validatorEmplacement.test(box.abs(node.x, node.y))) {
                path = reassemblePath(node);
            } else {
                double cout = node.cout + 1;
                for (int[] dpos : PRIORITY_DEPLACEMENTS) {
                    int dx = node.x + dpos[0], dy = node.y + dpos[1];
                    try {
                        if (!closed[dy][dx]) {
                            Node oldVoisin = openArrray[dy][dx];
                            if (oldVoisin == null || cout < oldVoisin.cout) {
                                if (invalidatorEmplacement.test(box.abs(dx, dy))) {
                                    closed[dy][dx] = true;
                                } else {
                                    Node voisin = new Node(dx, dy, cout, cout + heuristique(dx, dy, x, y), node);
                                    openArrray[dy][dx] = voisin;
                                    openList.remove(oldVoisin);
                                    openList.add(voisin);
                                }
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {
                    }
                }
                closed[node.y][node.x] = true;
            }
            cpt++;
        }
        return path;
    }

    public static Deque<int[]> subOptimalWalkablePathTo(WalkBox walkBox, Entity target, int max) {
        if (target.getWalkbox() == null)
            return subOptimalWalkablePathTo(walkBox, target.getX(), target.getY(), max);
        else
            return awstar(walkBox, b -> b.isBarrier() || b.isObstructed(), b -> b.intersect(target.getWalkbox()), target.getX(), target.getY(), max);
    }

    public static Deque<int[]> subOptimalWalkablePathTo(WalkBox walkBox, int x, int y, int max) {
        return awstar(walkBox, b -> b.isBarrier() || b.isObstructed(), b -> b.centerY() == y && b.centerX() == x, x, y, max);
    }

    private static class Node implements Comparable<Node> {

        private final int x, y;
        private final double cout, heuristique;
        private final Node from;

        public Node(int x, int y, double cout, double heuristique, Node from) {
            this.x = x;
            this.y = y;
            this.cout = cout;
            this.heuristique = heuristique;
            this.from = from;
        }

        @Override
        public String toString() {
            return "Node({" + x + ", " + y + "}, c=" + cout + ", h=" + heuristique + ")";
        }

        @Override
        public int compareTo(Node n) {
            return Double.compare(heuristique, n.heuristique);
        }

    }

}
