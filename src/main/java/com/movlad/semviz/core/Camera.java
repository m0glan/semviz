package com.movlad.semviz.core;

import org.joml.Matrix4f;

/**
 * Base camera class.
 */
public abstract class Camera extends Object3d {

	protected Matrix4f projectionMatrix;
	
	public Camera() { 
		this.projectionMatrix = new Matrix4f(); 
		this.setVisible(false); 
	}
	
	/**
	 * @return projection matrix (<i>e.g.</i> perspective, orthogonal etc.)
	 */
	public Matrix4f getProjectionMatrix() { return projectionMatrix; }
	
	protected abstract void updateProjectionMatrix();
	
}
