package com.movlad.semviz.core.graphics.engine;

import org.joml.Matrix4f;

/**
 * Camera offering a perspective (real-life) view of the scene.
 */
public class PerspectiveCamera extends Camera {

    private float fov;

    private float aspect;

    public PerspectiveCamera(float fov, float aspect, float near, float far) {
        this.fov = fov;
        this.aspect = aspect;

        setNear(near);
        setFar(far);

        updateProjectionMatrix();
    }

    public final float getFov() {
        return fov;
    }

    public final void setFov(float fov) {
        this.fov = fov;
    }

    public final float getAspect() {
        return aspect;
    }

    public final void setAspect(float aspect) {
        this.aspect = aspect;
    }

    @Override
    public final void zoom(float zoom) {
        if ((fov - zoom + 1.0f) > 0 && (fov - zoom + DEFAULT_ZOOM) < Math.PI) {
            this.zoom = zoom;
        }
    }

    @Override
    public final void updateProjectionMatrix() {
        projectionMatrix = new Matrix4f();
        projectionMatrix = projectionMatrix.perspective(fov - zoom + DEFAULT_ZOOM, aspect, getNear(), getFar());
    }

}
