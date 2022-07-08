package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.Images;
import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.ModelConstants;
import fr.dashstrom.model.assignable.weapon.*;
import fr.dashstrom.model.entity.dommageable.fighter.Player;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import static fr.dashstrom.controller.ControllerConstants.FPS;
import static fr.dashstrom.model.ModelConstants.*;

public class PlayerSprite extends SpriteDommageable<Player> {

    public PlayerSprite(ZPane pane, Player player) {
        super(pane, player, -29, -48, Images.PLAYER.get());

        addAnimLoop(S, FPS / 20, 0, 0, 0, 1);
        addAnimLoop(E, FPS / 20, 7, 7, 7, 8);
        addAnimLoop(W, FPS / 20, 39, 39, 39, 40);
        addAnimLoop(N, FPS / 20, 46, 46, 47, 14, 14, 15);
        addAnimLoop(S + MOVING, FPS / 20, 2, 4, 3, 4, 2, 6, 5, 6);
        addAnimLoop(E + MOVING, FPS / 20, 9, 10, 10, 9, 12, 12);
        addAnimLoop(W + MOVING, FPS / 20, 41, 42, 42, 41, 44, 44);
        addAnimLoop(N + MOVING, FPS / 20, 14, 49, 16, 15, 46, 17, 48, 47);

        player.cooldownProperty().addListener((p, o, n) -> {
            if (o.intValue() < n.intValue())
                attack();
        });
        changeOrientation();
    }

    public void slash() {
        Player p = getEntity();
        ImageView view = new ImageView();

        view.setImage(Images.SLASH.get());
        view.translateXProperty().bind(p.xProperty().add(p.faceXProperty().multiply(20)).subtract(view.getImage().getWidth() / 2));
        view.translateYProperty().bind(p.yProperty().add(p.faceYProperty().multiply(20)).subtract(view.getImage().getHeight() / 2 - ModelConstants.PLAYER_HEAD));
        view.setRotate((p.orientation() * 45) - 180);
        getPane().add(view);

        FadeTransition ft = new FadeTransition(Duration.millis(300), view);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setByValue(0.1);
        ft.setOnFinished(e -> getPane().remove(view));
        ft.play();
    }

    public void simpleWeapon() {
        Player p = getEntity();
        ImageView view = new ImageView();
        if (p.getWeapon() instanceof Axe) {
            view.setImage(Images.AXE.get());
        } else if (p.getWeapon() instanceof Staff) {
            if (p.getWeapon() instanceof FireStaff) {
                view.setImage(Images.FIRE_STAFF.get());
            } else if (p.getWeapon() instanceof IceStaff) {
                view.setImage(Images.ICE_STAFF.get());
            }
        }
        anim(view, p);
    }

    private void doubleWeapon() {
        Player p = getEntity();
        ImageView view = new ImageView();

        CompositeWeapon weapon = (CompositeWeapon) p.getWeapon();
        if (weapon instanceof MasterAxe) {
            view.setImage(Images.MASTER_AXE.get());
        } else if (weapon instanceof FireAxe) {
            view.setImage(Images.FIRE_AXE.get());
        } else view.setImage(Images.AXE.get());
        anim(view, p);
    }

    public void anim(ImageView view, Player p) {
        int dir = 1;
        view.translateXProperty().bind(p.xProperty().add(p.faceXProperty().multiply(20)).subtract(view.getImage().getWidth() / 2));
        view.translateYProperty().bind(p.yProperty().add(p.faceYProperty().multiply(20)).subtract(view.getImage().getHeight() / 2 - ModelConstants.PLAYER_HEAD));
        view.setRotate((p.orientation() - 1) * 45);

        if (p.orientation() - 1 >= 4) {
            dir = -1;
            view.setScaleX(dir);
        }

        getPane().add(view);

        RotateTransition rt = new RotateTransition(Duration.millis(500), view);
        rt.setByAngle(45 * dir);
        rt.setToAngle(view.getRotate() + 90 * dir);
        rt.setFromAngle(view.getRotate() + 90 * -dir);
        rt.setOnFinished(e -> getPane().getChildren().remove(view));
        rt.play();
        FadeTransition ft = new FadeTransition(Duration.millis(500), view);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setByValue(0.1);
        ft.setOnFinished(e -> getPane().getChildren().remove(view));
        ft.play();
    }

    public void attack() {
        Weapon w = getEntity().getWeapon();
        slash();
        if (w instanceof CompositeWeapon) {
            doubleWeapon();
        } else if (w != null) {
            simpleWeapon();
        }
    }

}