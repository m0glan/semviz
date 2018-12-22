package com.movlad.semviz.core.graphics.engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.jogamp.opengl.GL4;

/**
 * Generic representation of a 3d object.
 */
public class Object3d implements Iterable<Object3d> {

	private String id;

	private float scale;
	
	private Vector3f position;

	// object vectors

	private Vector3f worldUp;
	private Vector3f target;
	private Vector3f direction;
	private Vector3f up;
	
	Matrix4f matrixWorld;

	private boolean isVisible;
	
	List<Object3d> children;

	public Object3d() {
		id = UUID.randomUUID().toString();

		scale = 1.0f;
		
		position = new Vector3f(0.0f, 0.0f, 0.0f);

		worldUp = new Vector3f(0.0f, 0.0f, 1.0f);
		target = new Vector3f(1.0f, 0.0f, 0.0f);
		direction = new Vector3f();
		up = new Vector3f();
		
		setTarget(new Vector3f(1.0f, 0.0f, 0.0f));
		
		isVisible = true;
		
		children = new ArrayList<>();
	}

	public String getId() { return id; }

	public float getScale() { return scale; }
	
	public void setScale(float scale) { 
		this.scale = scale; 
		
		updateVectors();
	}
	
	public Vector3f getPosition() { return position; }

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

	public Vector3f getWorldUp() { return worldUp; }

	public void setWorldUp(Vector3f worldUp) {
		this.worldUp = worldUp;
		
		updateVectors();
	}

	public Vector3f getVectorUp() { return up; }

	public Vector3f getDirection() { return direction; }
	
	public Vector3f getVectorRight() { 
		Vector3f right = new Vector3f();
		
		up.cross(direction, right);
		
		return right;
	}

	public Vector3f getTarget() { return target; }

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
		
		matrixWorld = new Matrix4f();
		
		matrixWorld.lookAt(position, target, up);
	}

	/**
	 * @return the matrix world based on the object vectors
	 */
	public Matrix4f getMatrixWorld() {
		Matrix4f matrixWorld = new Matrix4f();

		this.matrixWorld.scale(scale, scale, scale, matrixWorld);

		return matrixWorld;
	}
	
	public int getDrawingMode() { return GL4.GL_TRIANGLES; }

	public boolean isVisible() { return isVisible; }

	public void setVisible(boolean isVisible) { this.isVisible = isVisible; }
	
	public void add(Object3d object) { children.add(object); }
	
	public void remove(Object3d object) { children.remove(object); }
	
	public void remove(int i) { children.remove(i); }
	
	public Object3d get(int i) { return children.get(i); }
	
	public int numChildren() { return children.size(); }

	@Override
	public Iterator<Object3d> iterator() { return children.iterator(); }

}
