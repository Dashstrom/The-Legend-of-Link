package fr.dashstrom.model;

import fr.dashstrom.utils.ResourceContainer;
import fr.dashstrom.utils.ResourceUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Dialogs {

    public static final ResourceContainer<String[]>
        CRATE = lines("/dialog/crate"),
        TEST = lines("/dialog/test"),
        ICE_FIRE = lines("/dialog/ice_fire"),
        BEFORE_BOSS = lines("/dialog/before_boss"),
        CLUE = lines("/dialog/clue"),
        LINK = lines("/dialog/link");

    public static ResourceContainer<String[]> lines(String path) {
        return new ResourceContainer<>(path, () -> {
            InputStream in = ResourceUtils.getResourceAsStream(path);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            List<String> lignes = new ArrayList<>();
            String ligne;
            while ((ligne = bufReader.readLine()) != null)
                lignes.add(ligne);
            String[] arr = new String[lignes.size()];
            lignes.toArray(arr);
            return arr;
        });
    }

}
