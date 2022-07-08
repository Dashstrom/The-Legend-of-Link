package fr.dashstrom.model.entity.dommageable.projectile;

import fr.dashstrom.model.Element;
import fr.dashstrom.model.Land;
import fr.dashstrom.model.effect.Stun;
import fr.dashstrom.model.entity.dommageable.Dommageable;
import fr.dashstrom.model.entity.dommageable.fighter.PlayerDeadException;

public class Iceball extends Projectile {

    public Iceball(Land land, int x, int y, int ax, int ay, int speed, int range, int atk, boolean friendly, boolean canSplit) {
        super(land, x, y, ax, ay, speed, range, atk, friendly, canSplit, Element.COLD);
    }

    public Iceball(Land land, int x, int y, int ax, int ay, int range, int atk, boolean friendly) {
        super(land, x, y, ax, ay, 15, range, atk, friendly, false, Element.COLD);
    }

    @Override
    public boolean hit(Dommageable entity) {
        entity.addEffect(new Stun(entity, 30));
        return true;
    }

    @Override
    public void die() throws PlayerDeadException {
        if (super.canSplit) {

            for (int nbProj = 0; nbProj < 2; nbProj++) {

                int ay;
                if ((Math.random() * 100) >= 50)
                    ay = -getFaceY();
                else
                    ay = 0;

                int ax;
                if (getFaceX() == 0) {
                    if ((Math.random() * 100) >= 50)
                        ax = 1;
                    else
                        ax = -1;
                } else
                    ax = -getFaceX();

                Fireball f = new Fireball(getLand(), getX(), getY(), ax, ay, 4, 200, 1, false, false);
                getLand().getProjectiles().add(f);
            }
        }
        super.die();
    }

}

