package voodoo.demo.boid;

import org.joml.Vector3f;

public class Boundary {

    private final Vector3f min, max;

    private Vector3f position;

    public Boundary(Vector3f min, Vector3f max, Vector3f position) {
        this.min = min;
        this.max = max;
        this.position = position;
    }

    public boolean isPointInsideBoundary(Vector3f point) {
        return (point.x >= min.x + position.x && point.x <= max.x + position.x) &&
                (point.y >= min.y + position.y && point.y <= max.y + position.y) &&
                (point.z >= min.z + position.z && point.z <= max.z + position.z);
    }

    public Vector3f getMin() {
        return min;
    }

    public Vector3f getMax() {
        return max;
    }

    public Vector3f getPosition() {return position;}

}
