package fr.dashstrom.model.assignable.consumable;

import fr.dashstrom.model.assignable.Assignable;
import fr.dashstrom.model.assignable.AssignableException;
import fr.dashstrom.model.entity.dommageable.fighter.Player;

public abstract class Consumable extends Assignable {

    private final String name, description;

    public Consumable(Player p, String name, String description) {
        super(p);
        this.name = name;
        this.description = description;
    }

    // used in tableView
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void assign() {
        getPlayer().getConsumables().add(this);
    }

    protected abstract void utilization() throws AssignableException;

    public void use() throws AssignableException {
        if (getPlayer() != null) {
            utilization();
            getPlayer().getConsumables().remove(this);
        } else {
            throw new AssignableException(getName() + " n'est pas à vous !");
        }
    }

}
