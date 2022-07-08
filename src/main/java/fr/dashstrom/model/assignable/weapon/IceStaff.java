package fr.dashstrom.model.assignable.weapon;

import fr.dashstrom.model.Element;
import fr.dashstrom.model.ModelConstants;
import fr.dashstrom.model.entity.dommageable.fighter.Player;
import fr.dashstrom.model.entity.dommageable.projectile.Iceball;

public class IceStaff extends Staff {

    public IceStaff(Player p) {
        super(p, 3, 50, "Bâton de glace", Element.COLD);
    }

    public void attack() {
        Player p = getLand().getPlayer();
        Iceball iceball = new Iceball(getLand(), p.getX(), p.getY() + ModelConstants.PLAYER_HEAD, p.getFaceX(), p.getFaceY(), 10, 150, getAtk(), true, false);
        p.getLand().getProjectiles().add(iceball);
    }

}