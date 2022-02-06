package trash.scene;

public interface Scene {
    void update(double dt);
    void render(double dt);
    void renderImGui(double dt);
}

