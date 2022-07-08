package fr.dashstrom.utils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceContainer<T> {

    private static final Map<String, ResourceContainer<?>> containers = new ConcurrentHashMap<>();
    private final String path;
    private final Loader<T> loader;
    private T resource;
    public ResourceContainer(String path, Loader<T> loader) {
        if (containers.containsKey(path))
            throw new IllegalArgumentException("This resource already exist");
        containers.put(path, this);
        this.path = path;
        this.loader = loader;
    }

    public static void loadAll() {
        for (ResourceContainer<?> res : containers.values())
            res.get();
    }

    public T get() {
        if (resource == null) {
            try {
                resource = loader.load();
            } catch (Exception err) {
                err.printStackTrace();
                throw new NullPointerException("Error during loading");
            }
            if (resource == null)
                throw new NullPointerException("Can't load " + this.path);
        }
        return resource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceContainer<?> that = (ResourceContainer<?>) o;
        return Objects.equals(path, that.path)
            && Objects.equals(loader, that.loader)
            && Objects.equals(resource, that.resource);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    public interface Loader<T> {

        T load() throws Exception;

    }

}
