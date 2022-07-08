package fr.dashstrom.model.entity.dommageable.fighter;

import fr.dashstrom.model.Element;
import fr.dashstrom.model.Land;
import fr.dashstrom.model.entity.dommageable.Dommageable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class Fighter extends Dommageable {

    private final IntegerProperty cooldown;

    public Fighter(Land land, int x, int y, int pv, int dxHitbox, int dyHitbox, int widthHitbox, int heightHitbox, Element element) {
        super(land, x, y, pv, dxHitbox, dyHitbox, widthHitbox, heightHitbox, element);
        cooldown = new SimpleIntegerProperty(2);
    }

    @Override
    public void act() throws PlayerDeadException {
        if (cooldown.get() > 0)
            cooldown.set(cooldown.get() - 1);
        super.act();
        if (endedCooldown())
            duringAttackAvaibable();
    }

    public boolean endedCooldown() {
        return cooldown.get() <= 0;
    }

    public ReadOnlyIntegerProperty cooldownProperty() {
        return cooldown;
    }

    public boolean beforeEndCooldown() {
        return cooldown.get() == 1;
    }

    public void setCooldown(int cooldown) {
        this.cooldown.set(Math.max(cooldown, 0));
    }

    /**
     * Called when fighter can attack
     *
     * @throws PlayerDeadException if player is dead
     */
    public abstract void duringAttackAvaibable() throws PlayerDeadException;

}
