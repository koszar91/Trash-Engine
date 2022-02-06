package trash.window;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import trash.scene.MainScene;
import trash.scene.Scene;
import trash.scene.SceneEnum;
import trash.util.Time;

import java.util.Objects;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Window {

    private static Window instance; // this class is a singleton (sorry, I'll do IoC later)

    private long glfwWindow;

    private int width = 1920, height = 1080;
    private final String name = "Trash Engine";
    private double frameTime = 1 / 75.0;
    private Scene currentScene = null;
    private static final double FPS_TITLE_REFRESH_RATE = 0.75;

    // ImGui stuff
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final String glslVersion = "#version 330";

    private Window() {
        System.out.println("Trash engine initialization...");
        System.out.println("LWJGL Version: " + Version.getVersion());

        initGLFW();
        initImGui();

        updateFPSTitle();
        glClearColor(0.5f, 0.5f, 0.8f, 1.0f);
        System.out.println("Trash engine initialized.");
    }

    public static Window getInstance() {
        if (instance == null) {
            instance = new Window();
        }
        return instance;
    }

    public float getAspectRatio() {
        return (float) width / (float) height;
    }

    // interface
    public void run() {
        System.out.println("Trash engine running...");

        setScene(SceneEnum.RACING);

        loop();
        terminate();
    }

    public void setScene(SceneEnum scene) {
        switch (scene) {
            case RACING -> currentScene = new MainScene();
            default -> throw new IllegalStateException("Unexpected scene value: " + scene);
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

    public void close() {
        glfwSetWindowShouldClose(glfwWindow, true);
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
    private void initImGui() {
        ImGui.createContext();
        imGuiGlfw.init(glfwWindow, true);
        imGuiGl3.init(glslVersion);
    }

    private void initGLFW() {
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
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

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
    }

    private void loop() {
        double lastFPSTitleUpdate = Time.getTime();
        double frameStartTime = Time.getTime();
        while (!glfwWindowShouldClose(glfwWindow)) {
            currentScene.update(frameTime);

            glClear(GL_COLOR_BUFFER_BIT);
            currentScene.render(frameTime);

            imGuiGlfw.newFrame();
            ImGui.newFrame();
            currentScene.renderImGui(frameTime);
            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());
            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                glfwMakeContextCurrent(backupWindowPtr);
            }
            glfwSwapBuffers(glfwWindow);
            glfwPollEvents();

            if (Time.getTime() - lastFPSTitleUpdate >= FPS_TITLE_REFRESH_RATE) {
                lastFPSTitleUpdate = Time.getTime();
                updateFPSTitle();
            }
            frameTime = Time.getTime() - frameStartTime;
            frameStartTime = Time.getTime();
        }
    }

    private void terminate() {

        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();

        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        Callbacks.glfwFreeCallbacks(glfwWindow);

        glfwTerminate();
        System.out.println("Trash engine terminated.");
    }

    private void updateFPSTitle() {
        glfwSetWindowTitle(glfwWindow, String.format("%s | %d fps", name, (int) getFramerate()));
    }
}
