package com.movlad.semviz.core.graphics.engine;

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
	
	public float getLeft() { return left; }
	
	public void setLeft(float left) {
		this.left = left;
		
		updateProjectionMatrix();
	}
	
	public float getRight() { return right; }
	
	public void setRight(float right) {
		this.right = right;
		
		updateProjectionMatrix();
	}
	
	public float getBottom() { return bottom; }
	
	public void setBottom(float bottom) {
		this.bottom = bottom;
		
		updateProjectionMatrix();
	}
	
	public float getTop() { return top; }
	
	public void setTop(float top) {
		this.top = top;
		
		updateProjectionMatrix();
	}
	
	@Override
	protected final boolean canZoom(float newZoom) {
		float zoomedLeft = (left * newZoom) / ZOOM_FACTOR;
		float zoomedRight = (right * newZoom) / ZOOM_FACTOR;
		float zoomedBottom = (bottom * newZoom) / ZOOM_FACTOR;
		float zoomedTop = (top * newZoom) / ZOOM_FACTOR;
		
		return zoomedLeft < zoomedRight && zoomedBottom < zoomedTop;
	}
	
	@Override
	protected void updateProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		projectionMatrix = projectionMatrix.ortho((left * getZoom()) / ZOOM_FACTOR, (right * getZoom()) / ZOOM_FACTOR, 
				(bottom * getZoom()) / ZOOM_FACTOR, (top * getZoom()) / ZOOM_FACTOR, near, far);
	}

}
