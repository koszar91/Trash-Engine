package trash.graphics;

public class ModelBuilder {

    public static Model buildColorfulRectangle() {
        float[] vertices = {
                // position               // color
                0.5f, -0.5f, 0.0f,        1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
                -0.5f,  0.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
                0.5f,  0.5f, 0.0f ,       0.0f, 0.0f, 1.0f, 1.0f, // Top right    2
                -0.5f, -0.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
        };
        int[] indices = {
                2, 1, 0, // Top right triangle
                0, 1, 3 // bottom left triangle
        };
        return new Model(new Mesh(vertices, indices));
    }
}
