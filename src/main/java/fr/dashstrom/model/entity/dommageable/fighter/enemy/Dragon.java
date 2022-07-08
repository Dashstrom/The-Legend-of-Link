package fr.dashstrom.model.entity.dommageable.fighter.enemy;

import fr.dashstrom.model.movement.FlyInCircle;
import fr.dashstrom.model.movement.Focus;
import fr.dashstrom.model.Element;
import fr.dashstrom.model.Land;
import fr.dashstrom.model.UtilsModel;
import fr.dashstrom.model.entity.dommageable.fighter.Fighter;
import fr.dashstrom.model.entity.dommageable.fighter.PlayerDeadException;

public abstract class Dragon extends Fighter {

    private final int atk;

    public Dragon(Land land, int x, int y, int pv, int atk, Element element) {
        super(land, x, y, pv, -24, -24, 48, 48, element);
        this.atk = atk;
    }

    public int getAtk() {
        return atk;
    }

    @Override
    public void duringAttackAvaibable() {
        //Enables the dragon to ice_fire only if it is near the player
        if (isInRadius(500)) {
            attack();
        }
        setCooldown(240);
    }

    public abstract void attack();

    /**
     * Gives the X and Y directions to the fireballs
     *
     * @return an array of int of 6 cases (1 case = 1 direction)
     */
    public int[] getProjectilesDirections() {
        // UP
        if (getFaceX() == 0 && getFaceY() > 0)
            return new int[]{0, 1, -1, 1, 1, 1};
            // DOWN
        else if (getFaceX() == 0 && getFaceY() < 0)
            return new int[]{0, 1, -1, -1, -1, -1};
            // RIGHT
        else if (getFaceX() > 0 && getFaceY() == 0)
            return new int[]{1, 1, 1, -1, 0, 1};
            // LEFT
        else if (getFaceX() < 0 && getFaceY() == 0)
            return new int[]{-1, -1, -1, -1, 0, 1};
            // RIGHT UP
        else if (getFaceX() > 0 && getFaceY() < 0)
            return new int[]{0, 1, 1, -1, -1, 0};
            // RIGHT DOWN
        else if (getFaceX() > 0 && getFaceY() > 0)
            return new int[]{1, 1, 0, 0, 1, 1};
            // LEFT UP
        else if (getFaceX() < 0 && getFaceY() < 0)
            return new int[]{0, -1, -1, -1, -1, 0};
            // LEFT DOWN
        else
            return new int[]{-1, -1, 0, 0, 1, 1};
    }

    @Override
    public void act() throws PlayerDeadException {

        boolean focusing = isInRadius(600);
        boolean positioning = isInRadius(400);

        //Player detected - Inside the Attack radius
        if (positioning)
            setMove(new FlyInCircle(this, getLand().getPlayer(), 200));

            //Player detected - Inside the Moving radius
        else if (focusing)
            setMove(new Focus(this, 2, getLand().getPlayer()));

            //Player not detected
        else
            setMove(null);

        super.act();
    }

    public boolean isInRadius(int radius) {
        return UtilsModel.distance(getX(), getY(), getLand().getPlayer().getX(), getLand().getPlayer().getY()) <= radius;
    }

}
