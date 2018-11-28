package com.movlad.semviz.core;

public class OrthogonalCamera extends Camera {

	private float left;
	private float right;
	private float bottom;
	private float top;
	private float near;
	private float far;
	
	public OrthogonalCamera(float left, float right, float bottom, float top, float near, float far) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		this.near = near;
		this.far = far;
		
		updateProjectionMatrix();
	}
	
	@Override
	protected void updateProjectionMatrix() {
		projectionMatrix = projectionMatrix.ortho(left, right, bottom, top, near, far);
	}

}
