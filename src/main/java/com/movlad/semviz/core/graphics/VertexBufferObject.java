package com.movlad.semviz.core.graphics;

import com.jogamp.opengl.GL3;
import java.nio.Buffer;
import java.nio.IntBuffer;

/**
 * Stores the raw data concerning what is to be drawn by OpenGL, such as vertex
 * positions, colors, texture coordinates etc. .
 */
class VertexBufferObject {

    private final int id;

    /**
     * Constructor.
     *
     * @param gl is the current gl context
     * @param data is the buffer containing the raw data
     * @param size is the size of the buffer in bytes
     * @param usage indicates OpenGL how the data should be drawn (<i>e.g.</i>
     * {@code GL_STATIC_DRAW)})
     */
    public VertexBufferObject(GL3 gl, Buffer data, int size, int usage) {
        IntBuffer intBuffer = IntBuffer.allocate(1);

        gl.glGenBuffers(1, intBuffer);

        this.id = intBuffer.get(0);

        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, id);
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, size, data, usage);
    }

    public int getId() {
        return id;
    }

    /**
     * Binds this vertex buffer object.
     *
     * @param gl is the current gl context
     */
    public void bind(GL3 gl) {
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, id);
    }

    /**
     * Binds vertex buffer object 0.
     *
     * @param gl is the current gl context
     */
    public void unbind(GL3 gl) {
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Deletes this vertex buffer object.
     */
    public void delete(GL3 gl) {
        IntBuffer intBuffer = IntBuffer.allocate(1);

        intBuffer.put(0, id);

        gl.glDeleteBuffers(1, intBuffer);
    }

}
