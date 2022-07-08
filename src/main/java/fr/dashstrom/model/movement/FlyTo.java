package fr.dashstrom.model.movement;

import fr.dashstrom.model.entity.Entity;

public class FlyTo extends FollowPath {

    private final String flyTo;
    private final int circleRadius;
    private final int[][] circlePoints;
    private int position;

    public FlyTo(Entity entity, int speed, boolean sliding, String flyTo, int circleRadius, int position) {
        super(entity, speed, sliding);
        this.flyTo = flyTo;
        this.circleRadius = circleRadius;
        this.position = position;
        this.circlePoints = createCircle();
    }

    /**
     * Initializes a circle of 8 points around the center of the arena.
     *
     * @return a array of points [pointX][pointY]
     */
    private int[][] createCircle() {
        int[][] radiusPoints = new int[7][2];
        int[] directionCoefficientX = {0, 1, 1, 1, 0, -1, -1, -1};
        int[] directionCoefficientY = {1, 1, 0, -1, -1, -1, 0, 1};
        int xPoint, yPoint;

        // Adds landmark points all around the center of the arena. (total of 8 positions)
        for (int count = 0; count < radiusPoints.length; count++) {
            // Shortens the diagonals by reducing the radius [without, we obtain a square around the player
            // not a circle]
            if (count % 2 != 0) {
                xPoint = 800 + (circleRadius * 70 / 100) * directionCoefficientX[count];
                yPoint = 500 + (circleRadius * 70 / 100) * directionCoefficientY[count];
            } else {
                xPoint = 800 + circleRadius * directionCoefficientX[count];
                yPoint = 500 + circleRadius * directionCoefficientY[count];
            }

            radiusPoints[count][0] = xPoint;
            radiusPoints[count][1] = yPoint;
        }
        return radiusPoints;
    }

    @Override
    public int[] next() {
        if (flyTo.equals("center")) {
            int ax = Integer.compare(800, getEntity().getX());
            int ay = Integer.compare(450, getEntity().getY());
            return new int[]{getEntity().getX() + ax, getEntity().getY() + ay};
        } else {
            int[] nextPoint = obtainNextPoint();
            int ax = Integer.compare(nextPoint[0], getEntity().getX());
            int ay = Integer.compare(nextPoint[1], getEntity().getY());
            return new int[]{getEntity().getX() + ax, getEntity().getY() + ay};
        }
    }

    private int[] obtainNextPoint() {

        if (position == -1)
            position = 0;
        else {
            //If the entity IS NOT on its assigned landmark point, we move it towards the point again.
            if (getEntity().getX() != circlePoints[position][0] || getEntity().getY() != circlePoints[position][1]) {
                return new int[]{circlePoints[position][0], circlePoints[position][1]};
            } else {
                if (position >= 6)
                    position = -1;
                return new int[]{circlePoints[(position + 1)][0], circlePoints[(position + 1)][1]};
            }
        }
        return new int[]{0, 0};
    }

}
