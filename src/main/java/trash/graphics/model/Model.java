package trash.graphics.model;

import java.util.List;

public class Model {

    private final List<Mesh> meshes;

    public Model(List<Mesh> meshes) {
        this.meshes = List.copyOf(meshes);
    }

    public Model(Mesh mesh) {
        this(List.of(mesh));
    }

    public void draw() {
        meshes.forEach(Mesh::draw);
    }
}
