package com.movlad.semviz.core.graphics;

import java.util.UUID;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Generic representation of a 3d object.
 */
public class Object3d {

    private final Vector3f direction;
    private final Vector3f up;

    private final String id;
    private Vector3f position;
    private Vector3f worldUp;
    private Vector3f target;
    private Matrix4f matrixWorld;

    public Object3d() {
        id = UUID.randomUUID().toString();

        position = new Vector3f(0.0f, 0.0f, 0.0f);

        worldUp = new Vector3f(0.0f, 0.0f, 1.0f);
        target = new Vector3f(1.0f, 0.0f, 0.0f);
        direction = new Vector3f();
        up = new Vector3f();

        setTarget(new Vector3f(1.0f, 0.0f, 0.0f));
    }

    public final String getId() {
        return id;
    }

    public final Vector3f getPosition() {
        return position;
    }

    public final void setPosition(Vector3f position) {
        this.position = position;

        updateVectors();
    }

    public final void setX(float x) {
        position.x = x;

        updateVectors();
    }

    public final void setY(float y) {
        position.y = y;

        updateVectors();
    }

    public final void setZ(float z) {
        position.z = z;

        updateVectors();
    }

    /**
     * @return the vector that gives the up direction within the world space
     */
    public final Vector3f getWorldUp() {
        return worldUp;
    }

    /**
     * @param worldUp is the vector that gives the up direction within the world
     * space
     */
    public final void setWorldUp(Vector3f worldUp) {
        this.worldUp = worldUp;

        updateVectors();
    }

    /**
     * @return the up vector relative to this object (what "up" is from the
     * perspective of the object)
     */
    public final Vector3f getVectorUp() {
        return up;
    }

    /**
     * @return the vector that gives the direction the object is facing
     */
    public final Vector3f getDirection() {
        return direction;
    }

    /**
     * @return right local axis of the object within the world space
     */
    public final Vector3f getVectorRight() {
        Vector3f right = new Vector3f();

        up.cross(direction, right);

        return right;
    }

    /**
     * @return the point in space the object is looking at
     */
    public final Vector3f getTarget() {
        return target;
    }

    /**
     * Changes the target the object is facing and updates its vectors.
     *
     * @param target is the new target the object is facing towards
     */
    public final void setTarget(Vector3f target) {
        this.target = target;

        updateVectors();
    }

    public final void translate(Vector3f t) {
        position.add(t);
        updateVectors();
    }

    /**
     * Recalculates object vectors upon a change of target or a translation.
     */
    protected final void updateVectors() {
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
        return matrixWorld;
    }

}
