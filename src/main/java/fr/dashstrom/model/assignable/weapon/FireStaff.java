package fr.dashstrom.model.assignable.weapon;

import fr.dashstrom.model.Element;
import fr.dashstrom.model.entity.dommageable.fighter.Player;
import fr.dashstrom.model.entity.dommageable.projectile.Fireball;

import static fr.dashstrom.model.ModelConstants.PLAYER_HEAD;

public class FireStaff extends Staff {

    public FireStaff(Player p) {
        super(p, 1, 40, "Bâton de feu", Element.HOT);
    }

    public void attack() {
        Player p = getLand().getPlayer();
        Fireball fireball = new Fireball(getLand(), p.getX(), p.getY() + PLAYER_HEAD, p.getFaceX(), p.getFaceY(),
            200, getAtk(), true);
        p.getLand().getProjectiles().add(fireball);
    }

}
