package voodoo.engine.gameObject;

public class Component {

    protected GameObject parent;

    public GameObject getParent() {
        return parent;
    }

    public void setParent(GameObject parent) {
        this.parent = parent;
    }
}
