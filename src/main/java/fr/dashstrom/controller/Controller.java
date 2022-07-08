package fr.dashstrom.controller;

import fr.dashstrom.model.Game;
import fr.dashstrom.model.LoadError;
import fr.dashstrom.model.entity.dommageable.fighter.Player;
import fr.dashstrom.model.entity.dommageable.fighter.PlayerDeadException;
import fr.dashstrom.model.movement.Move;
import fr.dashstrom.model.movement.StraightMove;
import fr.dashstrom.utils.ResourceUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static fr.dashstrom.model.ModelConstants.PLAYER_DEFAULT_HP;

public class Controller implements Initializable {

    @FXML
    private BorderPane borderPane;
    private ZPane pane;
    private Timeline gameLoop;
    private Game game;
    private KeyController keys;
    private boolean hasOpenInventory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    public void init() {
        borderPane.getChildren().clear();
        game = new Game();
        pane = new ZPane(game);
        keys = new KeyController(borderPane);
        borderPane.setCenter(pane);
        BorderPane.setAlignment(pane, Pos.CENTER);

        initKeys();
        initDialogs();
        initHUD();
        initGameLoop();
    }

    private void initKeys() {
        Player p = game.getPlayer();
        keys.listen(ControllerConstants.UP, b -> onMove());
        keys.listen(ControllerConstants.LEFT, b -> onMove());
        keys.listen(ControllerConstants.DOWN, b -> onMove());
        keys.listen(ControllerConstants.RIGHT, b -> onMove());
        keys.listen(ControllerConstants.INTERACT, p::setInteracting);
        keys.listenPressed(ControllerConstants.INVENTORY, this::onInventory);
        keys.listenPressed(ControllerConstants.EXIT_GAME, this::onInventory);
        keys.listenPressed(ControllerConstants.ATTACK1, () -> p.takeWeapon(0));
        keys.listenPressed(ControllerConstants.ATTACK2, () -> p.takeWeapon(1));
        keys.listenPressed(ControllerConstants.ATTACK3, () -> p.takeWeapon(2));
    }

    public void onMove() {
        if (hasOpenInventory || game == null || keys == null)
            return;
        Player p = game.getPlayer();
        Move move = p.getMove();
        if (move instanceof StraightMove) {
            boolean s = keys.isPressed(ControllerConstants.DOWN), n = keys.isPressed(ControllerConstants.UP), e = keys.isPressed(ControllerConstants.LEFT), w = keys.isPressed(ControllerConstants.RIGHT);
            int dx = w ? (e ? 0 : 1) : (e ? -1 : 0), dy = s ? (n ? 0 : 1) : (n ? -1 : 0);
            ((StraightMove) move).direction(dx, dy);
        }
    }

