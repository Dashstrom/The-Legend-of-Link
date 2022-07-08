package fr.dashstrom.model.assignable;

import fr.dashstrom.model.entity.dommageable.fighter.Player;

public class Heart extends Assignable {

    public Heart(Player player) {
        super(player);
    }

    @Override
    public void assign() {
        getPlayer().addMaxHP(2);
    }

    @Override
    public String getName() {
        return "Coeur";
    }

}
