package com.movlad.semviz.core.graphics.engine;

import org.joml.Matrix4f;

/**
 * Camera offering a perspective (real-life) view of the scene.
 */
public class PerspectiveCamera extends Camera {

	private float fov;
	private float aspect;
	private float near;
	private float far;
	
	public PerspectiveCamera(float fov, float aspect, float near, float far) {
		this.fov = fov;
		this.aspect = aspect;
		this.near = near;
		this.far = far;

		updateProjectionMatrix();
	}
	
	public void setAspect(float aspect) { 
		this.aspect = aspect; 
		
		updateProjectionMatrix(); 
	}
	
	@Override
	public boolean canZoom(float newZoom) {
		float zoomedFov = fov - (newZoom / ZOOM_FACTOR);
		
		return zoomedFov > 0 && zoomedFov < Math.PI;
	}
	
	@Override
	protected void updateProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		projectionMatrix = projectionMatrix.perspective(fov + (getZoom() / ZOOM_FACTOR), aspect, near, far);
	}
	
}
