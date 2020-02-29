package voodoo.hud;

import voodoo.engine.gameObject.GameObject;

public interface IHud {

    GameObject[] getGameObjects();

    default void cleanup() {
        GameObject[] gameObjects = getGameObjects();
        for (GameObject gameObject : gameObjects) {
            gameObject.getMesh().cleanUp();
        }
    }
}
