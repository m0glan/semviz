package com.movlad.semviz.core.graphics;

import com.jogamp.opengl.GL3;

/**
 * Basic geometry grouping vertex data and buffer layout.
 */
public abstract class Geometry {

    private final BufferLayout layout;
    protected float[] data;

    protected Geometry() {
        this.layout = new BufferLayout();

        layout.push("position", GL3.GL_FLOAT, 3, false);
        layout.push("color", GL3.GL_UNSIGNED_BYTE, 3, false);
    }

    /**
     * Constructor.
     *
     * @param data is the OpenGL buffer vertex data
     */
    public Geometry(float[] data) {
        this.data = data;
        this.layout = new BufferLayout();

        layout.push("position", GL3.GL_FLOAT, 3, false);
        layout.push("color", GL3.GL_UNSIGNED_BYTE, 3, false);
    }

    public float[] getData() {
        return data;
    }

    public void setData(float[] data) {
        this.data = data;
    }

    public int getDataSize() {
        return data.length * Float.BYTES;
    }

    public int getVertexCount() {
        return data.length / layout.getAttributes().size();
    }

    public BufferLayout getLayout() {
        return layout;
    }

    /**
     * @return the primitives that OpenGL uses to draw this object
     */
    public abstract int getDrawingMode();

}
