package trash.util;

import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {

    @Getter
    private Vector3f position, rotation, scale;
    @Getter
    private Matrix4f modelMatrix;

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        updateMatrix();
    }

    private void updateMatrix() {
        this.modelMatrix = new Matrix4f()
                .rotateX(rotation.x).rotateY(rotation.y).rotateZ(rotation.z)
                .scale(scale)
                .translate(position);
    }
}
