package fr.dashstrom.model.movement;

import fr.dashstrom.model.entity.Entity;

public class OrthogonalMove extends StraightMove {

    private final boolean vertical;

    public OrthogonalMove(Entity entity, int speed, boolean vertical) {
        super(entity, speed, false);
        this.vertical = vertical;
    }

    public boolean isVertical() {
        return vertical;
    }

    @Override
    public void direction(int ax, int ay) {
        if (vertical)
            super.direction(0, ay);
        else
            super.direction(ax, 0);
    }

}
