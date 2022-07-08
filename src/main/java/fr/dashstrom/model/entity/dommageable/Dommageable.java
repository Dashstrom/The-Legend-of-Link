package fr.dashstrom.model.entity.dommageable;

import fr.dashstrom.model.entity.dommageable.fighter.PlayerDeadException;
import fr.dashstrom.model.Element;
import fr.dashstrom.model.Land;
import fr.dashstrom.model.box.Box;
import fr.dashstrom.model.effect.Effect;
import fr.dashstrom.model.entity.Entity;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Dommageable extends Entity {

    private final Box hitbox;
    private final IntegerProperty hp;
    private final IntegerProperty maxHP;
    private final Element element;
    private final ObservableList<Effect> effects;
    private int accel;

    public Dommageable(Land land, int x, int y, int pv, int dx, int dy, int width, int height, Element element) {
        super(land, x, y);
        this.hitbox = new Box(this, dx, dy, width, height);
        this.hp = new SimpleIntegerProperty(pv);
        this.maxHP = new SimpleIntegerProperty(pv);
        this.accel = 1;
        this.element = element;
        this.effects = FXCollections.observableArrayList();
    }

    public Element getElement() {
        return element;
    }

    public void takeDamage(int damage, Element element) throws PlayerDeadException {
        if (damage > 0 && isAlive()) {
            int pvLost = Math.min(hp.get(), element.againstMultiplicator(this.element) * damage);
            hp.set(hp.get() - pvLost);
            if (isDead())
                die();
        }
    }

    public void heal(int restored) {
        if (restored > 0)
            hp.set(Math.min(maxHP.get(), hp.get() + restored));
    }

    public void addMaxHP(int addedHP) {
        maxHP.set(maxHP.get() + addedHP);
        heal(addedHP);
    }

    public Box getHitbox() {
        return this.hitbox;
    }

    public void die() throws PlayerDeadException {
        hp.set(0);
    }

    public boolean isAlive() {
        return getHp() > 0;
    }

    public boolean isDead() {
        return getHp() <= 0;
    }

    public int getHp() {
        return hp.get();
    }

    public int getMaxHp() {
        return maxHP.get();
    }

    public IntegerProperty hpProperty() {
        return hp;
    }

    public int getMaxHP() {
        return maxHP.get();
    }

    public IntegerProperty maxHPProperty() {
        return maxHP;
    }

    public void act() throws PlayerDeadException {
        for (int i = 0; i < getAccel(); i++)
            super.act();
        for (Effect effect : effects) {
            effect.act();
        }
        effects.removeIf(Effect::consumed);
    }

    public void addAccel(int accel) {
        setAccel(getAccel() + accel);
    }

    public void addEffect(Effect effect) {
        if (effect.getDommageable() != this)
            throw new IllegalArgumentException(String.format("Effect '%s' must be added on his own target", effect));
        effects.add(effect);
    }

    public int getAccel() {
        return accel;
    }

    public void setAccel(int accel) {
        this.accel = accel;
    }

}
