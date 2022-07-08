package fr.dashstrom.model.entity.dommageable.fighter.enemy;

import fr.dashstrom.controller.ControllerConstants;
import fr.dashstrom.model.assignable.weapon.FireAxe;
import fr.dashstrom.model.entity.dommageable.projectile.Fireball;
import fr.dashstrom.model.entity.dommageable.projectile.Iceball;
import fr.dashstrom.model.entity.dommageable.projectile.Projectile;
import fr.dashstrom.model.entity.interactable.Npc;
import fr.dashstrom.model.movement.FlyTo;
import fr.dashstrom.model.Dialogs;
import fr.dashstrom.model.Element;
import fr.dashstrom.model.Land;
import fr.dashstrom.model.entity.Pickable;
import fr.dashstrom.model.entity.dommageable.Dommageable;
import fr.dashstrom.model.entity.dommageable.fighter.Fighter;
import fr.dashstrom.model.entity.dommageable.fighter.PlayerDeadException;

import java.util.ArrayList;

import static fr.dashstrom.model.ModelConstants.PRIORITY_DEPLACEMENTS;

public class Hydra extends Fighter {

    private final ArrayList<Fighter> minions;
    private boolean isHealing;
    private boolean isCentered;
    private boolean isProtected;
    private int healPhases;
    private int position;

    public Hydra(Land land, int x, int y, int pv) {
        super(land, x, y, pv, 0, 0, 16, 16, Element.NORMAL);
        minions = new ArrayList<>();
        isHealing = false;
        isCentered = false;
        isProtected = false;
        healPhases = 3;
        position = 0;
    }

    @Override
    public void duringAttackAvaibable() {

        //Attacks if is not healing
        if (!isHealing) {
            attackProjectiles();
            if (position >= 6)
                position = 0;
            else
                position++;
            setCooldown((int) (60 + (Math.random() * (300 - 60))));
        }

        //Or regains life
        else {
            heal(3);
            setCooldown(60);
            // If the hydra is centered
            if (isCentered && !isProtected) {
                createMinions();
                isProtected = true;
            }

            //Updates the list if a minion dies
            if (!minions.isEmpty())
                minions.removeIf(Dommageable::isDead);

            //If the hydra has regained full life we stop healing
            if (getHp() >= getMaxHP() || (minions.isEmpty() && isCentered)) {
                isHealing = false;
                isCentered = false;
                isProtected = false;
                healPhases--;
            }
        }
    }

    private void attackProjectiles() {
        Projectile ball;
        for (int count = 0; count < 8; count++) {
            if ((Math.random() * 100) >= 50) {
                ball = new Fireball(getLand(), getX(), getY(),
                    PRIORITY_DEPLACEMENTS[count][0], PRIORITY_DEPLACEMENTS[count][1], 4,
                    300, 1, false, true);
            } else {
                ball = new Iceball(getLand(), getX(), getY(),
                    PRIORITY_DEPLACEMENTS[count][0], PRIORITY_DEPLACEMENTS[count][1], 4,
                    300, 1, false, true);
            }
            getLand().getProjectiles().add(ball);
        }
    }

    private void createMinions() {
        minions.add(new RedDragon(getLand(), getX() + 250, getY() + 250, 20, 1));
        minions.add(new BlueDragon(getLand(), getX() - 250, getY() - 250, 20, 1));

        for (Fighter f : minions)
            getLand().getEnemies().add(f);
    }

    @Override
    public void takeDamage(int damage, Element element) throws PlayerDeadException {
        // The hydra is not healing : normal behaviour
        if (!isHealing)
            super.takeDamage(damage, element);
        //No damage in the other case
    }

    @Override
    public void die() throws PlayerDeadException {
        getLand().getPickables().add(new Pickable(getLand(), getX(), getY(), new FireAxe(getLand().getPlayer())));
        getLand().getInteractables().add(new Npc(getLand(), 800, 200, Dialogs.LINK.get(), "link"));
        super.die();
    }

    @Override
    public void act() throws PlayerDeadException {

        //Heals if its HP get below 50 and if it not already getting healed
        if (getHp() < getMaxHP() / 2 && !isHealing && healPhases != 0) {
            isHealing = true;
        }

        // The Hydra is not regenerating
        if (!isHealing)
            setMove(new FlyTo(this, 2, false, "point", 300, position));

        else if (!isCentered) {
            // If the Hydra is not centered
            if (getX() < ControllerConstants.WIDTH / 2 - 10 || ControllerConstants.WIDTH / 2 < getX() || getY() < ControllerConstants.HEIGHT / 2 - 8 || ControllerConstants.HEIGHT / 2 + 2 < getY()) {
                setMove(new FlyTo(this, 3, false, "center", 0, position));
            } else
                isCentered = true;
        } else
            setMove(null);

        super.act();
    }

}
