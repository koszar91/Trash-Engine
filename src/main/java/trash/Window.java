package trash;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import trash.scene.RacingScene;
import trash.scene.Scene;
import trash.scene.SceneEnum;
import trash.util.Time;

import java.util.Objects;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Window {

    private static Window instance; // this class is a singleton

    private final long glfwWindow;

    private int width = 2560, height = 1440;
    private final String name = "Trash Engine";
    private double frameTime = 1 / 75.0;
    private Scene currentScene = null;

    private Window() {
        System.out.println("Trash engine initialization...");
        System.out.println("LWJGL Version: " + Version.getVersion());

        // setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // create window
        glfwWindow = glfwCreateWindow(width, height, name, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("GLFW Window creation failed.");
        }

        // setup event callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, Window::resizeCallback);
        glfwSetFramebufferSizeCallback(glfwWindow, Window::framebufferSizeCallback);

        // initialize OpenGL context
        glfwMakeContextCurrent(glfwWindow);

        // enable v-sync
        glfwSwapInterval(1);

        // make the window visible
        glfwShowWindow(glfwWindow);

        // something important
        GL.createCapabilities();

        updateFPSTitle();
        System.out.println("Trash engine initialized.");
    }

    public static Window getInstance() {
        if (instance == null) {
            instance = new Window();
        }
        return instance;
    }

    // interface
    public void run() {
        System.out.println("Trash engine running...");

        setScene(SceneEnum.RACING);

        loop();
        terminate();
    }

    public void setScene(SceneEnum scene) {
        switch(scene) {
            case RACING: currentScene = new RacingScene();
        }
    }

    public void enableVsync(boolean enable) {
        if (enable) {
            glfwSwapInterval(1);
        } else {
            glfwSwapInterval(0);
        }
    }

    public double getFramerate() {
        return frameTime != 0 ?  1 / frameTime : Double.MAX_VALUE;
    }

    // callbacks
    public static void resizeCallback(long window, int width, int height) {
        getInstance().width = width;
        getInstance().height = height;
        glViewport(0, 0, width, height);
    }

    public static void framebufferSizeCallback(long window, int width, int height) {
        glViewport(0, 0,  width, height);
    }

    // private methods
    private void loop() {
        double lastFPSTitleUpdate = Time.getTime();

        double frameStartTime = Time.getTime();
        while (!glfwWindowShouldClose(glfwWindow)) {

            // poll events
            glfwPollEvents();

            // update game logic
            currentScene.update(frameTime);

            // render
            glClearColor(0.5f, 0.5f, 0.8f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);
            glfwSwapBuffers(glfwWindow);

            // update window title fps, if
            double fpsTitleRefreshRate = 0.75;
            if (Time.getTime() - lastFPSTitleUpdate >= fpsTitleRefreshRate) {
                lastFPSTitleUpdate = Time.getTime();
                updateFPSTitle();
            }

            // calculate frame time
            frameTime = Time.getTime() - frameStartTime;
            frameStartTime = Time.getTime();
        }
    }

    private void terminate() {
        // free memory :-)
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();

        System.out.println("Trash engine terminated.");
    }

    private void updateFPSTitle() {
        glfwSetWindowTitle(glfwWindow, String.format("%s | %d fps", name, (int) getFramerate()));
    }
}
