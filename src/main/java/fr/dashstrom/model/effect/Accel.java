package fr.dashstrom.model.effect;

import fr.dashstrom.model.entity.dommageable.Dommageable;

public class Accel extends Effect {

    public Accel(Dommageable dommageable, int duration, int power) {
        super(dommageable, duration, power);
        dommageable.addAccel(power);
    }

    @Override
    public void consume() {
    }

    @Override
    public void endConsume() {
        getDommageable().addAccel(-getPower());
    }

}
