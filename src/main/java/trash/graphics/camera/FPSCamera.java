package trash.graphics.camera;

import org.joml.Vector3f;

public class FPSCamera extends Camera {

    public FPSCamera(Vector3f position, Vector3f target, Vector3f up, float fov, float aspectRatio) {
        super(position, target, up, fov, aspectRatio);
    }

    public FPSCamera(Vector3f position, Vector3f target, float fov, float aspectRatio) {
        super(position, target, fov, aspectRatio);
    }

    public FPSCamera(Vector3f position, float fov, float aspectRatio) {
        super(position, fov, aspectRatio);
    }

    public FPSCamera(float fov, float aspectRatio) {
        super(fov, aspectRatio);
    }

    public void moveForward(double dt, double speed) {
        var frontProjectedOnXZPlane = new Vector3f(front.x, 0, front.z).normalize();
        position.add(frontProjectedOnXZPlane.mul((float) (speed * dt)));
    }

    public void moveBackward(double dt, double speed) {
        moveForward(dt, -speed);
    }

    public void moveRight(double dt, double speed) {
        var front = getFront();
        var frontProjectedOnXZPlane = new Vector3f(front.x, 0, front.z).normalize();
        var right = frontProjectedOnXZPlane.cross(up, new Vector3f()).normalize();
        position.add(right.mul((float) (speed * dt)));
    }

    public void moveLeft(double dt, double speed) {
        moveRight(dt, -speed);
    }

    public void moveUp(double dt, double speed) {
        position.add(new Vector3f(0, (float) (speed * dt), 0));
    }

    public void moveDown(double dt, double speed) {
        moveUp(dt, -speed);
    }
}
