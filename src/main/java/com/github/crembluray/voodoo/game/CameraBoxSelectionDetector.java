package com.github.crembluray.voodoo.game;

import com.github.crembluray.voodoo.engine.graph.Camera;
import com.github.crembluray.voodoo.engine.items.GameObject;
import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CameraBoxSelectionDetector {

    private final Vector3f max;

    private final Vector3f min;

    private final Vector2f nearFar;

    private Vector3f dir;

    public CameraBoxSelectionDetector() {
        dir = new Vector3f();
        min = new Vector3f();
        max = new Vector3f();
        nearFar = new Vector2f();
    }

    public void selectGameItem(GameObject[] gameObjects, Camera camera) {
        dir = camera.getViewMatrix().positiveZ(dir).negate();
        selectGameItem(gameObjects, camera.getPosition(), dir);
    }
    
    protected boolean selectGameItem(GameObject[] gameObjects, Vector3f center, Vector3f dir) {
        boolean selected = false;
        GameObject selectedGameObject = null;
        float closestDistance = Float.POSITIVE_INFINITY;

        for (GameObject gameObject : gameObjects) {
            gameObject.setSelected(false);
            min.set(gameObject.getPosition());
            max.set(gameObject.getPosition());
            min.add(-gameObject.getScale(), -gameObject.getScale(), -gameObject.getScale());
            max.add(gameObject.getScale(), gameObject.getScale(), gameObject.getScale());
            if (Intersectionf.intersectRayAab(center, dir, min, max, nearFar) && nearFar.x < closestDistance) {
                closestDistance = nearFar.x;
                selectedGameObject = gameObject;
            }
        }

        if (selectedGameObject != null) {
            selectedGameObject.setSelected(true);
            selected = true;
        }
        return selected;
    }
}
