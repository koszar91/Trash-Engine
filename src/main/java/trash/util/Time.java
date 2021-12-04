package trash.util;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time {
    private static final double timeAppStartedRunning = glfwGetTime();

    /**
     * Method returning current time in seconds
     */
    public static double getTime() {
        return glfwGetTime() - Time.timeAppStartedRunning;
    }
}
