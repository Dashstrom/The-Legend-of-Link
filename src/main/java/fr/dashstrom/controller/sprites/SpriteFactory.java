package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.ControllerConstants;
import fr.dashstrom.controller.Images;
import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.assignable.Assignable;
import fr.dashstrom.model.assignable.consumable.Booster;
import fr.dashstrom.model.assignable.consumable.Potion;
import fr.dashstrom.model.entity.Entity;
import fr.dashstrom.model.entity.Pickable;
import fr.dashstrom.model.entity.dommageable.destructible.Destructible;
import fr.dashstrom.model.entity.dommageable.destructible.Fire;
import fr.dashstrom.model.entity.dommageable.destructible.Hay;
import fr.dashstrom.model.entity.dommageable.destructible.Ice;
import fr.dashstrom.model.entity.dommageable.fighter.Fighter;
import fr.dashstrom.model.entity.dommageable.fighter.enemy.BlueDragon;
import fr.dashstrom.model.entity.dommageable.fighter.enemy.Bug;
import fr.dashstrom.model.entity.dommageable.fighter.enemy.Hydra;
import fr.dashstrom.model.entity.dommageable.fighter.enemy.RedDragon;
import fr.dashstrom.model.entity.dommageable.projectile.Fireball;
import fr.dashstrom.model.entity.dommageable.projectile.Iceball;
import fr.dashstrom.model.entity.dommageable.projectile.Projectile;
import fr.dashstrom.model.entity.dommageable.projectile.Slash;
import fr.dashstrom.model.entity.interactable.Crate;
import fr.dashstrom.model.entity.interactable.Interactable;
import fr.dashstrom.model.entity.interactable.Npc;
import javafx.scene.image.Image;

public class SpriteFactory {

    public static Image getImageAssignable(Assignable assignable) {
        Image assignImage;
        if (assignable instanceof Potion) {
            int power = ((Potion) assignable).getHeal();
            int size = Integer.compare(power, ControllerConstants.MEDIUM_SIZE) + 1;
            assignImage = Images.POTIONS_RED.get()[size];
        } else if (assignable instanceof Booster) {
            assignImage = Images.POTIONS_BLUE.get()[0];
        } else {
            assignImage = Images.ASSIGNABLES.get(assignable.getName()).get();
        }
        return assignImage == null ? Images.ERROR.get() : assignImage;
    }

    public static Sprite<Pickable> makePickable(ZPane pane, Pickable pickable) {
        Image frame = getImageAssignable(pickable.getAssignable());
        return new Sprite<>(pane, pickable, -(int) frame.getWidth() / 2, -(int) frame.getHeight() / 2, frame);
    }

    public static Sprite<? extends Fighter> makeFighter(ZPane pane, Fighter fighter) {
        if (fighter instanceof Bug)
            return new BugSprite(pane, (Bug) fighter);
        else if (fighter instanceof RedDragon)
            return new RedDragonSprite(pane, (RedDragon) fighter);
        else if (fighter instanceof BlueDragon)
            return new BlueDragonSprite(pane, (BlueDragon) fighter);
        else if (fighter instanceof Hydra)
            return new HydraSprite(pane, (Hydra) fighter);
        return null;
    }

    public static Sprite<? extends Interactable> makeInteractable(ZPane pane, Interactable interactable) {
        if (interactable instanceof Crate)
            return new CrateSprite(pane, (Crate) interactable);
        else if (interactable instanceof Npc && interactable.getId().equals("link"))
            return new LinkSprite(pane, (Npc) interactable);
        else if (interactable instanceof Npc)
            return new NpcSprite(pane, (Npc) interactable);

        return null;
    }

    public static Sprite<? extends Destructible> makeDestructible(ZPane pane, Destructible destructible) {
        if (destructible instanceof Hay)
            return new HaySprite(pane, (Hay) destructible);
        if (destructible instanceof Fire)
            return new FireSprite(pane, (Fire) destructible);
        if (destructible instanceof Ice)
            return new IceSprite(pane, (Ice) destructible);
        return null;
    }

    public static Sprite<? extends Projectile> makeProjectile(ZPane pane, Projectile projectile) {
        if (projectile instanceof Fireball)
            return new FireballSprite(pane, (Fireball) projectile);
        else if (projectile instanceof Iceball)
            return new IceballSprite(pane, (Iceball) projectile);
        else if (projectile instanceof Slash)
            return new SlashSprite(pane, (Slash) projectile);
        return null;
    }

    public interface SpriteMaker<E extends Entity> {

        Sprite<?> make(ZPane pane, E entity);

    }

}
