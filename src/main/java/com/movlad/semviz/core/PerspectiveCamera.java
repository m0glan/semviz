package com.movlad.semviz.core;

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
	protected void updateProjectionMatrix() {
		this.projectionMatrix = this.projectionMatrix.perspective(fov, aspect, near, far);
	}
	
}
