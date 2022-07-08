package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.Images;
import fr.dashstrom.controller.UtilsController;
import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.entity.dommageable.fighter.enemy.Hydra;
import javafx.animation.FadeTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class HydraSprite extends SpriteDragonLike<Hydra> {

    public HydraSprite(ZPane pane, Hydra enemy) {
        super(pane, enemy, -UtilsController.midleX(Images.HYDRA.get()), -UtilsController.midleY(Images.HYDRA.get()), Images.HYDRA.get());
        changeOrientation();
        loadLightShield(enemy);
    }

    // Prompts the light shield when the HP are rising
    private void loadLightShield(Hydra enemy) {
        enemy.hpProperty().addListener((observable, oldValue, newValue) -> {
            if ((int) newValue > (int) oldValue) {
                ImageView view = new ImageView();
                view.setImage(Images.LIGHT_SHIELD.get());
                view.setX(enemy.getX() - 130);
                view.setY(enemy.getY() - 140);
                getPane().add(view);

                FadeTransition ft = new FadeTransition(Duration.millis(1000), view);
                ft.setFromValue(1);
                ft.setToValue(0);
                ft.setByValue(0.1);
                ft.setOnFinished(e -> getPane().remove(view));
                ft.play();
            }
        });
    }

}