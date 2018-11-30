package com.movlad.semviz.engine;

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

	@Override
	protected boolean performZoom(float zoom) {
		float newFov = fov - zoom;
		
		if (Math.toRadians(newFov) > 0 && Math.toRadians(newFov) < Math.PI) {
			fov = newFov;
			
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void updateProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		projectionMatrix = projectionMatrix.perspective(fov, aspect, near, far);
	}
	
}