    public void onInventory() {
        stopGameLoop();
        keys.cleanKeys();
        if (hasOpenInventory) {
            pane.getChildren().removeIf(n -> "inventory".equals(n.getId()));
            startGameLoop();
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(ResourceUtils.getResourceURL("/view/inventory.fxml"));
                Parent inventory = fxmlLoader.load();
                InventoryController controller = fxmlLoader.getController();
                controller.setParent(this);
                pane.add(inventory);
            } catch (IOException e) {
                startGameLoop();
                throw new LoadError("Error during loading inventory.fxml", e);
            }
        }
        hasOpenInventory = !hasOpenInventory;
    }

    public void onDeath() {
        stopGameLoop();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ResourceUtils.getResourceURL("/view/gameover.fxml"));
            Musics.stop();
            game = null;
            pane = null;
            keys = null;
            borderPane.getChildren().removeAll();
            Parent gameover = fxmlLoader.load();
            GameOverController controller = fxmlLoader.getController();
            controller.setParent(this);
            borderPane.setCenter(gameover);
        } catch (IOException e) {
            startGameLoop();
            throw new LoadError("Heroes never die", e);
        }
    }

    public Game getGame() {
        return game;
    }

    /**
     * initializes the life bar in the HUD.
     */
    private void initHUD() {
        //Initializes the life bar of the character
        int x = 0, y = 4;
        Player p = game.getPlayer();
        //Adds the correct amount of hearts depending on the total amount of player's pv
        for (int count = 1; count <= (PLAYER_DEFAULT_HP / 2); count++) {
            ImageView view = new ImageView();
            view.setImage(Images.HEART.get());
            view.setX(x + 4);
            view.setY(y);
            view.setId("H" + count);
            borderPane.getChildren().add(view);
            x += 33;
        }
        p.hpProperty().addListener((observable, oldValue, newValue) -> {
            if ((int) newValue > (int) oldValue)
                updateLifeHUD(1);
            else
                updateLifeHUD(0);
        });
        p.maxHPProperty().addListener((observable, oldValue, newValue) -> updateLifeHUD(2));
    }

    /**
     * Removes the correct amount of hearts depending on the life of the player.
     * mode = 0 ==> hitting or killing (removes life)
     * mode = 1 ==> healing (adds life)
     * mode = 2 ==> adding hearts (adds heart(s))
     */
    private void updateLifeHUD(int mode) {
        Player p = game.getPlayer();

        //Removes life
        if (mode == 0 && p.getHp() >= 0) {
            //Get the number of hearts to display from the player'pv
            double totalHearts = ((double) p.getMaxHP() / 2 - (double) p.getHp() / 2);
            int nbFullHeartsToRm = (int) Math.floor(totalHearts);
            double nbHalfHeartsToRm = totalHearts - nbFullHeartsToRm;

            //If the hit received kills the player
            if (p.getMaxHP() - totalHearts <= 0 || p.getHp() < 0) {
                for (int count = 0; count < p.getMaxHP(); count++) {
                    ImageView view = (ImageView) borderPane.lookup("#H" + (p.getMaxHP() / 2 - count));
                    view.setImage(Images.EMPTY_HEART.get());
                }
            }
            //Other hits
            else {
                if (nbFullHeartsToRm != 0)
                    for (int count = 0; count < nbFullHeartsToRm; count++) {
                        ImageView view = (ImageView) borderPane.lookup("#H" + (p.getMaxHP() / 2 - count));
                        view.setImage(Images.EMPTY_HEART.get());
                    }
                if (nbHalfHeartsToRm == 0.5) {
                    ImageView view = (ImageView) borderPane.lookup("#H" + (p.getMaxHP() / 2 - nbFullHeartsToRm));
                    view.setImage(Images.HALF_HEART.get());
                }
            }
        }

        //Adds life
        else if (mode == 1 && p.isAlive()) {
            //Get the number of hearts to display (from the player'hp)
            int totalHearts = p.getHp() / 2;
            int nbFullHeartsToAdd;
            int nbHalfHeartsToAdd = 0;

            // Checks if there is a half-heart to prompt
            if (p.getHp() % 2 == 0)
                nbFullHeartsToAdd = totalHearts;
            else {
                nbFullHeartsToAdd = totalHearts;
                nbHalfHeartsToAdd = 1;
            }

            //Adding life in the lifebar (full hearts)
            int count;
            for (count = 1; count <= p.getMaxHP() / 2; count++) {
                if (count <= nbFullHeartsToAdd) {
                    ImageView view = (ImageView) borderPane.lookup("#H" + count);
                    view.setImage(Images.HEART.get());
                } else if (count <= (nbFullHeartsToAdd + nbHalfHeartsToAdd)) {
                    ImageView view = (ImageView) borderPane.lookup("#H" + count);
                    view.setImage(Images.HALF_HEART.get());
                } else {
                    ImageView view = (ImageView) borderPane.lookup("#H" + count);
                    if (view != null)
                        view.setImage(Images.EMPTY_HEART.get());
                }

            }
        }

        // Adds heart(s)
        else if (mode == 2 && p.isAlive()) {
            // Get the amount of hearts added to the normal lifebar
            int nbHearts = (PLAYER_DEFAULT_HP + (p.getMaxHP() - PLAYER_DEFAULT_HP)) / 2;

            if (nbHearts > PLAYER_DEFAULT_HP / 2) {
                // Get the position of the last heart on the screen
                ImageView lastHeart = (ImageView) borderPane.lookup("#H" + (p.getMaxHP() / 2 - 1));
                int x = (int) lastHeart.getX();
                x += 33;

                //Sets the new heart on the screen
                ImageView view = new ImageView();

                //If the player if not full life
                if (p.getHp() < (p.getMaxHP() - 2))
                    view.setImage(Images.EMPTY_HEART.get());
                else
                    view.setImage(Images.HEART.get());

                view.setX(x);
                view.setY(4);
                view.setId("H" + (p.getMaxHP() / 2));
                borderPane.getChildren().add(view);
            }
            updateLifeHUD(1);
        }

    }

    /**
     * Initializes the dialog window dedicated to the discussion with
     * NPCs. Creates and hides the dialog label.
     */
    private void initDialogs() {
        Label dialogWindow = new Label();
        dialogWindow.setLayoutX(300);
        dialogWindow.setLayoutY(600);
        dialogWindow.setId("D1");
        dialogWindow.setVisible(false);
        //Listens for String dialog changes
        game.saysProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue.equals("")) {
                //Center the dialog above the character speaking
                if (newValue.length() <= 25)
                    dialogWindow.setLayoutX(game.getPlayer().getCurrentInteractable().getX() - (newValue.length() * 3));
                else
                    dialogWindow.setLayoutX(game.getPlayer().getCurrentInteractable().getX() - (newValue.length() * 3 + 20));
                dialogWindow.setLayoutY(game.getPlayer().getCurrentInteractable().getY() - 65);
                dialogWindow.setVisible(true);
            }

            if (newValue.equals(""))
                dialogWindow.setVisible(false);

            dialogWindow.setText(newValue);
        }
        ));
        pane.add(dialogWindow);
    }

    /**
     * Initializes the gameloop using a TimeLine
     **/
    private void initGameLoop() {

        gameLoop = new Timeline(new KeyFrame(Duration.seconds(1 / ControllerConstants.FPS), event -> {
            try {
                game.aTurn();
            } catch (PlayerDeadException e) {
                onDeath();
            }
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        startGameLoop();
    }

    public void stopGameLoop() {
        gameLoop.stop();
    }

    public void startGameLoop() {
        gameLoop.play();
    }

}


