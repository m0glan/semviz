package com.movlad.semviz.core.graphics;

import org.joml.Matrix4f;

/**
 * Cube view camera.
 */
public class OrthographicCamera extends Camera {

    private float left;
    private float right;
    private float bottom;
    private float top;

    public OrthographicCamera(float near, float far) {
        super(near, far);
    }

    /**
     * Constructor.
     *
     * @param left is the frustrum left
     * @param right is the frustrum right
     * @param bottom is the frustum bottom
     * @param top is the frustum top
     * @param near is the minimum distance at which the camera can see objects
     * @param far is the maximum distance at which the camera can see objects
     */
    public OrthographicCamera(float left, float right, float bottom, float top,
            float near, float far) {
        super(near, far);

        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;

        updateProjectionMatrix();
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public void setFrustum(float left, float right, float bottom, float top) {
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
    }

    @Override
    public final void zoom(float zoom) {
        float zoomedLeft = left / zoom;
        float zoomedRight = right / zoom;
        float zoomedBottom = bottom / zoom;
        float zoomedTop = top / zoom;

        if (zoomedLeft < zoomedRight && zoomedBottom < zoomedTop) {
            this.zoom = zoom;
        }
    }

    @Override
    public final void updateProjectionMatrix() {
        projectionMatrix = new Matrix4f();
        projectionMatrix = projectionMatrix.ortho(left / zoom, right / zoom,
                bottom / zoom, top / zoom, near, far);
    }

}
