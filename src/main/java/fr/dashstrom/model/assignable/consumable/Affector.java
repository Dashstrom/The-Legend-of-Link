package fr.dashstrom.model.assignable.consumable;

import fr.dashstrom.model.assignable.AssignableException;
import fr.dashstrom.model.effect.Effect;
import fr.dashstrom.model.entity.dommageable.fighter.Player;

public abstract class Affector extends Consumable {

    public Affector(Player p, String name, String description) {
        super(p, name, description);
    }

    @Override
    protected void utilization() throws AssignableException {
        getPlayer().addEffect(makeEffect());
    }

    abstract protected Effect makeEffect() throws AssignableException;

}
