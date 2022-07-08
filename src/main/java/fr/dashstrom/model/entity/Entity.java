package fr.dashstrom.model.entity;

import fr.dashstrom.model.entity.dommageable.fighter.PlayerDeadException;
import fr.dashstrom.model.movement.Move;
import fr.dashstrom.model.Land;
import fr.dashstrom.model.UtilsModel;
import fr.dashstrom.model.box.WalkBox;
import fr.dashstrom.model.graph.BFS;
import javafx.beans.property.*;

import static fr.dashstrom.model.ModelConstants.POS_CONVERT;

public abstract class Entity {

    private static int compteur = 1;

    private final IntegerProperty faceX, faceY;
    private final String id;
    private final IntegerProperty x, y;
    private final ObjectProperty<WalkBox> walbox;
    private final BooleanProperty moving;
    private Move move;
    private Land land;

    public Entity(Land land, int x, int y) {
        this.land = land;
        this.move = null;
        this.id = "E" + compteur++;
        this.walbox = new SimpleObjectProperty<>();
        this.faceX = new SimpleIntegerProperty(0);
        this.faceY = new SimpleIntegerProperty(1);
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
        this.moving = new SimpleBooleanProperty(false);
    }

    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    public boolean isMoving() {
        return moving.get();
    }

    public BooleanProperty movingProperty() {
        return moving;
    }

    public int getY() {
        return y.get();
    }

    public void setY(int y) {
        this.y.set(y);
    }

    public int getX() {
        return x.get();
    }

    public void setX(int x) {
        this.x.set(x);
    }

    public void addY(int dy) {
        setY(getY() + dy);
    }

    public void addX(int dx) {
        setX(getX() + dx);
    }

    public IntegerProperty yProperty() {
        return y;
    }

    public IntegerProperty xProperty() {
        return x;
    }

    public WalkBox getWalkbox() {
        return walbox.get();
    }

    public void setWalkbox(int width, int height) {
        setWalkbox(-width / 2, -height / 2, width, height);
    }

    public ObjectProperty<WalkBox> walkboxProperty() {
        return walbox;
    }

    public void setWalkbox(int dx, int dy, int width, int height) {
        walbox.set(new WalkBox(this, dx, dy, width, height));
    }

    public boolean isNear(Entity other, int distanceMax) {
        return UtilsModel.distance(getX(), getY(), other.getX(), other.getY()) <= distanceMax
            && (BFS.bestWalkablePathTo(this, other, distanceMax) != null);
    }

    public void act() throws PlayerDeadException {
        int xBefore = getX(), yBefore = getY();
        if (getMove() != null)
            getMove().move();
        if (!(xBefore == getX() && yBefore == getY()))
            moving.set(true);
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move movement) {
        move = movement;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s(id=%s, x=%d, y=%d)", getClass().getSimpleName(), id, getX(), getY());
    }

    public int orientation() {
        return POS_CONVERT[getFaceX() + 1 + (getFaceY() + 1) * 3];
    }

    public ReadOnlyIntegerProperty faceXProperty() {
        return faceX;
    }

    public ReadOnlyIntegerProperty faceYProperty() {
        return faceY;
    }

    public int getFaceX() {
        return faceX.get();
    }

    public int getFaceY() {
        return faceY.get();
    }

    public void setFace(int ax, int ay) {
        if (!(ay == 0 && ax == 0)) {
            faceX.set(Integer.compare(ax, 0));
            faceY.set(Integer.compare(ay, 0));
        } else {
            moving.set(false);
        }
    }

}
