package com.github.crembluray.voodoo.demo;

import com.github.crembluray.voodoo.demo.boid.Boid;
import com.github.crembluray.voodoo.demo.boid.Boundary;
import com.github.crembluray.voodoo.engine.*;
import com.github.crembluray.voodoo.engine.gameObject.GameObject;
import com.github.crembluray.voodoo.engine.gameObject.SkyBox;
import com.github.crembluray.voodoo.engine.graph.Camera;
import com.github.crembluray.voodoo.engine.light.DirectionalLight;
import com.github.crembluray.voodoo.engine.light.SceneLight;
import com.github.crembluray.voodoo.engine.model.Material;
import com.github.crembluray.voodoo.engine.model.Mesh;
import com.github.crembluray.voodoo.engine.util.OBJLoader;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;
import java.util.Map;

public class Demo implements IGameLogic {

    private final Renderer renderer;

    private final Camera camera;

    private Scene scene;

    private Hud hud;

    public Demo() {
        renderer = new Renderer();
        camera = new Camera();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        scene = new Scene();

        // Setup GameObject
        float reflectance = 1f;
        Boundary boundary = new Boundary(
                new Vector3f(-50.0f, -50.0f, -50.0f), // min
                new Vector3f(50.0f, 50.0f, 50.0f),    // max
                new Vector3f(0, 0, 0));               // position

        // Create boids
        int numBoids = 500;
        Boid[] boids = new Boid[numBoids];
        for(int i = 0; i < numBoids; i++) {
            Mesh mesh = OBJLoader.loadMesh("/models/cone.obj");
            Material material = new Material(new Vector4f(
                    (float)(Math.random()),
                    (float)(Math.random()),
                    (float)(Math.random()),
                    100
            ), reflectance);
            mesh.setMaterial(material);
            Boid boid = new Boid(mesh, boundary, new Vector3f(
                    (float)(Math.random() * 50),
                    (float)(Math.random() * 50),
                    (float)(Math.random() * 50)
            ));
            boid.setScale(0.5f);
            boids[i] = boid;
        }

        float skyBoxScale = 50.0f;

        scene.setGameObjects(boids);

        // Setup SkyBox
        SkyBox skyBox = new SkyBox("/models/skybox.obj", "textures/skybox.png");
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);

        // SetupLights
        setupLights();

        // Create HUD
        hud = new Hud("DEMO");

        // Setup camera rotation
        camera.setRotation(22.2f, -43.19f, 0f);
    }

    private void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 1.0f;
        Vector3f lightPosition = new Vector3f(-1, 0, 0);
        sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {}

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update boid position
        for(Boid boid : Boid.getBoids()) {
            boid.update();
        }
    }

    @Override
    public void render(Window window) {
        hud.updateSize(window);
        renderer.render(window, camera, scene, hud);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        Map<Mesh, List<GameObject>> mapMeshes = scene.getGameMeshes();
        for (Mesh mesh : mapMeshes.keySet()) {
            mesh.cleanUp();
        }
        hud.cleanup();
    }

}
