package fr.dashstrom.model.effect;

import fr.dashstrom.model.entity.dommageable.Dommageable;

public class Stun extends Accel {

    public Stun(Dommageable dommageable, int duration) {
        super(dommageable, duration, -100);
    }

}
