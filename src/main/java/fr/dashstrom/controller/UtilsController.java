package fr.dashstrom.controller;

import fr.dashstrom.utils.ResourceUtils;
import fr.dashstrom.model.LoadError;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Image utility allowing to load, crop, mirror, resize an Image
 **/
public class UtilsController {

    /**
     * Load an Image
     *
     * @param path Image path
     * @return An Image or null if can't load
     **/
    public static Image loadImage(String path) {
        return new Image(ResourceUtils.getResourceURL(path).toString());
    }

    /**
     * Load and Crop many time an image
     *
     * @param path    Image path
     * @param rows    Number of rows in image
     * @param lines   Number of lines in image
     * @param mirror  if true add to the array mirror images
     * @param reverse if true add to the array reverse image
     * @return An Array of Image loaded or generated from the image
     **/
    public static Image[] multiCrop(String path, int rows, int lines, boolean mirror, boolean reverse) {
        // Open the image
        Image image = loadImage(path);

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
                    underImages[y * rows + x + length] = mirror(img);
                if (reverse) {
                    Image reversed = reverse(img);
                    underImages[y * rows + x + size / 2] = reversed;
                    if (mirror)
                        underImages[y * rows + x + size / 2 + length] = mirror(reversed);
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
    public static Image mirror(Image image) {
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
    public static Image reverse(Image image) {
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
    public static Image stain(Image image, Color color, double weight) {
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

    /**
     * Load image dir
     *
     * @param directory path of dir
     * @return Map with String to Image
     */
    public static Map<String, Image> loadImageDir(String directory) {
        Log.info("Loading Images in " + directory);
        List<String> paths;
        try {
            paths = ResourceUtils.getResourceListDirectory(directory); //  Files.list(UtilsModel.path("image/" + path)).toArray(Path[]::new);
        } catch (IOException e) {
            throw new LoadError("Can't list dir for load all zones", e);
        }

        Map<String, Image> map = new HashMap<>();
        Pattern extarct = Pattern.compile(".*[\\\\/](?<name>.+)\\.(?:png|jpeg|gif|jpg)$", Pattern.CASE_INSENSITIVE);
        for (String path : paths) {
            Matcher m = extarct.matcher(path);
            if (m.matches())
                map.put(m.group("name"), UtilsController.loadImage(path));
        }
        return map;
    }

    /**
     * Get the midle on x axis of the first image or return 0
     *
     * @param images images to center
     * @return center X
     */
    public static int midleX(Image... images) {
        return images.length != 0 ? (int) Math.round(images[0].getWidth() / 2) : 0;
    }

    /**
     * Get the midle on y axis of the first image or return 0
     *
     * @param images images to center
     * @return center Y
     */
    public static int midleY(Image... images) {
        return images.length != 0 ? (int) Math.round(images[0].getHeight() / 2) : 0;
    }

}
