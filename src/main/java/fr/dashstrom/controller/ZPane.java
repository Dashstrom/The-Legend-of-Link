package fr.dashstrom.controller;

import fr.dashstrom.controller.sprites.PlayerSprite;
import fr.dashstrom.controller.sprites.Sprite;
import fr.dashstrom.controller.sprites.SpriteFactory;
import fr.dashstrom.model.Game;
import fr.dashstrom.model.Land;
import fr.dashstrom.model.Tile;
import fr.dashstrom.model.entity.Entity;
import fr.dashstrom.model.entity.Pickable;
import fr.dashstrom.model.entity.dommageable.destructible.Destructible;
import fr.dashstrom.model.entity.dommageable.fighter.Fighter;
import fr.dashstrom.model.entity.dommageable.projectile.Projectile;
import fr.dashstrom.model.entity.interactable.Interactable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.HashMap;

import static fr.dashstrom.model.ModelConstants.TILE_SIZE;

public class ZPane extends Pane {

    private final Group ground;
    private final Group[] slices;
    private final HashMap<String, ChangeListener<Number>> listenersSprites;
    private final EntityListener<Projectile> projectilesListener;
    private final EntityListener<Fighter> fightersListener;
    private final EntityListener<Interactable> interactablesListener;
    private final EntityListener<Pickable> pickablesListener;
    private final EntityListener<Destructible> destructibleListener;

    public ZPane(Game game) {
        setFocusTraversable(true);
        ground = new Group();
        getChildren().add(ground);
        slices = new Group[ControllerConstants.HEIGHT + ControllerConstants.DEPTH * TILE_SIZE * 2];
        for (int i = 0; i < slices.length; i++)
            slices[i] = new Group();
        getChildren().addAll(slices);
        this.listenersSprites = new HashMap<>();

        projectilesListener = new EntityListener<>(this, SpriteFactory::makeProjectile);
        fightersListener = new EntityListener<>(this, SpriteFactory::makeFighter);
        interactablesListener = new EntityListener<>(this, SpriteFactory::makeInteractable);
        pickablesListener = new EntityListener<>(this, SpriteFactory::makePickable);
        destructibleListener = new EntityListener<>(this, SpriteFactory::makeDestructible);

        game.landProperty().addListener(this::changeLand);
        changeLand(null, null, game.getLand());
        setPrefHeight(ControllerConstants.HEIGHT);
        setPrefWidth(ControllerConstants.WIDTH);
        setMaxHeight(ControllerConstants.HEIGHT);
        setMaxWidth(ControllerConstants.WIDTH);
    }

    public void changeLand(ObservableValue<? extends Land> p, Land oldLand, Land newLand) {
        if (oldLand != null) {
            forgetEntities(oldLand.getEnemies(), fightersListener);
            forgetEntities(oldLand.getInteractables(), interactablesListener);
            forgetEntities(oldLand.getPickables(), pickablesListener);
            forgetEntities(oldLand.getProjectiles(), projectilesListener);
            forgetEntities(oldLand.getDestructibles(), destructibleListener);
            remove(oldLand.getPlayer());
        }

        ground.getChildren().clear();
        for (Group slice : slices)
            slice.getChildren().clear();

        for (Tile[] tilesLine : newLand.getTiles())
            for (Tile tile : tilesLine)
                add(tile);

        Musics.play(newLand.getX() + "_" + newLand.getY());

        observeEntities(newLand.getEnemies(), fightersListener);
        observeEntities(newLand.getInteractables(), interactablesListener);
        observeEntities(newLand.getPickables(), pickablesListener);
        observeEntities(newLand.getProjectiles(), projectilesListener);
        observeEntities(newLand.getDestructibles(), destructibleListener);
        PlayerSprite ps = new PlayerSprite(this, newLand.getPlayer());
        add(ps);

    }

    public void add(Sprite<?> sprite) {
        ChangeListener<Number> listener = (p, oldSlice, newSlice) -> {
            if (oldSlice.intValue() != newSlice.intValue()) {
                removeFromSlice(sprite, oldSlice.intValue());
                addIntoSlice(sprite, newSlice.intValue());
            }
        };
        addIntoSlice(sprite, sprite.getIndexSlice());
        sprite.indexSliceBind().addListener(listener);
        listenersSprites.put(sprite.getId(), listener);
    }

    private boolean validIndex(int index) {
        return 0 <= index && index < slices.length;
    }

    private void removeFromSlice(Sprite<?> sprite, int index) {
        if (validIndex(index)) {
            boolean removed = slices[index].getChildren().remove(sprite);
            int i = 0;
            while (!removed && (index - i >= 0 || index + i < slices.length)) {
                if (index - i >= 0)
                    removed = slices[index - i].getChildren().remove(sprite);
                if (index + i < slices.length && !removed)
                    removed = slices[index + i].getChildren().remove(sprite);
                i++;
            }

            if (!removed) {
                throw new NullPointerException("Can't remove sprite " + sprite + " at index " + index);
            }
        }
    }

    private void addIntoSlice(Sprite<?> sprite, int index) {
        if (validIndex(index))
            slices[index].getChildren().add(sprite);
    }

    public void add(Node node) {
        getChildren().add(node);
    }

    public void remove(Node node) {
        getChildren().remove(node);
    }

    public void remove(Sprite<?> sprite, String id) {
        ChangeListener<Number> listener = listenersSprites.remove(id);
        if (sprite != null) {
            sprite.stop();
            sprite.indexSliceBind().removeListener(listener);
            removeFromSlice(sprite, sprite.getIndexSlice());
        }
    }

    public void remove(Entity entity) {
        remove((Sprite<?>) lookup("#" + entity.getId()), entity.getId());
    }

    public void add(Tile tile) {
        ObservableList<ObservableList<Integer>> layers = tile.getLayers();
        for (int z = 0; z < layers.size(); z++) {
            Group layer = z == 0 ? ground : slices[(tile.getY() + z + ControllerConstants.DEPTH) * TILE_SIZE - TILE_SIZE / 2];
            for (int code : layers.get(z)) {
                if (code != -1) {
                    ImageView imageTile = new ImageView(Images.TILES.get()[code]);
                    imageTile.setX(tile.getX() * TILE_SIZE);
                    imageTile.setY(tile.getY() * TILE_SIZE);
                    layer.getChildren().add(imageTile);
                }
            }
        }
    }

    private <E extends Entity> void observeEntities(ObservableList<E> entities, EntityListener<E> listener) {
        for (E e : entities)
            add(listener.make(e));
        entities.addListener(listener);
    }

    private <E extends Entity> void forgetEntities(ObservableList<E> entities, EntityListener<E> listener) {
        for (E e : entities)
            remove(e);
        entities.removeListener(listener);
    }

}
