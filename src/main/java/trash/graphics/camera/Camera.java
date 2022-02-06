package trash.graphics.camera;

import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector3f;


public class Camera {

    @Getter
    protected Vector3f position, front, up;
    @Getter
    protected Matrix4f projection, view;
    @Getter
    protected float fov, aspectRatio;

    protected static final float DEFAULT_NEAR = (float) 1e-2;
    protected static final float DEFAULT_FAR = (float) 1e4;
    protected static final Vector3f DEFAULT_POSITION = new Vector3f(0, 0, 0);
    protected static final Vector3f DEFAULT_FRONT = new Vector3f(0, 0, -1);
    protected static final Vector3f DEFAULT_UP = new Vector3f(0, 1, 0);

    public Camera(Vector3f position, Vector3f front, Vector3f up, float fov, float aspectRatio) {
        if (front.length() <= 0) {
            throw new IllegalArgumentException("Front vector must not be zero!");
        }
        if (up.length() <= 0) {
            throw new IllegalArgumentException("Up vector must not be zero!");
        }
        this.position = position;
        this.front = front;
        if (front.cross(up, new Vector3f()).length() == 0) {
            throw new IllegalArgumentException("Front vector cannot be collinear with up vector!");
        }
        this.up = up;

        this.fov = fov;
        this.aspectRatio = aspectRatio;

        updateProjection();
        updateView();
    }

    public Camera(Vector3f position, Vector3f front, float fov, float aspectRatio) {
        this(position, front, DEFAULT_UP, fov, aspectRatio);
    }

    public Camera(Vector3f position, float fov, float aspectRatio) {
        this(position, DEFAULT_FRONT, DEFAULT_UP, fov, aspectRatio);
    }

    public Camera(float fov, float aspectRatio) {
        this(DEFAULT_POSITION, DEFAULT_FRONT, DEFAULT_UP, fov, aspectRatio);
    }

    public void setFov(float fov) {
        this.fov = fov;
        updateProjection();
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        updateProjection();
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        updateView();
    }

    public void setFront(Vector3f front) {
        this.front = front;
        updateView();
    }

    public void setUp(Vector3f up) {
        this.up = up;
        updateView();
    }

    private void updateProjection() {
        this.projection = new Matrix4f().perspective(fov, aspectRatio, DEFAULT_NEAR , DEFAULT_FAR);
    }

    private void updateView() {
        var target = position.add(front, new Vector3f());
        this.view = new Matrix4f().lookAt(position, target, up);
    }

}
