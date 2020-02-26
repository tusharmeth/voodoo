package com.github.crembluray.voodoo.engine.gameObject;

import com.github.crembluray.voodoo.engine.model.Mesh;
import org.joml.Vector3f;

public class GameObject {

    private Mesh mesh;

    private final Vector3f position;

    private float scale;

    private final Vector3f rotation;

    private final Component[] components;

    public GameObject() {
        position = new Vector3f();
        scale = 1;
        rotation = new Vector3f();
        components = new Component[]{};
    }

    public GameObject(Mesh mesh) {
        this();
        this.mesh = mesh;
    }

    public GameObject(Mesh mesh, Component[] components) {
        position = new Vector3f();
        scale = 1;
        rotation = new Vector3f();
        this.mesh = mesh;
        this.components = components;
    }

    public Component[] getComponents() {return components;}

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
}