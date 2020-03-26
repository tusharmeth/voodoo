package com.github.crembluray.voodoo.engine.graph;

import java.util.List;
import java.util.Map;

import com.github.crembluray.voodoo.engine.items.GameObject;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class FrustumCullingFilter {

    private final Matrix4f prjViewMatrix;

    private final FrustumIntersection frustumInt;

    public FrustumCullingFilter() {
        prjViewMatrix = new Matrix4f();
        frustumInt = new FrustumIntersection();
    }

    public void updateFrustum(Matrix4f projMatrix, Matrix4f viewMatrix) {
        // Calculate projection view matrix
        prjViewMatrix.set(projMatrix);
        prjViewMatrix.mul(viewMatrix);
        // Update frustum intersection class
        frustumInt.set(prjViewMatrix);
    }

    public void filter(Map<? extends Mesh, List<GameObject>> mapMesh) {
        for (Map.Entry<? extends Mesh, List<GameObject>> entry : mapMesh.entrySet()) {
            List<GameObject> gameObjects = entry.getValue();
            filter(gameObjects, entry.getKey().getBoundingRadius());
        }
    }

    public void filter(List<GameObject> gameObjects, float meshBoundingRadius) {
        float boundingRadius;
        Vector3f pos;
        for (GameObject gameObject : gameObjects) {
            if (!gameObject.isDisableFrustumCulling()) {
                boundingRadius = gameObject.getScale() * meshBoundingRadius;
                pos = gameObject.getPosition();
                gameObject.setInsideFrustum(insideFrustum(pos.x, pos.y, pos.z, boundingRadius));
            }
        }
    }

    public boolean insideFrustum(float x0, float y0, float z0, float boundingRadius) {
        return frustumInt.testSphere(x0, y0, z0, boundingRadius);
    }
}
