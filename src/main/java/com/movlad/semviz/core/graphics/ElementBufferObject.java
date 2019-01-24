package com.movlad.semviz.core.graphics;

import com.jogamp.opengl.GL3;
import java.nio.Buffer;
import java.nio.IntBuffer;

/**
 * Also called index buffer object, it allows re-using points in a
 * {@code VertexBufferObject} multiple times to draw shapes composed of two or
 * more triangles having common points.
 */
public class ElementBufferObject {

    private final int id;
    private final GL3 gl;

    /**
     * Constructor.
     *
     * @param gl is the GL context
     * @param data is the buffer containing the sequences of points for drawing
     * elements
     * @param count is the number of indices
     * @param usage indicates OpenGL on how to draw the elements (<i>e.g.</i>
     * {@code GL_STATIC_DRAW})
     */
    public ElementBufferObject(GL3 gl, Buffer data, int count, int usage) {
        this.gl = gl;

        IntBuffer intBuffer = IntBuffer.allocate(1);

        this.gl.glGenBuffers(GL3.GL_ELEMENT_ARRAY_BUFFER, intBuffer);

        this.id = intBuffer.get(0);

        this.gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, id);
        this.gl.glBufferData(id, count * Integer.BYTES, data, usage);
    }

    public int getId() {
        return id;
    }

    /**
     * Binds this element buffer object.
     */
    public void bind() {
        gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, id);
    }

    /**
     * Binds the 0 element buffer object.
     */
    public void unbind() {
        gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    /**
     * Deletes this buffer object.
     */
    public void delete() {
        IntBuffer intBuffer = IntBuffer.allocate(1);

        intBuffer.put(0, id);

        gl.glDeleteBuffers(1, intBuffer);
    }

}
