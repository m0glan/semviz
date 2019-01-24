package com.movlad.semviz.core.graphics.engine;

import com.jogamp.opengl.GL3;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Generic representation of a 3d object.
 */
public class Object3d implements Iterable<Object3d> {

    private final Vector3f direction;
    private final Vector3f up;
    private final List<Object3d> children;
    private String id;
    private Vector3f position;
    private Vector3f worldUp;
    private Vector3f target;
    private Matrix4f matrixWorld;
    private boolean isVisible;

    public Object3d() {
        id = UUID.randomUUID().toString();

        position = new Vector3f(0.0f, 0.0f, 0.0f);

        worldUp = new Vector3f(0.0f, 0.0f, 1.0f);
        target = new Vector3f(1.0f, 0.0f, 0.0f);
        direction = new Vector3f();
        up = new Vector3f();

        setTarget(new Vector3f(1.0f, 0.0f, 0.0f));

        isVisible = true;

        children = new ArrayList<>();
    }

    public final String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
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

    /**
     * @return the primitives that OpenGL uses to draw this object
     */
    public int getDrawingMode() {
        return GL3.GL_TRIANGLES;
    }

    public final boolean isVisible() {
        return isVisible;
    }

    public final void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public final void add(Object3d object) {
        children.add(object);
    }

    public final void add(int i, Object3d object) {
        children.add(i, object);
    }

    public final void remove(Object3d object) {
        children.remove(object);
    }

    public final void remove(int i) {
        children.remove(i);
    }

    public final void remove(String id) {
        ListIterator<Object3d> it = children.listIterator();

        while (it.hasNext()) {
            if (it.next().getId().equals(id)) {
                it.remove();
            }
        }
    }

    public final Object3d get(int i) {
        return children.get(i);
    }

    public final int numChildren() {
        return children.size();
    }

    public final void clear() {
        children.clear();
    }

    @Override
    public final Iterator<Object3d> iterator() {
        return children.iterator();
    }

}
