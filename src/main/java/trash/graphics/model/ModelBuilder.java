package trash.graphics.model;

public class ModelBuilder {

    public static Model buildColorfulRectangle() {
        float[] vertices = {
                // position            // color
                -1f,  1f, 0.0f,        0.0f, 1.0f, 0.0f, 1.0f, // Top left     0
                 1f,  1f, 0.0f,        0.0f, 0.0f, 1.0f, 1.0f, // Top right    1
                 1f, -1f, 0.0f,        1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 2
                -1f, -1f, 0.0f,        1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
        };
        int[] indices = {
                0, 1, 2, // Top right triangle
                0, 2, 3  // bottom left triangle
        };
        return new Model(new Mesh(vertices, indices));
    }
}
