package fr.dashstrom.controller.sprites;

import fr.dashstrom.controller.UtilsController;
import fr.dashstrom.controller.ZPane;
import fr.dashstrom.model.box.WalkBox;
import fr.dashstrom.model.entity.Entity;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static fr.dashstrom.controller.ControllerConstants.DEPTH;
import static fr.dashstrom.controller.ControllerConstants.FLY_DEPTH;
import static fr.dashstrom.model.ModelConstants.TILE_SIZE;

public class Sprite<E extends Entity> extends ImageView {

    private final AtomicReference<Image> lastStainFrame;
    private final AtomicReference<Image> lastFrame;
    private final Map<Color, Double> colorFilters;

    private final E entity;
    private final Image[] frames;
    private final Map<Integer, Animation> animations;
    private final ZPane pane;

    private Animation lastAnim;
    private NumberBinding indexSlice;

    /**
     * Add an animation from frames to the sprite
     *
     * @param frames array of images
     * @param entity Entity to anchor
     **/
    public Sprite(ZPane pane, E entity, int dx, int dy, Image... frames) {
        super();
        lastFrame = new AtomicReference<>(getImage());
        lastStainFrame = new AtomicReference<>(getImage());
        colorFilters = new HashMap<>();

        translateXProperty().bind(entity.xProperty().add(dx));
        translateYProperty().bind(entity.yProperty().add(dy));
        setId(entity.getId());
        animations = new HashMap<>();
        this.frames = frames;
        this.entity = entity;
        setImage(frames[0]);
        this.pane = pane;
        indexSlice = null;

        imageProperty().addListener((p, o, img) -> interceptImage(img));
    }

    public Sprite(ZPane pane, E entity, Image... frames) {
        this(pane, entity, -UtilsController.midleX(frames), -UtilsController.midleY(frames), frames);
    }

    /**
     * Intercept an Image for stain it.
     *
     * @param img Picture to possibly paint
     */
    private void interceptImage(Image img) {
        if (!img.equals(lastStainFrame.get())) {
            Image colorized = filterImage(img);
            if (colorized != null) {
                lastStainFrame.set(colorized);
                setImage(colorized);
            }
        }
    }

    /**
     * Filters the colors of an image
     *
     * @param img image to filter
     * @return Image which tends towards color filters or null if weight total is lower than 0.001
     */
    private Image filterImage(Image img) {
        double size = 0;
        double r = 0, g = 0, b = 0, a = 0;
        for (Map.Entry<Color, Double> e : colorFilters.entrySet()) {
            Color c = e.getKey();
            double weight = e.getValue();
            size += weight;
            r += c.getRed() * weight;
            g += c.getGreen() * weight;
            b += c.getBlue() * weight;
            a += c.getOpacity() * weight;
        }
        if (size <= 0.001) {
            return null;
        } else {
            return UtilsController.stain(img, new Color(r / size, g / size, b / size, a / size), size);
        }
    }

    public void colorize(Color color, int ms, double weight) {
        if (ms < 5 || weight <= 0.001)
            return;
        if (colorFilters.isEmpty())
            lastFrame.set(getImage());
        colorFilters.putIfAbsent(color, 0.0);
        colorFilters.computeIfPresent(color, (c, cpt) -> cpt + weight);
        interceptImage(lastFrame.get());
        Timeline colorAnimation = new Timeline(
            new KeyFrame(Duration.millis(ms), e -> {
                colorFilters.computeIfPresent(color, (c, cpt) -> cpt - weight);
                if (colorFilters.get(color) <= 0.001)
                    colorFilters.remove(color);
                setImage(lastFrame.get());
            })
        );
        colorAnimation.setCycleCount(1);
        colorAnimation.play();
    }

    public NumberBinding indexSliceBind() {
        if (indexSlice == null) {
            ObjectProperty<WalkBox> pWBox = getEntity().walkboxProperty();
            NumberBinding zBind = Bindings.when(pWBox.isNull()).then(FLY_DEPTH * TILE_SIZE).otherwise(0);
            indexSlice = getEntity().yProperty().add(zBind).add(DEPTH * TILE_SIZE);
        }
        return indexSlice;
    }

    public int getIndexSlice() {
        return indexSliceBind().getValue().intValue();
    }

    /**
     * Add an animation from frames to the sprite
     *
     * @param key         Unique key for get item
     * @param cycles      Number of repeat
     * @param FPS         Animation speed
     * @param framesIndex index of each frames
     **/
    public void addAnim(int key, int cycles, double FPS, int... framesIndex) {
        Timeline anim = new Timeline();
        // Set the numbers of repeat
        anim.setCycleCount(cycles);
        double time = 0;
        for (int index : framesIndex) {
            // Create a frame at 'time' replacing the last frame by the new
            KeyFrame kf = new KeyFrame(Duration.seconds(time), event -> setImage(frames[index]));
            anim.getKeyFrames().add(kf);
            time += 1 / FPS;
        }
        // Dummy frame for show last frame without reset to early
        anim.getKeyFrames().add(new KeyFrame(Duration.seconds(time), e -> {
        }));
        animations.put(key, anim);
    }

    /**
     * Add an loop animation from frames to the sprite
     *
     * @param key         Unique key for get item
     * @param FPS         Animation speed
     * @param framesIndex index of each frames
     **/
    public void addAnimLoop(int key, double FPS, int... framesIndex) {
        addAnim(key, -1, FPS, framesIndex);
    }

    /**
     * Add an animation
     *
     * @param key       Unique key for get item
     * @param animation Animation
     **/
    public void addAnim(int key, Animation animation) {
        animations.put(key, animation);
    }

    /**
     * Play an animation
     *
     * @param key Unique key for get item
     **/
    public void play(int key) {
        Animation anim = animations.get(key);
        stop();
        if (anim == null) {
            System.err.println(this + " can't find anim : " + key);
        } else {
            if (anim != lastAnim) {
                anim.play();
                lastAnim = anim;
            }
        }
    }

    public boolean playing(int key) {
        return lastAnim != null && animations.get(key) == lastAnim && lastAnim.getCurrentRate() != 0.0;
    }

    public void stop() {
        if (lastAnim != null)
            lastAnim.stop();
    }

    public E getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return String.format("%s(id=\"%s\" frames : %d, animations : %d)",
            getClass().getSimpleName(), getId(), frames.length, animations.size());
    }

    public ZPane getPane() {
        return pane;
    }

}
