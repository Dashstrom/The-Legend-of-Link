package fr.dashstrom.model.entity.dommageable.projectile;

import fr.dashstrom.model.Element;
import fr.dashstrom.model.Land;
import fr.dashstrom.model.effect.Burn;
import fr.dashstrom.model.effect.Stun;
import fr.dashstrom.model.entity.dommageable.Dommageable;
import fr.dashstrom.model.entity.dommageable.fighter.PlayerDeadException;

public class Fireball extends Projectile {

    public Fireball(Land land, int x, int y, int ax, int ay, int speed, int range, int atk, boolean friendly, boolean canSplit) {
        super(land, x, y, ax, ay, speed, range, atk, friendly, canSplit, Element.HOT);
    }

    public Fireball(Land land, int x, int y, int ax, int ay, int range, int atk, boolean friendly) {
        super(land, x, y, ax, ay, 15, range, atk, friendly, false, Element.HOT);
    }

    @Override
    public boolean hit(Dommageable entity) {
        entity.addEffect(new Stun(entity, 10));
        entity.addEffect(new Burn(entity));
        return true;
    }

    @Override
    public void die() throws PlayerDeadException {
        if (super.canSplit) {

            for (int nbProj = 0; nbProj < 2; nbProj++) {

                int dy;
                if ((Math.random() * 100) >= 50)
                    dy = -getFaceY();
                else
                    dy = 0;

                int dx;
                if (getFaceX() == 0) {
                    if ((Math.random() * 100) >= 50)
                        dx = 1;
                    else
                        dx = -1;
                } else
                    dx = -getFaceX();

                Iceball f = new Iceball(getLand(), getX(), getY(), dx, dy, 200, 1, false);
                f.setSpeed(4);
                getLand().getProjectiles().add(f);
            }
        }
        super.die();
    }

}
