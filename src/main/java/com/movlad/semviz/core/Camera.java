package com.movlad.semviz.core;

import org.joml.Matrix4f;

public abstract class Camera extends Object3d {

	protected Matrix4f projectionMatrix;
	
	public Camera() { this.projectionMatrix = new Matrix4f(); }
	
	public Matrix4f getProjectionMatrix() { return projectionMatrix; }
	
	protected abstract void updateProjectionMatrix();
	
}
