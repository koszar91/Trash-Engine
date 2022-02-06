package trash.scene;

import imgui.ImGui;
import org.joml.Vector3f;
import trash.graphics.camera.FPSCamera;
import trash.graphics.Shader;
import trash.graphics.model.Model;
import trash.graphics.model.ModelBuilder;
import trash.util.Transform;
import trash.window.KeyListener;
import trash.window.Window;

import static org.lwjgl.glfw.GLFW.*;

public class MainScene implements Scene {

    private FPSCamera camera = new FPSCamera(75, Window.getInstance().getAspectRatio());
    private final double MOVEMENT_SPEED = 10.0;
    private Shader shader;
    private Model model = ModelBuilder.buildColorfulRectangle();
    private Transform transform = new Transform(new Vector3f(0, 0, -3), new Vector3f(0), new Vector3f(1));

    public MainScene() {
        try {
            shader = new Shader("3d.vs.glsl", "basic.fs.glsl");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void update(double dt) {
        processKeys(dt);
    }

    @Override
    public void render(double dt) {
        shader.bind();
        shader.setMatrix4("projection", camera.getProjection());
        shader.setMatrix4("view", camera.getView());
        shader.setMatrix4("model", transform.getModelMatrix());
        model.draw();
    }

    @Override
    public void renderImGui(double dt) {
        ImGui.showDemoWindow();

        ImGui.begin("Debugging");
        var camPos = camera.getPosition();
        var entityPos = transform.getPosition();
        ImGui.text(String.format("Camera position: (%f, %f, %f)",
                camPos.x, camPos.y, camPos.z));
        ImGui.text(String.format("Entity position: (%f, %f, %f)",
                entityPos.x, entityPos.y, entityPos.z));
        ImGui.end();
    }


    private void processKeys(double dt) {
        var listener = KeyListener.getInstance();
        if (listener.isPressed(GLFW_KEY_W)) {
            camera.moveForward(dt, MOVEMENT_SPEED);
        }
        if (listener.isPressed(GLFW_KEY_A)) {
            camera.moveLeft(dt, MOVEMENT_SPEED);
        }
        if (listener.isPressed(GLFW_KEY_S)) {
            camera.moveBackward(dt, MOVEMENT_SPEED);
        }
        if (listener.isPressed(GLFW_KEY_D)) {
            camera.moveRight(dt, MOVEMENT_SPEED);
        }
        if (listener.isPressed(GLFW_KEY_SPACE)) {
            camera.moveUp(dt, MOVEMENT_SPEED);
        }
        if (listener.isPressed(GLFW_KEY_LEFT_SHIFT)) {
            camera.moveDown(dt, MOVEMENT_SPEED);
        }
        if (listener.isPressed(GLFW_KEY_ESCAPE)) {
            Window.getInstance().close();
        }
    }
}
