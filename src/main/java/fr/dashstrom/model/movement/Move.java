package fr.dashstrom.model.movement;

import fr.dashstrom.model.Land;
import fr.dashstrom.model.box.WalkBox;
import fr.dashstrom.model.entity.Entity;

import java.util.Objects;

public abstract class Move {

    private static final int SLIDING_MAX = 16;

    private final boolean sliding;
    private final Entity entity;
    private final int speed;

    public Move(Entity entity, int speed, boolean sliding) {
        this.sliding = sliding;
        this.entity = Objects.requireNonNull(entity);
        this.speed = speed;
    }

    /**
     * Move with sliding
     *
     * @param ax       x coefficient
     * @param ay       y coefficient
     * @param distance distance to move
     * @return rest of distance
     */
    public int moveInDirection(int ax, int ay, int distance) {
        ax = Integer.compare(ax, 0);
        ay = Integer.compare(ay, 0);
        int rest;
        if ((ax == 0 && ay == 0) || distance == 0)
            return 0;
        if (getWalkbox() == null) {
            getEntity().addX(ax * distance);
            getEntity().addY(ay * distance);
            rest = 0;
        } else {
            rest = rawMove(ax, ay, distance);
            boolean succesSlide = sliding;
            while (rest != 0 && succesSlide) {
                succesSlide = slide(ax, ay);
                if (succesSlide)
                    rest = rawMove(ax, ay, --rest);
            }
        }
        return rest;
    }

    /**
     * Raw move
     *
     * @param ax       x coefficient
     * @param ay       y coefficient
     * @param distance distance to move
     * @return rest of distance
     */
    private int rawMove(int ax, int ay, int distance) {
        int dxy = 0;
        if (ax == 0 && ay == 0)
            return 0;

        if (getWalkbox() != null) {
            while (dxy < distance
                && getWalkbox().rel(dxy * ax + ax, dxy * ay + ay).walkable()) {
                dxy++;
            }
            getEntity().addX(dxy * ax);
            getEntity().addY(dxy * ay);
        } else {
            getEntity().addX(distance * ax);
            getEntity().addY(distance * ay);
        }
        return distance - dxy;
    }

    /**
     * Resolve end of movement if colide
     *
     * @param ax x coefficient
     * @param ay y coefficient
     * @return true if sliding is resolved and moved else false
     */
    private boolean slide(int ax, int ay) {
        WalkBox box = getWalkbox();
        boolean canUp = false, canDown = false, obstacleUp = false, obstacleDown = false;
        int dxy;
        if (ax == 0 || ay == 0) {
            for (dxy = 0; dxy <= SLIDING_MAX; dxy++) {
                if (!obstacleUp) {
                    obstacleUp = !box.rel(ay * dxy, ax * dxy).walkable();
                    canUp = !obstacleUp && box.rel(ay * dxy + ax, ax * dxy + ay).walkable();
                }
                if (!obstacleDown) {
                    obstacleDown = !box.rel(-ay * dxy, -ax * dxy).walkable();
                    canDown = !obstacleDown && box.rel(-ay * dxy + ax, -ax * dxy + ay).walkable();
                }
                if (canUp && canDown) {
                    return false;
                } else if (canUp) {
                    getEntity().addX(ay);
                    getEntity().addY(ax);
                    return true;
                } else if (canDown) {
                    getEntity().addX(-ay);
                    getEntity().addY(-ax);
                    return true;
                }
            }
        } else {
            if (box.rel(0, ay).walkable()) {
                getEntity().addY(ay);
                return true;
            } else if (box.rel(ax, 0).walkable()) {
                getEntity().addX(ax);
                return true;
            }
        }
        return false;
    }

    /**
     * Make a move
     */
    abstract public void move();

    public Entity getEntity() {
        return entity;
    }

    public WalkBox getWalkbox() {
        return entity.getWalkbox();
    }

    public Land getLand() {
        return entity.getLand();
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
            "entity=" + entity +
            ", speed=" + speed +
            ")";
    }

}
