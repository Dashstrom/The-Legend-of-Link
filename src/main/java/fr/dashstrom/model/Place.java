package fr.dashstrom.model;

public class Place {

    private final int x, y;

    /**
     * Positioning of a zone
     *
     * @param x x position
     * @param y y position
     */
    public Place(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get X positions on a land scale
     *
     * @return x position
     */
    public int getX() {
        return x;
    }

    /**
     * Get X positions on a land scale
     *
     * @return y position
     */
    public int getY() {
        return y;
    }

    /**
     * Compare two Place
     *
     * @param o Object to test
     * @return True if the Object is a Place and and that he has the same position
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place landPlace = (Place) o;
        return x == landPlace.x && y == landPlace.y;
    }

    /**
     * Test if a place is at a certain coordinate
     *
     * @param x x position
     * @param y y position
     * @return True if it has the same coordinate else false
     */
    public boolean isAt(int x, int y) {
        return x == this.x && y == this.y;
    }

    /**
     * Make an hashCode for HashMap or HashSet
     *
     * @return x << 16 + y
     */
    @Override
    public int hashCode() {
        return x << 16 + y;
    }

    @Override
    public String toString() {
        return "Place{" + x + ", " + y + "}";
    }

}
