package trash.scene;

import trash.graphics.*;

public class MainScene implements Scene {

    private Shader shader;
    private Model model;

    public MainScene() {
        try {
            shader = new Shader("basic.vs.glsl", "basic.fs.glsl");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        model = ModelBuilder.buildColorfulRectangle();
    }

    @Override
    public void update(double dt) {
    }

    @Override
    public void render(double dt) {
        model.draw(shader);
    }
}
