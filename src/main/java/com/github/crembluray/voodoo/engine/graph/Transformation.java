package com.github.crembluray.voodoo.engine.graph;

import com.github.crembluray.voodoo.engine.items.GameObject;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transformation {

    private final Matrix4f modelMatrix;
    
    private final Matrix4f modelViewMatrix;

    private final Matrix4f modelLightViewMatrix;

    private final Matrix4f lightViewMatrix;

    private final Matrix4f ortho2DMatrix;

    private final Matrix4f orthoModelMatrix;

    public Transformation() {
        modelMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();
        modelLightViewMatrix = new Matrix4f();
        ortho2DMatrix = new Matrix4f();
        orthoModelMatrix = new Matrix4f();
        lightViewMatrix = new Matrix4f();
    }

    public Matrix4f getLightViewMatrix() {
        return lightViewMatrix;
    }

    public void setLightViewMatrix(Matrix4f lightViewMatrix) {
        this.lightViewMatrix.set(lightViewMatrix);
    }

    public Matrix4f updateLightViewMatrix(Vector3f position, Vector3f rotation) {
        return updateGenericViewMatrix(position, rotation, lightViewMatrix);
    }

    public static  Matrix4f updateGenericViewMatrix(Vector3f position, Vector3f rotation, Matrix4f matrix) {
        // First do the rotation so camera rotates over its position
        return matrix.rotationX((float)Math.toRadians(rotation.x))
                     .rotateY((float)Math.toRadians(rotation.y))
                     .translate(-position.x, -position.y, -position.z);
    }

    public final Matrix4f getOrtho2DProjectionMatrix(float left, float right, float bottom, float top) {
        return ortho2DMatrix.setOrtho2D(left, right, bottom, top);
    }
    
    public Matrix4f buildModelMatrix(GameObject gameObject) {
        Quaternionf rotation = gameObject.getRotation();
        return modelMatrix.translationRotateScale(
                gameObject.getPosition().x, gameObject.getPosition().y, gameObject.getPosition().z,
                rotation.x, rotation.y, rotation.z, rotation.w,
                gameObject.getScale(), gameObject.getScale(), gameObject.getScale());
    }

    public Matrix4f buildModelViewMatrix(GameObject gameObject, Matrix4f viewMatrix) {
        return buildModelViewMatrix(buildModelMatrix(gameObject), viewMatrix);
    }
    
    public Matrix4f buildModelViewMatrix(Matrix4f modelMatrix, Matrix4f viewMatrix) {
        return viewMatrix.mulAffine(modelMatrix, modelViewMatrix);
    }

    public Matrix4f buildModelLightViewMatrix(GameObject gameObject, Matrix4f lightViewMatrix) {
        return buildModelViewMatrix(buildModelMatrix(gameObject), lightViewMatrix);
    }

    public Matrix4f buildModelLightViewMatrix(Matrix4f modelMatrix, Matrix4f lightViewMatrix) {
        return lightViewMatrix.mulAffine(modelMatrix, modelLightViewMatrix);
    }

    public Matrix4f buildOrthoProjModelMatrix(GameObject gameObject, Matrix4f orthoMatrix) {
        return orthoMatrix.mulOrthoAffine(buildModelMatrix(gameObject), orthoModelMatrix);
    }
}
