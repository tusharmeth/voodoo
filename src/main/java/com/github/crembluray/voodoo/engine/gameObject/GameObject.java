package com.github.crembluray.voodoo.engine.gameObject;

import com.github.crembluray.voodoo.engine.model.Mesh;
import org.joml.Vector3f;

import java.util.ArrayList;

public class GameObject {

    private final Mesh mesh;

    private final Vector3f position;

    private float scale;

    private final Vector3f rotation;

    private ArrayList<GameObject> children;

    private ArrayList<Component> components;

    private GameObject parent;

    public GameObject(Mesh mesh, ArrayList<GameObject> children, ArrayList<Component> components) {
        this.mesh = mesh;
        this.children = children;
        this.components = components;
        if(children != null) {
            for(GameObject child : this.children)
                child.setParent(this);
        }
        if(components != null) {
            for(Component component : this.components)
                component.setParent(this);
        }
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
    }

    public GameObject(Mesh mesh, ArrayList<GameObject> children) {
        this.mesh = mesh;
        this.children = children;
        this.components = new ArrayList<Component>();
        if(children != null) {
            for(GameObject child : this.children)
                child.setParent(this);
        }
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
    }

    public GameObject(Mesh mesh) {
        this.mesh = mesh;
        children = new ArrayList<GameObject>();
        components = new ArrayList<Component>();
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
    }

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

    public ArrayList<GameObject> getChildren() {
        return children;
    }

    public void addChild(GameObject child) {
        children.add(child);
        child.setParent(this);
    }

    public GameObject getChild(int index) {
        return children.get(index);
    }

    public void setChildren(ArrayList<GameObject> children) {
        this.children = children;
        for(GameObject child : this.children)
            child.setParent(this);
    }

    public Component getComponent(int index) {
        return components.get(index);
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }

    public void addComponent(Component component) {
        components.add(component);
        component.setParent(this);
    }

    public void setParent(GameObject parent) {
        this.parent = parent;
    }

    public GameObject getParent() {
        return parent;
    }

}
