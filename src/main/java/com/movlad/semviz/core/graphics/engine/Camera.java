package com.movlad.semviz.core.graphics.engine;

import org.joml.Matrix4f;

/**
 * Base camera class.
 */
public abstract class Camera extends Object3d {

	protected static final float ZOOM_FACTOR = 5.0f;
	
	protected Matrix4f projectionMatrix;
	private float zoom;
	private float minZoom;
	private float maxZoom;
	
	public Camera() { 
            this.zoom = 1.0f;
            this.setVisible(false); 
	}
	
	protected float getZoom() { return zoom; }
	
	/**
	 * Zooms the camera by the given amount.
	 * 
	 * @param zoom is the amount to zoom by
	 */
	public final void zoom(float zoom) {
            float newZoom = this.zoom - zoom;

            if (newZoom >= minZoom && newZoom <= maxZoom) {
                if (canZoom(newZoom)) {
                        this.zoom = newZoom;

                        updateProjectionMatrix();
                }
            }
	}
	
	/**
	 * @param newZoom is the potential new value of zoom
	 * 
	 * @return true if the camera can zoom
	 */
	protected abstract boolean canZoom(float newZoom);
	
	public void setMinZoom(float minZoom) {
            this.minZoom = minZoom;
	}
	
	public void setMaxZoom(float maxZoom) {
            this.maxZoom = maxZoom;
	}
	
	/**
	 * @return projection matrix (<i>e.g.</i> perspective, orthogonal etc.)
	 */
	public Matrix4f getProjectionMatrix() { return projectionMatrix; }
	
	protected abstract void updateProjectionMatrix();
	
}
