package fr.dashstrom.model.assignable.weapon;

import fr.dashstrom.model.Element;
import fr.dashstrom.model.ModelConstants;
import fr.dashstrom.model.entity.dommageable.fighter.Player;
import fr.dashstrom.model.entity.dommageable.projectile.Slash;

public class SlashStaff extends Staff {

    public SlashStaff(Player p) {
        super(p, 1, 150, "Onde de choc", Element.NORMAL);

    }

    public void attack() {
        Player p = getLand().getPlayer();
        if (p.getHp() >= p.getMaxHp()) {
            Slash slash = new Slash(getLand(), p.getX(), p.getY() + ModelConstants.PLAYER_HEAD, p.getFaceX(), p.getFaceY(), getAtk());
            p.getLand().getProjectiles().add(slash);
        }
    }

}