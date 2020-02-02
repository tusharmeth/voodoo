package com.github.crembluray.voodoo.game;

import com.github.crembluray.voodoo.engine.gameObject.GameObject;
import com.github.crembluray.voodoo.engine.IGameLogic;
import com.github.crembluray.voodoo.engine.MouseInput;
import com.github.crembluray.voodoo.engine.Window;
import com.github.crembluray.voodoo.engine.graph.Camera;
import com.github.crembluray.voodoo.engine.light.DirectionalLight;
import com.github.crembluray.voodoo.engine.light.PointLight;
import com.github.crembluray.voodoo.engine.light.SpotLight;
import com.github.crembluray.voodoo.engine.model.Material;
import com.github.crembluray.voodoo.engine.model.Mesh;
import com.github.crembluray.voodoo.engine.model.Texture;
import com.github.crembluray.voodoo.engine.physics.AABB;
import com.github.crembluray.voodoo.engine.util.OBJLoader;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private GameObject[] gameObjects;

    private Vector3f ambientLight;

    private PointLight[] pointLightList;

    private SpotLight[] spotLightList;

    private DirectionalLight directionalLight;

    private float lightAngle;

    private static final float CAMERA_POS_STEP = 0.05f;

    private float spotAngle = 0;

    private float spotInc = 1;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
        lightAngle = -90;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        float reflectance = 1f;
        Mesh mesh = OBJLoader.loadMesh("/models/bunny.obj");
        Material material = new Material(new Vector4f(0.2f, 0.5f, 0.5f, 1.0f), reflectance);

        // Assign a material to the mesh
        mesh.setMaterial(material);
        GameObject gameObject = new GameObject(mesh);
        gameObject.setScale(0.5f);
        gameObject.setPosition(0, 0, -2);

        // Child mesh
        Mesh childMesh = OBJLoader.loadMesh("/models/cube.obj");
        Texture texture = new Texture("textures/grassblock.png");
        Material childMaterial = new Material(texture, reflectance);
        childMesh.setMaterial(childMaterial);

        // Add child to gameObject
        GameObject child = new GameObject(childMesh);
        child.setScale(0.5f);
        child.setPosition(2.0f, 0.3f, -2.0f);

        // Add AABB to gameObject
        AABB aabb = new AABB();
        aabb.calcAABB("/models/bunny.obj");
        gameObject.addComponent(aabb);
        aabb.setPosition(aabb.getParent().getPosition());
//        System.out.println(aabb.getMin().x + aabb.getPosition().x);
//        System.out.println(aabb.getMin().y + aabb.getPosition().y);
//        System.out.println(aabb.getMin().z + aabb.getPosition().z);
//        System.out.println(aabb.getMax().x + aabb.getPosition().x);
//        System.out.println(aabb.getMax().y + aabb.getPosition().y);
//        System.out.println(aabb.getMax().z + aabb.getPosition().z);
//
//        System.out.println(aabb.isPointInsideAABB(new Vector3f(-1.7f, 1f, -2f)));

        gameObject.setChildren(new ArrayList<GameObject>());
        gameObject.addChild(child);

        gameObjects = new GameObject[]{gameObject};

        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);

        // Point Light
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 1.0f;
        PointLight pointLight = new PointLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);
        pointLightList = new PointLight[]{pointLight};

        // Spot Light
        lightPosition = new Vector3f(0, 0.0f, 10f);
        pointLight = new PointLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
        att = new PointLight.Attenuation(0.0f, 0.0f, 0.02f);
        pointLight.setAttenuation(att);
        Vector3f coneDir = new Vector3f(0, 0, -1);
        float cutoff = (float) Math.cos(Math.toRadians(140));
        SpotLight spotLight = new SpotLight(pointLight, coneDir, cutoff);
        spotLightList = new SpotLight[]{spotLight, new SpotLight(spotLight)};

        lightPosition = new Vector3f(-1, 0, 0);
        directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
        float lightPos = spotLightList[0].getPointLight().getPosition().z;
        if (window.isKeyPressed(GLFW_KEY_N)) {
            this.spotLightList[0].getPointLight().getPosition().z = lightPos + 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_M)) {
            this.spotLightList[0].getPointLight().getPosition().z = lightPos - 0.1f;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        // Update spot light direction
        spotAngle += spotInc * 0.05f;
        if (spotAngle > 2) {
            spotInc = -1;
        } else if (spotAngle < -2) {
            spotInc = 1;
        }
        double spotAngleRad = Math.toRadians(spotAngle);
        Vector3f coneDir = spotLightList[0].getConeDirection();
        coneDir.y = (float) Math.sin(spotAngleRad);

        // Update directional light direction, intensity and colour
        lightAngle += 1.1f;
        if (lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 360) {
                lightAngle = -90;
            }
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameObjects, ambientLight, pointLightList, spotLightList, directionalLight);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameObject gameObject : gameObjects) {
            gameObject.getMesh().cleanUp();
        }
    }

}