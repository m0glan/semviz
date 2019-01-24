package com.movlad.semviz.core.graphics;

import com.jogamp.opengl.GL3;
import java.nio.Buffer;
import java.nio.IntBuffer;

/**
 * Stores the raw data concerning what is to be drawn by OpenGL, such as vertex
 * positions, colors, texture coordinates etc. .
 */
public class VertexBufferObject {

    private final int id;
    private final GL3 gl;

    /**
     * Constructor.
     *
     * @param gl is the context
     * @param data is the buffer containing the raw data
     * @param size is the size of the buffer in bytes
     * @param usage indicates OpenGL how the data should be drawn (<i>e.g.</i>
     * {@code GL_STATIC_DRAW)})
     */
    public VertexBufferObject(GL3 gl, Buffer data, int size, int usage) {
        this.gl = gl;

        IntBuffer intBuffer = IntBuffer.allocate(1);

        this.gl.glGenBuffers(1, intBuffer);

        this.id = intBuffer.get(0);

        this.gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, id);
        this.gl.glBufferData(GL3.GL_ARRAY_BUFFER, size, data, usage);
    }

    public int getId() {
        return id;
    }

    /**
     * Binds this vertex buffer object.
     */
    public void bind() {
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, id);
    }

    /**
     * Binds vertex buffer object 0.
     */
    public void unbind() {
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Deletes this vertex buffer object.
     */
    public void delete() {
        IntBuffer intBuffer = IntBuffer.allocate(1);

        intBuffer.put(0, id);

        gl.glDeleteBuffers(1, intBuffer);
    }

}
