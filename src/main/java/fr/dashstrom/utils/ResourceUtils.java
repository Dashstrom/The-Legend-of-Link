package fr.dashstrom.utils;

import fr.dashstrom.controller.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceUtils {

    public static final boolean DEBUG = inDebugMode();

    /**
     * Get a potential flog of debug mode,
     *
     * @return true if find else false
     */
    private static boolean inDebugMode() {
        for (String name : System.getProperties().stringPropertyNames())
            if (name.toLowerCase().contains("debug") && "true".equalsIgnoreCase(System.getProperty(name)))
                return true;
        return false;
    }

    public static URL getResourceURL(String file) {
        URL url = ResourceUtils.class.getResource(file);
        if (url == null) {
            // TODO turn into FileNotFoundError
            throw new IllegalArgumentException("Can't find file " + file);
        }
        return url;
    }

    public static InputStream getResourceAsStream(String file) {
        InputStream in = ResourceUtils.class.getResourceAsStream(file);
        if (in == null)
            throw new IllegalArgumentException("Invalid path " + file);
        return in;
    }

    public static URI getResourceURI(String file) {
        URL url = getResourceURL(file);
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL : " + url);
        }
    }

    public static String getResourceString(String file) {
        return getResourceURI(file).toString();
    }

    public static List<String> getResourceListDirectory(String directory) throws IOException {
        URI uri = getResourceURI(directory);
        List<String> paths = "jar".equals(uri.getScheme())
            ? getResourceFilesInJar(directory)
            : getResourceFilesInSrc(directory);
        if (paths.isEmpty())
            throw new FileNotFoundException("No sub items at " + directory);
        return paths;
    }

    /*
    public static boolean isDirectory(String path) {
        try {
            return new File(path).isDirectory();
        } catch (IllegalArgumentException err) {
            return false;
        }
    }

    public static boolean isFile(String path) {
        try {
            return new File(getResourceURI(path)).isFile();
        } catch (IllegalArgumentException err) {
            return false;
        }
    }
    */

    /*
    public static List<String> getResourceRecursive(String path) throws IOException {
        List<String> paths = new ArrayList<>();
        if (isDirectory(path)) {
            for (String p : getResourceListDirectory(path))
                paths.addAll(getResourceRecursive(p));
        } else {
            paths.add(path);
        }
        return paths;
    }
    */

    private static List<String> getResourceFilesInJar(String directory) throws IOException {
        Log.info("Scanning " + directory + " as a jar");
        List<String> filenames = new ArrayList<>();
        URI uri = getResourceURI(directory);
        FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
        Path directoryPath = fileSystem.getPath(directory);
        Stream<Path> walk = Files.walk(directoryPath, 1);
        Iterator<Path> walkIterator = walk.iterator();
        walkIterator.next(); // remove directory path
        while (walkIterator.hasNext()) {
            String path = pathUnify(walkIterator.next());
            Log.info(" - " + path);
            filenames.add(path);
        }
        fileSystem.close();
        return filenames;
    }

    private static List<String> getResourceFilesInSrc(String directory) throws IOException {
        Log.info("Scanning " + directory + " as a sys");
        return Files.list(Paths.get(getResourceURI(directory)))
            .map(p -> directory + "/" + p.getFileName())
            .collect(Collectors.toList());
    }

    private static String pathUnify(String path) {
        return path
            .replace('\\', '/')
            .replaceAll("[\\\\/]$", "")
            .replaceAll("^file:/", "");
    }

    private static String pathUnify(Path path) {
        return pathUnify(path.toString());
    }

}
