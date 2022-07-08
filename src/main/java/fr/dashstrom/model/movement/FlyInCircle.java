package fr.dashstrom.model.movement;

import fr.dashstrom.model.entity.Entity;

import static fr.dashstrom.model.ModelConstants.TOO_FAR;

public class FlyInCircle extends StraightMove {

    private final Entity entity, focused;
    private final int radius;

    //CONSTRUCTOR
    public FlyInCircle(Entity entity, Entity focused, int radius) {
        super(entity, 1, false);
        this.entity = entity;
        this.focused = focused;
        this.radius = radius;
    }

    /**
     * Allows a flying entity to get in attack positions all around a target.
     * It is useful ONLY if the attacking entity is a flying object (no walkbox) and if
     * it needs to be aligned in order to throw projectiles at its target.
     */
    public void move() {
        int[][] radiusPoints = new int[7][2];
        int[] directionCoefficientX = {0, 1, 1, 1, 0, -1, -1, -1};
        int[] directionCoefficientY = {1, 1, 0, -1, -1, -1, 0, 1};
        int xPoint = 0, yPoint = 0, dx = TOO_FAR, dy = TOO_FAR;

        // Adds landmark points all around the focused entity. (total of 8 positions)
        for (int count = 0; count < radiusPoints.length; count++) {
            // Shortens the diagonals by reducing the radius [without, we obtain a square around the player
            // not a circle]
            if (count % 2 != 0) {
                xPoint = focused.getX() + (radius * 70 / 100) * directionCoefficientX[count];
                yPoint = focused.getY() + (radius * 70 / 100) * directionCoefficientY[count];
            } else {
                xPoint = focused.getX() + radius * directionCoefficientX[count];
                yPoint = focused.getY() + radius * directionCoefficientY[count];
            }

            radiusPoints[count][0] = xPoint;
            radiusPoints[count][1] = yPoint;
        }

        // Compares the position of the entity and the landmark points to obtain the nearest landmark point.
        for (int[] radiusPoint : radiusPoints) {
            // If the landmark point is nearest than the previous one
            // and if the landmark point is not out of bounds
            if (
                Math.abs(entity.getX() - radiusPoint[0]) <= dx
                    &&
                    Math.abs(entity.getY() - radiusPoint[1]) <= dy
                    &&
                    radiusPoint[0] >= 0
                    &&
                    radiusPoint[1] >= 0) {

                xPoint = radiusPoint[0];
                yPoint = radiusPoint[1];
                dx = Math.abs(entity.getX() - radiusPoint[0]);
                dy = Math.abs(entity.getY() - radiusPoint[1]);
            }
        }

        //If the entity IS NOT on its assigned landmark point, we move it towards the point again.
        if (entity.getX() != xPoint || entity.getY() != yPoint) {
            direction(xPoint - entity.getX(),
                yPoint - entity.getY(),
                focused.getX() - entity.getX(),
                focused.getY() - entity.getY());
            super.move();
        }
    }

}
