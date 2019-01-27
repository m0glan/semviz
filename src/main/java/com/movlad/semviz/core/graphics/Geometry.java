package com.movlad.semviz.core.graphics;

import java.nio.FloatBuffer;

/**
 * Basic geometry grouping vertex data and buffer layout.
 */
public abstract class Geometry {

    private final BufferLayout layout;
    protected FloatBuffer data;

    protected Geometry(FloatBuffer data, BufferLayout layout) {
        this.data = data;
        this.layout = layout;
    }

    public FloatBuffer getData() {
        return data;
    }

    public int getDataSize() {
        return data.capacity() * Float.BYTES;
    }

    public int getVertexCount() {
        return data.capacity() / layout.rowLength();
    }

    public BufferLayout getLayout() {
        return layout;
    }

    /**
     * @return the primitives that OpenGL uses to draw this object
     */
    public abstract int getDrawingMode();

}
