package fr.dashstrom.model;

public class UtilsModel {

    /***
     * Calculate manhattan distance : |x2 - x1| + |y2 - y1|
     *
     * @param x1 x of first point
     * @param y1 y of first point
     * @param x2 x of second point
     * @param y2 y of second point
     * @return manhattan distance
     */
    public static int distance(int x1, int y1, int x2, int y2) {
        return Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
    }

}
