package fr.dashstrom.model.movement;

import fr.dashstrom.model.entity.Entity;

public abstract class FollowPath extends Move {

    public FollowPath(Entity entity, int speed, boolean sliding) {
        super(entity, speed, sliding);
    }

    /**
     * Return next point, at a distance of 1, null for don't move
     *
     * @return array of 2 int with first x and second y
     */
    public abstract int[] next();

    public void move() {
        Entity entity = getEntity();
        int distance = 0;
        int[] step = next();
        while (distance < getSpeed() && step != null) {
            int ax = step[0] - entity.getX(), ay = step[1] - entity.getY();
            moveInDirection(ax, ay, 1);
            getEntity().setFace(ax, ay);
            distance++;
            if (distance < getSpeed())
                step = next();
        }
        if (distance == 0) {
            moveInDirection(0, 0, 0);
            getEntity().setFace(0, 0);
        }
    }

}
