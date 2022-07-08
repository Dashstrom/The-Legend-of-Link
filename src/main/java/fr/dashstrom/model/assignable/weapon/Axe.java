package fr.dashstrom.model.assignable.weapon;

import fr.dashstrom.model.Element;
import fr.dashstrom.model.ModelConstants;
import fr.dashstrom.model.box.Box;
import fr.dashstrom.model.entity.dommageable.fighter.Fighter;
import fr.dashstrom.model.entity.dommageable.fighter.Player;
import fr.dashstrom.model.entity.dommageable.fighter.PlayerDeadException;

public class Axe extends Weapon {

    public Axe(Player p) {
        super(p, 3, 30, "Hache", Element.NORMAL);
    }

    public Axe(Player p, int atk, int cooldown, String name, Element element) {
        super(p, atk, cooldown, name, element);
    }

    public void attack() throws PlayerDeadException {
        Player p = getLand().getPlayer();
        if (p.getLand() != null) {
            Box hit = new Box(p, (p.getFaceX() - 1) * 30, (p.getFaceY() - 1) * 30 + ModelConstants.PLAYER_HEAD, 60, 60);
            for (Fighter e : getLand().getEnemies())
                if (hit.intersect(e.getHitbox()))
                    e.takeDamage(getAtk(), getElement());
        }
    }

}