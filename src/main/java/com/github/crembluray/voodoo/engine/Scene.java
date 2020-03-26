package com.github.crembluray.voodoo.engine;

import com.github.crembluray.voodoo.engine.graph.InstancedMesh;
import com.github.crembluray.voodoo.engine.graph.Mesh;
import com.github.crembluray.voodoo.engine.graph.particles.IParticleEmitter;
import com.github.crembluray.voodoo.engine.graph.weather.Fog;
import com.github.crembluray.voodoo.engine.items.GameObject;
import com.github.crembluray.voodoo.engine.items.SkyBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

    private final Map<Mesh, List<GameObject>> meshMap;

    private final Map<InstancedMesh, List<GameObject>> instancedMeshMap;

    private SkyBox skyBox;

    private SceneLight sceneLight;

    private Fog fog;

    private boolean renderShadows;
    
    private IParticleEmitter[] particleEmitters;

    public Scene() {
        meshMap = new HashMap();
        instancedMeshMap = new HashMap();
        fog = Fog.NOFOG;
        renderShadows = true;
    }

    public Map<Mesh, List<GameObject>> getGameMeshes() {
        return meshMap;
    }

    public Map<InstancedMesh, List<GameObject>> getGameInstancedMeshes() {
        return instancedMeshMap;
    }

    public boolean isRenderShadows() {
        return renderShadows;
    }

    public void setGameItems(GameObject[] gameObjects) {
        // Create a map of meshes to speed up rendering
        int numGameItems = gameObjects != null ? gameObjects.length : 0;
        for (int i = 0; i < numGameItems; i++) {
            GameObject gameObject = gameObjects[i];
            Mesh[] meshes = gameObject.getMeshes();
            for (Mesh mesh : meshes) {
                boolean instancedMesh = mesh instanceof InstancedMesh;
                List<GameObject> list = instancedMesh ? instancedMeshMap.get(mesh) : meshMap.get(mesh);
                if (list == null) {
                    list = new ArrayList<>();
                    if (instancedMesh) {
                        instancedMeshMap.put((InstancedMesh)mesh, list);
                    } else {
                        meshMap.put(mesh, list);
                    }
                }
                list.add(gameObject);
            }
        }
    }

    public void cleanup() {
        for (Mesh mesh : meshMap.keySet()) {
            mesh.cleanUp();
        }
        for (Mesh mesh : instancedMeshMap.keySet()) {
            mesh.cleanUp();
        }
        if (particleEmitters != null) {
            for (IParticleEmitter particleEmitter : particleEmitters) {
                particleEmitter.cleanup();
            }
        }
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }

    public void setRenderShadows(boolean renderShadows) {
        this.renderShadows = renderShadows;
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

    /**
     * @return the fog
     */
    public Fog getFog() {
        return fog;
    }

    /**
     * @param fog the fog to set
     */
    public void setFog(Fog fog) {
        this.fog = fog;
    }

    public IParticleEmitter[] getParticleEmitters() {
        return particleEmitters;
    }

    public void setParticleEmitters(IParticleEmitter[] particleEmitters) {
        this.particleEmitters = particleEmitters;
    }

}
