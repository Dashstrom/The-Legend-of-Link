package fr.dashstrom.model;

/**
 * Fatal Error, must threw for loading errors during backup or asset loading.
 */
public class LoadError extends RuntimeException {

    public LoadError(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadError(String message) {
        super(message);
    }

}
