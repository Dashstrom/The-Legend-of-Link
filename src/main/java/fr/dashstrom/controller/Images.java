package fr.dashstrom.controller;

import fr.dashstrom.utils.ResourceUtils;
import fr.dashstrom.utils.ResourceContainer;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class Images {

    public static final ResourceContainer<Image>
        SLASH = image("/image/items/slash.png"),
        AXE = image("/image/items/axe.png"),
        MASTER_AXE = image("/image/items/masteraxe.png"),
        FIREBALL = image("/image/items/fireball.png"),
        CRATE = image("/image/crates/crate.png"),
        HAY = image("/image/crates/hay.png"),
        HEART = image("/image/hud/heart.png"),
        EMPTY_HEART = image("/image/hud/empty_heart.png"),
        HALF_HEART = image("/image/hud/half_heart2.png"),
        ERROR = image("/image/error.png"),
        FIRE_STAFF = image("/image/items/firestaff.png"),
        FIRE_AXE = image("/image/items/fireaxe.png"),
        ICE_STAFF = image("/image/items/icestaff.png"),
        ICEBALL = image("/image/items/iceball.png"),
        LIGHT_SHIELD = image("/image/enemies/hydra_shield.png"),
        ICE = image("/image/crates/ice.png"),
        ICON = image("/image/icon.png");
    public static final ResourceContainer<Image[]>
        TILES = crave("/image/tiles/tileSet.png", 64, 64, false, false),
        PLAYER = crave("/image/characters/zelda.png", 32, 1, true, false),
        BUG = crave("/image/enemies/bug.png", 5, 4, false, false),
        NPC = crave("/image/characters/green_npc.png", 3, 4, false, false),
        LINK = crave("/image/characters/link.png", 5, 4, false, false),
        RED_DRAGON = crave("/image/enemies/red_dragon.png", 3, 4, false, false),
        BLUE_DRAGON = crave("/image/enemies/blue_dragon.png", 3, 4, false, false),
        FIRE = crave("/image/crates/fire.png", 8, 4, false, false),
        HYDRA = crave("/image/enemies/hydra.png", 3, 4, false, false),
        POTIONS_BLUE = crave("/image/items/potions/b.png", 3, 1, false, false),
        POTIONS_GREEN = crave("/image/items/potions/g.png", 3, 1, false, false),
        POTIONS_RED = crave("/image/items/potions/r.png", 3, 1, false, false),
        POTIONS_YELLOW = crave("/image/items/potions/y.png", 3, 1, false, false);
    public static final Map<String, ResourceContainer<Image>> ASSIGNABLES = new HashMap<String, ResourceContainer<Image>>() {{
        put("Coeur", HEART);
        put("Potion de vie", HEART);
        put("Bâton de feu", FIRE_STAFF);
        put("Bâton de glace", ICE_STAFF);
        put("Hache de feu", FIRE_AXE);
        put("Hache Ultime", MASTER_AXE);
        put("Hache", AXE);
    }};

    /**
     * Load and Crop many time an image
     *
     * @param rows    Number of rows in image
     * @param lines   Number of lines in image
     * @param mirror  if true add to the array mirror images
     * @param reverse if true add to the array reverse image
     * @return An Array of Image loaded or generated from the image
     **/
    public static Image[] craveTransform(Image image, int rows, int lines, boolean mirror, boolean reverse) {
        // Open the image

        // width and height in cropped image size
        int widthCrop = (int) Math.round(image.getWidth() / rows);
        int heightCrop = (int) Math.round(image.getHeight() / lines);
        int length = rows * lines;
        int size = length;

        // Create an instance of pixelreader for work with pixels
        PixelReader reader = image.getPixelReader();

        // get the final size of array
        if (mirror)
            size *= 2;
        if (reverse)
            size *= 2;
        Image[] underImages = new Image[size];
        for (int y = 0; y < lines; y++) {
            for (int x = 0; x < rows; x++) {
                // Crop image in the main image
                Image img = new WritableImage(reader, x * widthCrop, y * heightCrop, widthCrop, heightCrop);
                underImages[y * rows + x] = img;
                // Make mirror and reverse
                if (mirror)
                    underImages[y * rows + x + length] = mirrorTransform(img);
                if (reverse) {
                    Image reversed = reverseTransform(img);
                    underImages[y * rows + x + size / 2] = reversed;
                    if (mirror)
                        underImages[y * rows + x + size / 2 + length] = mirrorTransform(reversed);
                }
            }
        }
        return underImages;
    }

    /**
     * Create an Image that is the mirror of image in parameter (along x axis)
     *
     * @param image Image to mirror
     * @return mirrored Image
     **/
    public static Image mirrorTransform(Image image) {
        int w = (int) image.getWidth(), h = (int) image.getHeight();
        PixelReader reader = image.getPixelReader();
        WritableImage mirrored = new WritableImage(w, h);
        PixelWriter writer = mirrored.getPixelWriter();
        for (int y = 0; y < h; ++y)
            for (int x = 0; x < w; ++x)
                writer.setArgb(w - x - 1, y, reader.getArgb(x, y));
        return mirrored;
    }

    /**
     * Create an Image that is the reverse of image in parameter (along y axis)
     *
     * @param image Image to reverse
     * @return reversed Image
     **/
    public static Image reverseTransform(Image image) {
        int w = (int) image.getWidth(), h = (int) image.getHeight();
        PixelReader reader = image.getPixelReader();
        WritableImage mirrored = new WritableImage(w, h);
        PixelWriter writer = mirrored.getPixelWriter();
        for (int y = 0; y < h; ++y)
            for (int x = 0; x < w; ++x)
                writer.setArgb(x, h - y - 1, reader.getArgb(x, y));
        return mirrored;
    }

    /**
     * Stain an Image with a specific color
     *
     * @param image Image to reverse
     * @param color color used to dye
     * @return Stain Image
     **/
    public static Image stainTransform(Image image, Color color, double weight) {
        int w = (int) image.getWidth(), h = (int) image.getHeight();
        PixelReader reader = image.getPixelReader();
        WritableImage colored = new WritableImage(w, h);
        PixelWriter writer = colored.getPixelWriter();
        double ratio = (1 + weight);
        for (int y = 0; y < h; ++y)
            for (int x = 0; x < w; ++x) {
                Color c = reader.getColor(x, y);
                writer.setColor(x, y, new Color(
                    (c.getRed() + color.getRed() * weight) / ratio,
                    (c.getGreen() + color.getGreen() * weight) / ratio,
                    (c.getBlue() + color.getBlue() * weight) / ratio,
                    c.getOpacity()
                ));

            }
        return colored;
    }

    private static Image get(String path) {
        return new Image(ResourceUtils.getResourceURL(path).toString());
    }

    public static ResourceContainer<Image> image(String path) {
        return new ResourceContainer<>(path, () -> get(path));
    }

    public static ResourceContainer<Image[]> crave(String path, int rows, int lines, boolean mirror, boolean reverse) {
        return new ResourceContainer<>(path,
            () -> craveTransform(Images.get(path), rows, lines, mirror, reverse));
    }

}
