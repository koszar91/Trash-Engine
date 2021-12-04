package trash;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {

    private static KeyListener instance; // this class is a singleton

    public static final int N_KEYS = 350;
    private boolean pressedKeys[] = new boolean[N_KEYS];

    private KeyListener() { }

    public static KeyListener getInstance() {
        if (instance == null) {
            instance = new KeyListener();
        }
        return instance;
    }

    // callback
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key > N_KEYS) {
            return;
        }

        if (action == GLFW_PRESS) {
            getInstance().pressedKeys[key] = true;
        } else if (action == GLFW_RELEASE) {
            getInstance().pressedKeys[key] = false;
        }
    }

    // interface
    public static boolean isPressed(int key) {
        if (!(0 <= key && key < N_KEYS)) {
            throw new IllegalArgumentException(String.format("No such key: %d", key));
        }
        return getInstance().pressedKeys[key];
    }

}
