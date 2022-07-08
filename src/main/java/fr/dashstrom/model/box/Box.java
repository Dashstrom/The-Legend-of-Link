package fr.dashstrom.model.box;

import fr.dashstrom.model.entity.Entity;

public class Box {

    private final int shiftX, shiftY, dx, dy, width, height;
    private final Entity anchor;

    public Box(Entity anchor, int dx, int dy, int width, int height) {
        this(anchor, 0, 0, dx, dy, width, height);
    }

    public Box(Entity anchor, int shiftX, int shiftY, int dx, int dy, int width, int height) {
        if (width < 16)
            throw new IllegalArgumentException("width must be greater or equal 16");
        if (255 < width)
            throw new IllegalArgumentException("width must be lower than 256");
        if (height < 16)
            throw new IllegalArgumentException("height must be greater or equal 16");
        if (255 < height)
            throw new IllegalArgumentException("height must be lower than 256");
        if (anchor == null)
            throw new IllegalArgumentException("Anchor can't be null");
        this.dx = dx;
        this.dy = dy;
        this.width = width;
        this.height = height;
        this.anchor = anchor;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    public int anchorX() {
        return anchor.getX() + shiftX;
    }

    public int anchorY() {
        return anchor.getY() + shiftY;
    }

    public int centerX() {
        return x1() + width / 2;
    }

    public int centerY() {
        return y1() + height / 2;
    }

    public int x1() {
        return anchorX() + dx;
    }

    public int x2() {
        return x1() + width;
    }

    public int y1() {
        return anchorY() + dy;
    }

    public int y2() {
        return y1() + height;
    }

    public int getDx() {
        if (getAnchor().getWalkbox() == this)
            return dx;
        else
            return getAnchor().getWalkbox().getDx();
    }

    public int getDy() {
        if (getAnchor().getWalkbox() == this)
            return dy;
        else
            return getAnchor().getWalkbox().getDy();
    }

    public int getShiftX() {
        return shiftX;
    }

    public int getShiftY() {
        return shiftY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean intersect(Box otherBox) {
        return otherBox != null
            && x1() < otherBox.x2()
            && x2() > otherBox.x1()
            && y1() < otherBox.y2()
            && y2() > otherBox.y1();
    }

    public boolean in(int x, int y, int width, int height) {
        return x <= y1() && y2() <= x + height && y <= x1() && x2() <= y + width;
    }

    public Box rel(int dx, int dy) {
        return new WalkBox(getAnchor(), getShiftX() + dx, getShiftY() + dy, getDx(), getDy(), getWidth(), getHeight());
    }

    /**
     * Obtaining an absolute place box
     *
     * @param x x position
     * @param y y position
     * @return A box placed in x and y always anchored to its entity
     */
    public Box abs(int x, int y) {
        return new Box(
            getAnchor(),
            x - getAnchor().getX(),
            y - getAnchor().getY(),
            getDx(),
            getDy(),
            getWidth(),
            getHeight()
        );
    }

    public boolean hasNotAnchor(Entity entity) {
        return entity != anchor;
    }

    public Entity getAnchor() {
        return anchor;
    }

    @Override
    public String toString() {
        return String.format("%s[x1=%d, y1=%d, w=%d, h=%d]", getClass().getSimpleName(), x1(), y1(), width, height);
    }

}
