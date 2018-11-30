package com.movlad.semviz.core.graphics.engine;

import java.util.UUID;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Generic representation of a 3d object.
 */
public class Object3d {

	private String id;

	// world matrix and factors

	private Vector3f position;

	// object vectors

	private Vector3f worldUp;
	private Vector3f target;
	private Vector3f direction;
	private Vector3f up;

	private boolean visible;

	public Object3d() {
		id = UUID.randomUUID().toString();

		position = new Vector3f(0.0f, 0.0f, -5.0f);

		worldUp = new Vector3f(0.0f, 0.0f, 1.0f);
		target = new Vector3f();
		direction = new Vector3f();
		up = new Vector3f();

		updateVectors();
	}

	public String getId() {
		return id;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
		
		updateVectors();
	}
	
	public void setX(float x) {
		position.x = x;
		
		updateVectors();
	}
	
	public void setY(float y) {
		position.y = y;
		
		updateVectors();
	}
	
	public void setZ(float z) {
		position.z = z;
		
		updateVectors();
	}

	public Vector3f getWorldUp() {
		return worldUp;
	}

	public void setWorldUp(Vector3f worldUp) {
		this.worldUp = worldUp;
		updateVectors();
	}

	public Vector3f getUp() {
		return up;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public Vector3f getTarget() {
		return target;
	}

	/**
	 * Changes the target the object is facing and updates its vectors.
	 * 
	 * @param target is the new target the object is facing towards
	 */
	public void setTarget(Vector3f target) {
		this.target = target;

		updateVectors();
	}

	public void translate(Vector3f t) {
		position.add(t);
		updateVectors();
	};

	/**
	 * @return the matrix world based on the object vectors
	 */
	public Matrix4f getMatrixWorld() {
		Matrix4f matrixWorld = new Matrix4f();

		matrixWorld.lookAt(position, target, up);

		return matrixWorld;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Recalculates object vectors upon a change of target or a translation.
	 */
	protected void updateVectors() {
		target.sub(position, direction);
		direction.normalize();

		Vector3f right = new Vector3f();

		direction.cross(worldUp, right);
		right.normalize();

		direction.cross(right, up);
		up.normalize();
	}

}
