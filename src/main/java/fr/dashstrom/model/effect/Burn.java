package fr.dashstrom.model.effect;

import fr.dashstrom.model.entity.dommageable.Dommageable;
import fr.dashstrom.model.entity.dommageable.fighter.PlayerDeadException;
import fr.dashstrom.model.Element;

public class Burn extends Effect {

    public Burn(Dommageable dommageable) {
        super(dommageable, 120, 1);
    }

    @Override
    public void consume() throws PlayerDeadException {
        if (getDuration() % 40 == 0)
            getDommageable().takeDamage(getPower(), Element.HOT);
    }

    @Override
    public void endConsume() {

    }

}
