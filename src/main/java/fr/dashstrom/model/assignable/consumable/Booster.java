package fr.dashstrom.model.assignable.consumable;

import fr.dashstrom.model.effect.Accel;
import fr.dashstrom.model.effect.Effect;
import fr.dashstrom.model.entity.dommageable.fighter.Player;

public class Booster extends Affector {

    public Booster(Player p) {
        super(p, "Booster", "Vitesse x2 pendant 5s");
    }

    @Override
    protected Effect makeEffect() {
        return new Accel(getPlayer(), 360, 1);
    }

}
