package fr.dashstrom.model.assignable.consumable;

import fr.dashstrom.model.assignable.AssignableException;
import fr.dashstrom.model.entity.dommageable.fighter.Player;

public class Potion extends Consumable {

    private final int heal;

    public Potion(Player p, int heal) {
        super(p, "Potion de vie", accord(heal));
        this.heal = heal;
    }

    /**
     * Give the right name
     *
     * @param heal heal power
     * @return name
     */
    private static String accord(int heal) {
        String desc = "Restaure ";
        if (heal % 2 == 0) {
            desc += heal / 2 + " coeur";
            if (heal != 2)
                desc += "s";
        } else {
            desc += heal + " demi-coeur";
            if (heal != 1)
                desc += "s";
        }
        return desc;
    }

    public int getHeal() {
        return heal;
    }

    @Override
    protected void utilization() throws AssignableException {
        if (getPlayer().getHp() == getPlayer().getMaxHP())
            throw new AssignableException("Point de vie déjà au maximum");
        getPlayer().heal(heal);
    }

}
