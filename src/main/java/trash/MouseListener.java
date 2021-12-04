package trash;

import java.util.stream.IntStream;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    private static MouseListener instance;  // this class is a singleton

    private double scrollX, scrollY;
    private double posX, posY, prevX, prevY;

    private static final int N_BUTTONS = 3;
    private final boolean[] pressedButtons = new boolean[N_BUTTONS];

    private boolean isDragging;

    private MouseListener() {
        scrollX = scrollY = 0;
        posX = posY = prevY = prevX = 0;
    }

    public static MouseListener getInstance() {
        if (instance == null) {
            instance = new MouseListener();
        }
        return instance;
    }

    // callbacks
    public static void mousePosCallback(long window, double posX, double posY) {
        var instance = getInstance();
        instance.prevX = instance.posX;
        instance.prevY = instance.posY;
        instance.posX = posX;
        instance.posY = posY;

        // if any of the buttons are pressed, then it's dragging
        instance.isDragging = IntStream.range(0, instance.pressedButtons.length)
                .mapToObj(idx -> instance.pressedButtons[idx])
                .reduce(Boolean::logicalOr)
                .orElse(false);
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        // ignore unknown buttons
        if (button < N_BUTTONS) {
            return;
        }

        var instance = getInstance();
        if (action == GLFW_PRESS) {
            instance.pressedButtons[button] = true;
        } else if (action == GLFW_RELEASE) {
            instance.pressedButtons[button] = false;
            instance.isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        var instance = getInstance();
        instance.scrollX = xOffset;
        instance.scrollY = yOffset;
    }

    // interface
    public static float getX() {
        return (float) getInstance().posX;
    }

    public static float getY() {
        return (float) getInstance().posY;
    }

    public static float getDx() {
        return (float) (getInstance().prevX - getInstance().posX);
    }

    public static float getDy() {
        return (float) (getInstance().prevY - getInstance().posY);
    }

    public static float getScrollX() {
        return (float) getInstance().scrollX;
    }

    public static float getScrollY() {
        return (float) getInstance().scrollY;
    }

    public static boolean isDragging() {
        return getInstance().isDragging;
    }

    public static boolean isPressed(int button) {
        if (button < N_BUTTONS) {
            return false;
        }
        return getInstance().pressedButtons[button];
    }

    // has to be called when frame has ended
    public static void endFrame() {
        var instance = getInstance();
        instance.scrollX = instance.scrollY = 0;
        instance.prevY = instance.posY;
        instance.prevX = instance.posX;
    }
}
