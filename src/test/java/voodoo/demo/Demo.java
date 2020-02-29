package voodoo.demo;

import org.joml.Vector3f;
import org.joml.Vector4f;
import voodoo.demo.boid.Boid;
import voodoo.demo.boid.Boundary;
import voodoo.engine.*;
import voodoo.engine.gameObject.GameObject;
import voodoo.engine.gameObject.SkyBox;
import voodoo.engine.graph.Camera;
import voodoo.engine.light.DirectionalLight;
import voodoo.engine.light.SceneLight;
import voodoo.engine.model.Material;
import voodoo.engine.model.Mesh;
import voodoo.engine.util.OBJLoader;

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
                new Vector3f(-125, -75, -125));               // position

        float skyBoxScale = 50.0f;

        // Setup SkyBox
        SkyBox skyBox = new SkyBox("/models/skybox.obj", "textures/skybox.png");
        skyBox.setScale(skyBoxScale);
        skyBox.setPosition(-125, -75, -125);
        scene.setSkyBox(skyBox);

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
                    (float)(Math.random() * 50) + boundary.getPosition().x,
                    (float)(Math.random() * 50) + boundary.getPosition().y,
                    (float)(Math.random() * 50) + boundary.getPosition().z
            ));
            boid.setScale(0.5f);
            boids[i] = boid;
        }

        scene.setGameObjects(boids);

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
