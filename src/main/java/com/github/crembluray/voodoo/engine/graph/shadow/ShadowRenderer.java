package com.github.crembluray.voodoo.engine.graph.shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.crembluray.voodoo.engine.items.GameObject;
import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL30.*;
import com.github.crembluray.voodoo.engine.Scene;
import com.github.crembluray.voodoo.engine.SceneLight;
import com.github.crembluray.voodoo.engine.Utils;
import com.github.crembluray.voodoo.engine.Window;
import com.github.crembluray.voodoo.engine.graph.Camera;
import com.github.crembluray.voodoo.engine.graph.InstancedMesh;
import com.github.crembluray.voodoo.engine.graph.Mesh;
import com.github.crembluray.voodoo.engine.graph.Renderer;
import com.github.crembluray.voodoo.engine.graph.ShaderProgram;
import com.github.crembluray.voodoo.engine.graph.Transformation;
import com.github.crembluray.voodoo.engine.graph.anim.AnimGameObject;
import com.github.crembluray.voodoo.engine.graph.anim.AnimatedFrame;
import com.github.crembluray.voodoo.engine.graph.lights.DirectionalLight;

public class ShadowRenderer {

    public static final int NUM_CASCADES = 3;

    public static final float[] CASCADE_SPLITS = new float[]{Window.Z_FAR / 20.0f, Window.Z_FAR / 10.0f, Window.Z_FAR};

    private ShaderProgram depthShaderProgram;

    private List<ShadowCascade> shadowCascades;

    private ShadowBuffer shadowBuffer;

    private final List<GameObject> filteredItems;

    public ShadowRenderer() {
        filteredItems = new ArrayList<>();
    }

    public void init(Window window) throws Exception {
        shadowBuffer = new ShadowBuffer();
        shadowCascades = new ArrayList<>();

        setupDepthShader();

        float zNear = Window.Z_NEAR;
        for (int i = 0; i < NUM_CASCADES; i++) {
            ShadowCascade shadowCascade = new ShadowCascade(zNear, CASCADE_SPLITS[i]);
            shadowCascades.add(shadowCascade);
            zNear = CASCADE_SPLITS[i];
        }
    }

    public List<ShadowCascade> getShadowCascades() {
        return shadowCascades;
    }

    public void bindTextures(int start) {
        this.shadowBuffer.bindTextures(start);
    }

    private void setupDepthShader() throws Exception {
        depthShaderProgram = new ShaderProgram();
        depthShaderProgram.createVertexShader(Utils.loadResource("/shaders/depth_vertex.vs"));
        depthShaderProgram.createFragmentShader(Utils.loadResource("/shaders/depth_fragment.fs"));
        depthShaderProgram.link();

        depthShaderProgram.createUniform("isInstanced");
        depthShaderProgram.createUniform("modelNonInstancedMatrix");
        depthShaderProgram.createUniform("lightViewMatrix");
        depthShaderProgram.createUniform("jointsMatrix");
        depthShaderProgram.createUniform("orthoProjectionMatrix");
    }

    private void update(Window window, Matrix4f viewMatrix, Scene scene) {
        SceneLight sceneLight = scene.getSceneLight();
        DirectionalLight directionalLight = sceneLight != null ? sceneLight.getDirectionalLight() : null;
        for (int i = 0; i < NUM_CASCADES; i++) {
            ShadowCascade shadowCascade = shadowCascades.get(i);
            shadowCascade.update(window, viewMatrix, directionalLight);
        }
    }

    public void render(Window window, Scene scene, Camera camera, Transformation transformation, Renderer renderer) {
        update(window, camera.getViewMatrix(), scene);

        // Setup view port to match the texture size
        glBindFramebuffer(GL_FRAMEBUFFER, shadowBuffer.getDepthMapFBO());
        glViewport(0, 0, ShadowBuffer.SHADOW_MAP_WIDTH, ShadowBuffer.SHADOW_MAP_HEIGHT);
        glClear(GL_DEPTH_BUFFER_BIT);

        depthShaderProgram.bind();

        // Render scene for each cascade map
        for (int i = 0; i < NUM_CASCADES; i++) {
            ShadowCascade shadowCascade = shadowCascades.get(i);

            depthShaderProgram.setUniform("orthoProjectionMatrix", shadowCascade.getOrthoProjMatrix());
            depthShaderProgram.setUniform("lightViewMatrix", shadowCascade.getLightViewMatrix());

            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, shadowBuffer.getDepthMapTexture().getIds()[i], 0);
            glClear(GL_DEPTH_BUFFER_BIT);

            renderNonInstancedMeshes(scene, transformation);

            renderInstancedMeshes(scene, transformation);
        }

        // Unbind
        depthShaderProgram.unbind();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void renderNonInstancedMeshes(Scene scene, Transformation transformation) {
        depthShaderProgram.setUniform("isInstanced", 0);

        // Render each mesh with the associated game Items
        Map<Mesh, List<GameObject>> mapMeshes = scene.getGameMeshes();
        for (Mesh mesh : mapMeshes.keySet()) {
            mesh.renderList(mapMeshes.get(mesh), (GameObject gameObject) -> {
                Matrix4f modelMatrix = transformation.buildModelMatrix(gameObject);
                depthShaderProgram.setUniform("modelNonInstancedMatrix", modelMatrix);
                if (gameObject instanceof AnimGameObject) {
                    AnimGameObject animGameItem = (AnimGameObject) gameObject;
                    AnimatedFrame frame = animGameItem.getCurrentAnimation().getCurrentFrame();
                    depthShaderProgram.setUniform("jointsMatrix", frame.getJointMatrices());
                }
            }
            );
        }
    }

    private void renderInstancedMeshes(Scene scene, Transformation transformation) {
        depthShaderProgram.setUniform("isInstanced", 1);

        // Render each mesh with the associated game Items
        Map<InstancedMesh, List<GameObject>> mapMeshes = scene.getGameInstancedMeshes();
        for (InstancedMesh mesh : mapMeshes.keySet()) {
            filteredItems.clear();
            for (GameObject gameObject : mapMeshes.get(mesh)) {
                if (gameObject.isInsideFrustum()) {
                    filteredItems.add(gameObject);
                }
            }
            bindTextures(GL_TEXTURE2);

            mesh.renderListInstanced(filteredItems, transformation, null);
        }
    }

    public void cleanup() {
        if (shadowBuffer != null) {
            shadowBuffer.cleanup();
        }
        if (depthShaderProgram != null) {
            depthShaderProgram.cleanup();
        }
    }

}
