package fr.dashstrom.model.movement;

import fr.dashstrom.model.entity.Entity;

public class StraightMove extends Move {

    private int ax, ay;

    public StraightMove(Entity entity, int speed, boolean sliding) {
        super(entity, speed, sliding);
        this.ax = 0;
        this.ay = 0;
    }

    public void move() {
        moveInDirection(getAx(), getAy(), getSpeed());
    }

    /**
     * Changes the direction the entity faces.
     *
     * @param ax x coefficient
     * @param ay y coefficient
     */
    public void direction(int ax, int ay) {
        this.ax = Integer.compare(ax, 0);
        this.ay = Integer.compare(ay, 0);
        getEntity().setFace(getAx(), getAy());
    }

    public void direction(int ax, int ay, int faceX, int faceY) {
        this.ax = Integer.compare(ax, 0);
        this.ay = Integer.compare(ay, 0);
        getEntity().setFace(faceX, faceY);
    }

    public int getAx() {
        return ax;
    }

    public void setAx(int ax) {
        this.ax = ax;
    }

    public int getAy() {
        return ay;
    }

    public void setAy(int ay) {
        this.ay = ay;
    }

}