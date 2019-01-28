package com.movlad.semviz.core.graphics;

import java.nio.FloatBuffer;

/**
 * Basic geometry grouping vertex data and buffer layout.
 */
public abstract class Geometry {

    private final BufferLayout layout;
    protected FloatBuffer data;

    /**
     * @param data is a buffer containing the interleaved data for the geometry
     * @param layout is the layout of the data
     */
    protected Geometry(FloatBuffer data, BufferLayout layout) {
        this.data = data;
        this.layout = layout;
    }

    public FloatBuffer getBufferData() {
        return data;
    }

    public BufferLayout getLayout() {
        return layout;
    }

    /**
     * @return the size of the buffer data in bytes
     */
    public int sizeOfBufferData() {
        return data.capacity() * Float.BYTES;
    }

    /**
     * @return the number of vertices of the geometry
     */
    public int vertexCount() {
        return data.capacity() / layout.rowLength();
    }

    /**
     * @return the primitives that OpenGL uses to draw this object
     */
    public abstract int getDrawingMode();

}
