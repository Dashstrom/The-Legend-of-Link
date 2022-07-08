package fr.dashstrom.model.assignable.weapon;

import fr.dashstrom.model.Element;
import fr.dashstrom.model.entity.dommageable.fighter.Player;

public class MasterAxe extends CompositeWeapon {

    public MasterAxe(Player p) {
        super(p, "Hache Ultime", new Axe(p, 5, 35, "melee", Element.NORMAL), new SlashStaff(p));
    }

}
