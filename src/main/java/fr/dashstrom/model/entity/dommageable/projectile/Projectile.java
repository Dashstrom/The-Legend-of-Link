package fr.dashstrom.model.entity.dommageable.projectile;

import fr.dashstrom.model.entity.dommageable.destructible.Destructible;
import fr.dashstrom.model.movement.StraightMove;
import fr.dashstrom.model.Element;
import fr.dashstrom.model.Land;
import fr.dashstrom.model.box.Box;
import fr.dashstrom.model.entity.dommageable.Dommageable;
import fr.dashstrom.model.entity.dommageable.fighter.Fighter;
import fr.dashstrom.model.entity.dommageable.fighter.Player;
import fr.dashstrom.model.entity.dommageable.fighter.PlayerDeadException;

public abstract class Projectile extends Fighter {

    private final int atk;
    private final boolean friendly;
    protected boolean canSplit;
    private int speed;

    public Projectile(Land land, int x, int y, int ax, int ay, int speed, int range, int atk, boolean friendly, boolean canSplit, Element element) {
        super(land, x, y, range, -16, -16, 32, 32, element);
        StraightMove move = new StraightMove(this, 1, false);
        move.direction(ax, ay);
        setMove(move);
        this.atk = atk;
        this.speed = speed;
        this.friendly = friendly;
        this.canSplit = canSplit;
    }

    @Override
    public void act() throws PlayerDeadException {
        int compteur = 0;
        while (isAlive() && compteur < speed) {
            super.act();
            takeDamage(1, Element.NORMAL);
            compteur++;
            if (isDead())
                endedRange();
        }
    }

    @Override
    public void duringAttackAvaibable() throws PlayerDeadException {
        Box dmgBox = new Box(this, -16, -16, 32, 32);
        if (isFriendly()) {
            for (Fighter e : getLand().getEnemies())
                if (dmgBox.intersect(e.getHitbox()))
                    dealDamageTo(e);
            for (Destructible d : getLand().getDestructibles())
                if (dmgBox.intersect(d.getWalkbox()))
                    dealDamageTo(d);
        } else {
            Player p = getLand().getPlayer();
            if (dmgBox.intersect(p.getHitbox())) {
                canSplit = false;
                dealDamageTo(p);
            }
        }
    }

    private void dealDamageTo(Dommageable entity) throws PlayerDeadException {
        entity.takeDamage(getAtk(), getElement());
        boolean needDie = hit(entity);
        if (needDie)
            die();
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAtk() {
        return atk;
    }

    public boolean isFriendly() {
        return friendly;
    }

    public void endedRange() {
    }

    public abstract boolean hit(Dommageable entity);

    @Override
    public String toString() {
        return String.format("%s(id=%s, x=%d, y=%d, range=%d)", getClass().getSimpleName(), getId(), getX(), getY(), getHp());
    }

}
