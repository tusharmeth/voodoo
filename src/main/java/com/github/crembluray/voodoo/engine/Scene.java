package com.github.crembluray.voodoo.engine;

import com.github.crembluray.voodoo.engine.gameObject.GameObject;
import com.github.crembluray.voodoo.engine.gameObject.SkyBox;
import com.github.crembluray.voodoo.engine.light.SceneLight;
import com.github.crembluray.voodoo.engine.model.Mesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

    private Map<Mesh, List<GameObject>> meshMap;

    private SkyBox skyBox;

    private SceneLight sceneLight;

    private GameObject[] gameObjects;

    public Scene() {
        meshMap = new HashMap();
    }

    public void setGameObjects(GameObject[] GameObjects) {
        gameObjects = GameObjects;
        int numGameItems = GameObjects != null ? GameObjects.length : 0;
        for (int i = 0; i < numGameItems; i++) {
            GameObject gameObject = GameObjects[i];
            Mesh mesh = gameObject.getMesh();
            List<GameObject> list = meshMap.computeIfAbsent(mesh, k -> new ArrayList<>());
            list.add(gameObject);
        }
    }

    public GameObject[] getGameObjects() {return gameObjects;}

    public Map<Mesh, List<GameObject>> getGameMeshes() {
        return meshMap;
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }

    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }

    public SceneLight getSceneLight() {
        return sceneLight;
    }

    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }

}