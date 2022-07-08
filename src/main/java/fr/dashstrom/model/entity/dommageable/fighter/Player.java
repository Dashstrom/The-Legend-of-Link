package fr.dashstrom.model.entity.dommageable.fighter;

import fr.dashstrom.model.assignable.AssignableException;
import fr.dashstrom.model.assignable.consumable.Booster;
import fr.dashstrom.model.assignable.consumable.Consumable;
import fr.dashstrom.model.assignable.consumable.Potion;
import fr.dashstrom.model.assignable.weapon.*;
import fr.dashstrom.model.entity.interactable.Interactable;
import fr.dashstrom.model.movement.Move;
import fr.dashstrom.model.movement.StraightMove;
import fr.dashstrom.utils.ResourceUtils;
import fr.dashstrom.model.Element;
import fr.dashstrom.model.Land;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static fr.dashstrom.model.ModelConstants.PLAYER_DEFAULT_HP;
import static fr.dashstrom.model.ModelConstants.TOO_FAR;

/**
 * main.game.Main Characters of the Game
 **/
public class Player extends Fighter {

    private final ObservableList<Weapon> equipped;
    private final ObservableList<Weapon> weapons;
    private final ObservableList<Consumable> consumables;
    private int indexWeapon;
    private Interactable currentInteractable;

    // Constructeur

    /**
     * The constructor of Zelda
     *
     * @param land Land
     * @param x    X position
     * @param y    Y position
     **/
    public Player(Land land, int x, int y) {
        super(land, x, y, PLAYER_DEFAULT_HP, -12, -8, 24, 24, Element.NORMAL);
        setWalkbox(16, 16);

        equipped = FXCollections.observableArrayList();
        weapons = FXCollections.observableArrayList();
        consumables = FXCollections.observableArrayList();

        new Potion(this, 4).assign();
        new Potion(this, 4).assign();
        new Potion(this, 4).assign();

        if (ResourceUtils.DEBUG) {
            new FireStaff(this).assign();
            new MasterAxe(this).assign();
            new IceStaff(this).assign();
            new FireAxe(this).assign();
            new Booster(this).assign();
            new Potion(this, 10).assign();
            new Potion(this, 10).assign();
            new Potion(this, 10).assign();
        }

        new Axe(this).assign();

        setMove(new StraightMove(this, 4, true));
    }

    public void setInteracting(boolean interacting) {
        if (currentInteractable != null)
            currentInteractable.endInteracting();
        currentInteractable = null;
        if (interacting) {
            currentInteractable = getNearestInteractable();
            if (currentInteractable != null)
                currentInteractable.startInteracting();
        }
    }

    public void equip(int i) throws AssignableException {
        if (equipped.size() >= 3)
            throw new AssignableException("Vous ne pouvez pas equipée plus de 3 armes");
        else if (i >= weapons.size())
            throw new AssignableException("Le consomable à l'emplacement " + i + " n'a pas pu être utilisé");
        else
            equipped.add(weapons.remove(i));
    }

    public void unequip(int i) throws AssignableException {
        if (equipped.size() <= 1)
            throw new AssignableException("Vous devez avoir au moins un arme équipée");
        else if (i >= equipped.size())
            throw new AssignableException("Le consomable à l'emplacement " + i + " n'a pas pu être utilisé");
        else
            weapons.add(equipped.remove(i));
    }

    public void use(int i) throws AssignableException {
        if (0 <= i && i < consumables.size())
            consumables.get(i).use();
        else
            throw new AssignableException("Le consomable à l'emplacement " + i + " n'a pas pu être utilisé");
    }

    public void pickIpWeapon(Weapon weapon) {
        if (equipped.size() < 3)
            equipped.add(weapon);
        else
            weapons.add(weapon);
    }

    public ObservableList<Weapon> getEquipped() {
        return equipped;
    }

    @Override
    public void setMove(Move movement) {
        super.setMove(movement);
    }

    /**
     * Move the player these directions
     **/
    @Override
    public void act() throws PlayerDeadException {
        if (isDead())
            throw new PlayerDeadException("Vous êtes mort !");
        super.act();
        if (beforeEndCooldown())
            indexWeapon = -1;
    }

    @Override
    public void die() throws PlayerDeadException {
        if (this.isDead())
            throw new PlayerDeadException("Vous êtes mort !");
        super.die();
    }

    // INTERACTABLE PART

    /**
     * Tests if there is an interactble near the player. If there is one,
     * it is returned. Otherwise, null is returned
     *
     * @return nearest interactable
     */
    public Interactable getNearestInteractable() {
        Interactable nearest = null;
        int min = TOO_FAR;
        for (Interactable interactable : getLand().getInteractables()) {
            int distance = interactable.distance();
            if (distance < min) {
                min = distance;
                nearest = interactable;
            }
        }
        return min < 48 ? nearest : null;
    }

    public void takeWeapon(int indexWeapon) {
        this.indexWeapon = indexWeapon;
    }

    @Override
    public void duringAttackAvaibable() throws PlayerDeadException {
        Weapon weapon = getWeapon();
        if (weapon != null) {
            weapon.attack();
            setCooldown(weapon.getCooldown());
        }
    }

    public Weapon getWeapon() {
        try {
            return equipped.get(indexWeapon);
        } catch (IndexOutOfBoundsException err) {
            return null;
        }
    }

    public ObservableList<Weapon> getWeapons() {
        return weapons;
    }

    public ObservableList<Consumable> getConsumables() {
        return consumables;
    }

    //GETTER

    public Interactable getCurrentInteractable() {
        return currentInteractable;
    }

}
