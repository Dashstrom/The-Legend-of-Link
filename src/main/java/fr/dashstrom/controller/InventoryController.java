package fr.dashstrom.controller;

import fr.dashstrom.controller.sprites.SpriteFactory;
import fr.dashstrom.model.Game;
import fr.dashstrom.model.assignable.Assignable;
import fr.dashstrom.model.assignable.AssignableException;
import fr.dashstrom.model.assignable.consumable.Consumable;
import fr.dashstrom.model.assignable.weapon.Weapon;
import fr.dashstrom.model.entity.dommageable.fighter.Player;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {

    private Controller ctrl;

    @FXML
    private BorderPane inventory;

    @FXML
    private Button actionButton;

    @FXML
    private TableView<Consumable> consumables;

    @FXML
    private TableColumn<Consumable, ImageView> consumablesVisual;

    @FXML
    private TableColumn<Consumable, String> consumablesName;

    @FXML
    private TableColumn<Consumable, String> consumablesDescription;

    @FXML
    private TableView<Weapon> weapons;

    @FXML
    private TableColumn<Weapon, ImageView> weaponsVisual;

    @FXML
    private TableColumn<Weapon, String> weaponsName;

    @FXML
    private TableColumn<Weapon, Integer> weaponsDamage;

    @FXML
    private TableColumn<Weapon, Integer> weaponsCooldown;

    @FXML
    private TableView<Weapon> equipped;

    @FXML
    private TableColumn<Weapon, ImageView> equippedVisual;

    @FXML
    private TableColumn<Weapon, String> equippedName;

    @FXML
    private TableColumn<Weapon, Integer> equippedDamage;

    @FXML
    private TableColumn<Weapon, Integer> equippedCooldown;

    @FXML
    private TableView<?> selected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ctrl = null;
    }

    public void clearTables(TableView<?> table) {
        if (table != consumables) {
            consumables.getSelectionModel().clearSelection();
        }
        if (table != weapons) {
            weapons.getSelectionModel().clearSelection();
        }
        if (table != equipped) {
            equipped.getSelectionModel().clearSelection();
        }
        selected = table;
    }

    public void setParent(Controller ctrl) {
        this.ctrl = ctrl;
        Game game = this.ctrl.getGame();
        Player p = game.getPlayer();

        consumables.setPlaceholder(new Label(""));
        consumablesVisual.setCellValueFactory(extractImageViewProperty());
        prop(consumablesName, "name");
        prop(consumablesDescription, "description");
        consumables.setItems(p.getConsumables());

        weapons.setPlaceholder(new Label(""));
        weaponsVisual.setCellValueFactory(extractImageViewProperty());
        prop(weaponsName, "name");
        prop(weaponsDamage, "atk");
        prop(weaponsCooldown, "cooldown");
        weapons.setItems(p.getWeapons());

        equipped.setPlaceholder(new Label(""));
        equippedVisual.setCellValueFactory(extractImageViewProperty());
        prop(equippedName, "name");
        prop(equippedDamage, "atk");
        prop(equippedCooldown, "cooldown");
        prop(equippedCooldown, "cooldown");
        equipped.setItems(p.getEquipped());

        setActionHandler(equipped, "Déséquipper");
        setActionHandler(weapons, "Équiper");
        setActionHandler(consumables, "Utiliser");
    }

    private void setActionHandler(TableView<?> table, String actionName) {
        table.getSelectionModel().selectedIndexProperty().addListener((i, o, n) -> {
            if (n.intValue() != -1) {
                clearTables(table);
                actionButton.setText(actionName);
            }
        });
    }

    private void prop(TableColumn<?, ?> col, String name) {
        col.setCellValueFactory(new PropertyValueFactory<>(name));
    }

    private <A extends Assignable> Callback<TableColumn.CellDataFeatures<A, ImageView>, ObservableValue<ImageView>>
    extractImageViewProperty() {
        return (cellDataFeatures) -> {
            Assignable assignable = cellDataFeatures.getValue();
            Image weapon = SpriteFactory.getImageAssignable(assignable);
            ImageView iv = new ImageView(weapon);
            iv.setPreserveRatio(true);
            int size = Math.min(35, Math.min((int) weapon.getHeight(), (int) weapon.getWidth()));
            iv.setFitWidth(size);
            iv.setFitHeight(size);
            iv.setTranslateX((35 - size) / 2.0);
            iv.setTranslateY((35 - size) / 2.0);
            return new SimpleObjectProperty<>(iv);
        };
    }

    @FXML
    public void onAction() {
        int i = selected.getSelectionModel().getSelectedIndex();
        Player p = ctrl.getGame().getPlayer();
        try {
            if (equipped == selected)
                p.unequip(i);
            else if (weapons == selected)
                p.equip(i);
            else if (consumables == selected) {
                p.use(i);
            }
        } catch (AssignableException e) {
            actionErrorMessage(e.getMessage());
        }
    }

    private void actionErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("The Legend of Link");
        alert.setContentText(message);
        alert.setHeaderText(Math.random() > 0.1 ? "Action annulée" : "C'était pour les 4 points");

        DialogPane dialog = alert.getDialogPane();
        dialog.getStylesheets().add("/view/style/stylesheet.css");
        dialog.getStyleClass().add("assign-error");

        Stage stage = (Stage) dialog.getScene().getWindow();
        stage.setTitle("The Legend of Link");
        stage.getIcons().add(Images.ICON.get());

        alert.showAndWait();
    }

    @FXML
    public void onContinue() {
        ctrl.onInventory();
    }

    @FXML
    public void onExit() {
        System.exit(0);
    }

}
