package voodoo.engine.physics;

import org.joml.Vector3f;
import voodoo.engine.gameObject.Component;
import voodoo.engine.util.Utils;

import java.util.List;

public class AABB extends Component {

    // Position is offset from parent object
    private Vector3f position;

    // Stores the x, y, and z values of the bounding box
    private Vector3f min, max;

    public AABB() {
        min = new Vector3f(0, 0, 0);
        max = new Vector3f(0, 0, 0);
        position = new Vector3f(0, 0, 0);
    }

    public void calcAABB(String file) throws Exception {
        List<String> lines = Utils.readAllLines(file);
        for (String line : lines) {
            String[] tokens = line.split("\\s+");
            if ("v".equals(tokens[0])) {
                Vector3f ver = new Vector3f(
                        Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2]),
                        Float.parseFloat(tokens[3]));
                if (ver.x < min.x) min.x = ver.x;
                else if (ver.x > max.x) max.x = ver.x;
                if (ver.y < min.y) min.y = ver.y;
                else if (ver.y > max.y) max.y = ver.y;
                if (ver.z < min.z) min.z = ver.z;
                else if (ver.z > max.z) max.z = ver.z;
            }
        }
    }

    public boolean isPointInsideAABB(Vector3f point) {
        return (point.x >= (min.x * parent.getScale()) + position.x && point.x <= max.x + position.x) &&
               (point.y >= (min.y * parent.getScale()) + position.y && point.y <= max.y + position.y) &&
               (point.z >= (min.z * parent.getScale()) + position.z && point.z <= max.z + position.z);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getMin() {
        return min;
    }

    public Vector3f getMax() {
        return max;
    }
}
