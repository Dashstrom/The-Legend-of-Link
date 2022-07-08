package fr.dashstrom.controller;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class KeyController {

    private final HashMap<KeyCode, ArrayList<SimpleHandler>> handlersPressed;
    private final HashMap<KeyCode, ArrayList<SimpleHandler>> handlersReleased;
    private HashSet<KeyCode> keysPressed;

    public KeyController(Parent root) {
        this.keysPressed = new HashSet<>();
        this.handlersPressed = new HashMap<>();
        this.handlersReleased = new HashMap<>();
        root.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (!keysPressed.contains(code)) {
                keysPressed.add(code);
                callHandlers(code, handlersPressed);
            }
        });

        root.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            if (keysPressed.remove(code))
                callHandlers(code, handlersReleased);
        });
    }

    /**
     * Set all pressed keys as released and call handlers if exists.
     */
    public void cleanKeys() {
        HashSet<KeyCode> keysPressed = this.keysPressed;
        this.keysPressed = new HashSet<>();
        for (KeyCode key : keysPressed)
            callHandlers(key, handlersReleased);
    }

    public boolean isPressed(KeyCode code) {
        return keysPressed.contains(code);
    }

    private void callHandlers(KeyCode code, HashMap<KeyCode, ArrayList<SimpleHandler>> handlers) {
        ArrayList<SimpleHandler> handlersKey = handlers.get(code);
        if (handlersKey != null) {
            for (SimpleHandler handler : handlersKey) {
                try {
                    handler.handle();
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        }
    }

    public void listen(KeyCode code, BooleanHandler handler) {
        listenPressed(code, () -> handler.handle(true));
        listenReleased(code, () -> handler.handle(false));
    }

    public void listenPressed(KeyCode code, SimpleHandler handler) {
        handlersPressed.computeIfAbsent(code, k -> new ArrayList<>()).add(handler);
    }

    public void listenReleased(KeyCode code, SimpleHandler handler) {
        handlersReleased.computeIfAbsent(code, k -> new ArrayList<>()).add(handler);
    }

    public interface BooleanHandler {

        /**
         * Call when change, can be press or a release.
         *
         * @param pressed True if key is pressed else false
         */
        void handle(boolean pressed);

    }

    public interface SimpleHandler {

        /**
         * Call when change, can be press or a release
         */
        void handle();

    }

}
