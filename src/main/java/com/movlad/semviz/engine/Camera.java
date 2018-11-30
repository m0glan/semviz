package com.movlad.semviz.engine;

import org.joml.Matrix4f;

/**
 * Base camera class.
 */
public abstract class Camera extends Object3d {

	protected Matrix4f projectionMatrix;
	int zoomLevel;
	int minZoomLevel;
	int maxZoomLevel;
	
	public Camera() { 
		this.zoomLevel = 0;
		this.setVisible(false); 
	}
	
	/**
	 * Zooms the camera by the given amount.
	 * 
	 * @param zoom is the amount to zoom by
	 */
	public final void zoom(float zoom) {
		int newZoomLevel = zoomLevel;
		
		if (zoom < 0) {
			newZoomLevel--;
		} else {
			newZoomLevel++;
		}
		
		if (newZoomLevel >= minZoomLevel && newZoomLevel <= maxZoomLevel) {
			if (performZoom(zoom)) {
				zoomLevel = newZoomLevel;
				
				updateProjectionMatrix();
			}
		}
		
	}
	
	/**
	 * Actual zoom implementation, varying from camera type to camera type.
	 * 
	 * @param zoom is the amount by which to zoom
	 * @return true if a zoom has been performed
	 */
	protected abstract boolean performZoom(float zoom);
	
	public void setMinZoomLevel(int minZoomLevel) {
		if (minZoomLevel > 0) {
			this.minZoomLevel = 0;
		} else {
			this.minZoomLevel = minZoomLevel;
		}
	}
	
	public void setMaxZoomLevel(int maxZoomLevel) {
		if (maxZoomLevel < 0) {
			this.maxZoomLevel = 0;
		} else {
			this.maxZoomLevel = maxZoomLevel;
		}
	}
	
	/**
	 * @return projection matrix (<i>e.g.</i> perspective, orthogonal etc.)
	 */
	public Matrix4f getProjectionMatrix() { return projectionMatrix; }
	
	protected abstract void updateProjectionMatrix();
	
}
