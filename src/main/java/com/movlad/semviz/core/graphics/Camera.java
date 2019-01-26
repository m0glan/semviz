package com.movlad.semviz.core.graphics;

import org.joml.Matrix4f;

/**
 * Base camera class.
 */
public abstract class Camera extends Object3d {

    public static final float DEFAULT_ZOOM = 1.0f;

    protected Matrix4f projectionMatrix;

    protected float near;
    protected float far;
    protected float zoom;

    public Camera(float near, float far) {
        zoom = DEFAULT_ZOOM;

        this.near = near;
        this.far = far;
    }

    public final float getZoom() {
        return zoom;
    }

    /**
     * Zooms the camera by the given amount.
     *
     * @param zoom is the amount to zoom by
     */
    public abstract void zoom(float zoom);

    /**
     * @return projection matrix (<i>e.g.</i> perspective, orthogonal etc.)
     */
    public final Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public abstract void updateProjectionMatrix();

}
