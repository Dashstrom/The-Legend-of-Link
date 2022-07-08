package fr.dashstrom.model.movement;

import fr.dashstrom.model.box.WalkBox;
import fr.dashstrom.model.entity.Entity;

public class Push extends OrthogonalMove {

    private final OrthogonalMove pushed;

    public Push(Entity entity, OrthogonalMove pushed) {
        super(entity, 1, pushed.isVertical());
        this.pushed = pushed;
        stuck();
    }

    private void stuck() {
        WalkBox b = getWalkbox(), bp = pushed.getWalkbox();
        int dx, dy;
        int ex = getEntity().getX(), ey = getEntity().getY();
        int px = bp.anchorX(), py = bp.anchorY();
        if (pushed.isVertical()) {
            dy = py + (b.getHeight() / 2 + bp.getHeight() / 2) * Integer.compare(ey, py);
            dx = ex;
        } else {
            dx = px + (b.getWidth() / 2 + bp.getWidth() / 2) * Integer.compare(ex, px);
            dy = ey;
        }

        if (b.abs(dx, dy).walkable()) {
            getEntity().setX(dx);
            getEntity().setY(dy);
        } else {
            throw new MoveException("can't lock entity to pushed");
        }
    }

    @Override
    public int moveInDirection(int ax, int ay, int distance) {
        WalkBox pushBox = pushed.getWalkbox(), box = getWalkbox();
        int rest, pushX = pushBox.centerX(), pushY = pushBox.centerY(), x = box.centerX(), y = box.centerY();
        // Need to push one before the other depend of placement
        if (ax > 0 && x > pushX || ax < 0 && x < pushX || ay > 0 && y > pushY || ay < 0 && y < pushY) {
            rest = super.moveInDirection(ax, ay, distance);
            rest = pushed.moveInDirection(ax, ay, distance - rest);
            super.moveInDirection(-ax, -ay, rest);
        } else {
            rest = pushed.moveInDirection(ax, ay, distance);
            rest = super.moveInDirection(ax, ay, distance - rest);
            pushed.moveInDirection(-ax, -ay, rest);
        }
        return rest;
    }

}
