package com.movlad.semviz.engine;

import org.joml.Matrix4f;

/**
 * Cube view camera.
 */
public class OrthographicCamera extends Camera {

	private float left;
	private float right;
	private float bottom;
	private float top;
	private float near;
	private float far;
	
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
	public OrthographicCamera(float left, float right, float bottom, float top, float near, float far) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		this.near = near;
		this.far = far;
		
		updateProjectionMatrix();
	}
	
	@Override
	protected boolean performZoom(float zoom) {
		float newLeft = left + zoom * 1.4f;
		float newRight = right - zoom * 1.4f;
		float newBottom = bottom + zoom * 0.9f;
		float newTop = top - zoom * 0.9f;
		
		if (newLeft < newRight && newBottom < newTop) {
			left = newLeft;
			right = newRight;
			bottom = newBottom;
			top = newTop;
			
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void updateProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		projectionMatrix = projectionMatrix.ortho(left, right, bottom, top, near, far);
	}

}
