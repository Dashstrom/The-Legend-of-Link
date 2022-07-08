package fr.dashstrom.model.effect;

import fr.dashstrom.model.entity.dommageable.Dommageable;
import fr.dashstrom.model.entity.dommageable.fighter.PlayerDeadException;

public abstract class Effect {

    private final int power;
    private final Dommageable dommageable;
    private int duration;

    public Effect(Dommageable dommageable, int duration, int power) {
        this.duration = duration;
        this.dommageable = dommageable;
        this.power = power;
    }

    public Dommageable getDommageable() {
        return dommageable;
    }

    public void decrease() {
        duration--;
    }

    public int getPower() {
        return power;
    }

    public boolean consumed() {
        return duration <= 0;
    }

    public int getDuration() {
        return duration;
    }

    public abstract void consume() throws PlayerDeadException;

    public abstract void endConsume();

    public void act() throws PlayerDeadException {
        decrease();
        if (consumed())
            endConsume();
        else
            consume();
    }

    @Override
    public String toString() {
        return String.format("%s(duration=%d, power=%d)", getClass().getSimpleName(), duration, power);
    }

}
