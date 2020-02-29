<p align="center">
  <img src="https://raw.githubusercontent.com/CremBluRay/CremBluRay.github.io/master/images/demo/voodoo/voodoo.png" alt="React Native Game Engine" height="120" />
</p>

# Voodoo Game Engine
A Java game engine built in OpenGL using [LWJGL](https://www.lwjgl.org/)

# Demo
<p float="left">
  <img src="https://raw.githubusercontent.com/CremBluRay/CremBluRay.github.io/master/images/demo/voodoo/voodoo-demo.gif" width="500" />
</p>

# Example Game Code
### DummyGame Class:
```java
public class DummyGame implements IGameLogic {

    private final Renderer renderer;

    private final Camera camera;

    private Scene scene;

    private Hud hud;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        scene = new Scene();

        // Setup GameObject
        float reflectance = 1f;
        Mesh mesh = OBJLoader.loadMesh("/models/cone.obj");
        Material material = new Material(new Vector4f(
                (float)(Math.random()),
                (float)(Math.random()),
                (float)(Math.random()),
                100
        ), reflectance);
        mesh.setMaterial(material);
        GameObject gameObject = new GameObject(mesh);
        gameObject.setPosition(-2f, 0, -3f);

        scene.setGameObjects(new GameObject[]{gameObject});

        // Setup SkyBox
        float skyBoxScale = 50.0f;
        SkyBox skyBox = new SkyBox("/models/skybox.obj", "textures/skybox.png");
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);

        // Create lights for the scene
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 1.0f;
        Vector3f lightPosition = new Vector3f(-1, 0, 0);
        sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));

        // Create Hud
        hud = new Hud("DEMO");
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {}

    @Override
    public void update(float interval, MouseInput mouseInput) {}

    @Override
    public void render(Window window) {
        hud.updateSize(window);
        renderer.render(window, camera, scene, hud);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        // GameObject Mesh cleanup
        Map<Mesh, List<GameObject>> mapMeshes = scene.getGameMeshes();
        for (Mesh mesh : mapMeshes.keySet()) {
            mesh.cleanUp();
        }
    }

}
```
### Hud Class:
```java
public class Hud implements IHud {

    private static final Font FONT = new Font("Arial", Font.PLAIN, 20);

    private static final String CHARSET = "ISO-8859-1";

    private final GameObject[] gameObjects;

    private final TextItem statusTextItem;

    public Hud(String statusText) throws Exception {
        FontTexture fontTexture = new FontTexture(FONT, CHARSET);
        this.statusTextItem = new TextItem(statusText, fontTexture);
        this.statusTextItem.getMesh().getMaterial().setAmbientColour(new Vector4f(1, 1, 1, 1));

        // Create list that holds the HUD
        gameObjects = new GameObject[]{statusTextItem};
    }

    public void setStatusText(String statusText) {
        this.statusTextItem.setText(statusText);
    }

    @Override
    public GameObject[] getGameObjects() {
        return gameObjects;
    }

    public void updateSize(Window window) {
        this.statusTextItem.setPosition(10f, window.getHeight() - 50f, 0);
    }
}
```
### Main Class:
```java
public class Main {

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame();
            GameEngine gameEng = new GameEngine("VOODOO", 600, 480, vSync, gameLogic);
            gameEng.run();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}
```
# Contributing
Please see [CONTRIBUTING.md](https://github.com/CremBluRay/voodoo/blob/master/CONTRIBUTING.md)
