package fr.dashstrom.controller;

import fr.dashstrom.utils.ResourceContainer;
import fr.dashstrom.utils.ResourceUtils;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Audio with favafx is buged, use javax
 */
public class Musics {

    private static String lastMusic = null;
    private static MediaPlayer player;

    private static final Map<String, ResourceContainer<MediaPlayer>> musics = new HashMap<String, ResourceContainer<MediaPlayer>>() {{
        put("0_0", media("0_0"));
        put("0_-1", media("0_-1"));
        put("0_-2", media("0_-2"));
        put("0_-3", media("0_-3"));
        put("1_-1", media("1_-1"));
        put("-1_-1", media("-1_-1"));
    }};

    private static ResourceContainer<MediaPlayer> media(String name) {
        return new ResourceContainer<>("/audio/" + name + ".mp3", () -> {
            Media media;
            try {
                media = new Media(ResourceUtils.getResourceString("/audio/" + name + ".mp3"));
            } catch (IllegalArgumentException err) {
                return null;
            }
            lastMusic = name;
            player = new MediaPlayer(media);
            player.setVolume(0.1D);
            player.setCycleCount(-1);
            return player;
        });
    }

    /**
     * Play one music, if one is already playing cut it
     *
     * @param name Name of sound, do nothing if null or already playing
     */
    public static synchronized void play(String name) {
        if (name != null && !name.equals(lastMusic)) {
            ResourceContainer<MediaPlayer> md = musics.get(name);
            if (md == null)
                return;
            stop();
            player = md.get();
            lastMusic = name;
            new Thread(() -> {
                try {
                    player.setStartTime(Duration.ZERO);
                    player.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public static synchronized void stop() {
        if (player != null)
            player.stop();
        lastMusic = null;
    }

}
