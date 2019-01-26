package com.movlad.semviz.core.graphics;

import com.jogamp.opengl.GL3;
import java.nio.IntBuffer;

class VertexArrayObject {

    private final int id;
    private int offset;

    /**
     * Constructor.
     *
     * @param gl is the current gl context
     */
    public VertexArrayObject(GL3 gl) {
        IntBuffer intBuffer = IntBuffer.allocate(1);

        gl.glGenVertexArrays(1, intBuffer);

        id = intBuffer.get(0);
    }

    public int getId() {
        return id;
    }

    /**
     * Binds this vertex array object.
     *
     * @param gl is the current gl context
     */
    public void bind(GL3 gl) {
        gl.glBindVertexArray(id);
    }

    /**
     * Binds the vertex array object 0.
     *
     * @param gl is the current gl context
     */
    public void unbind(GL3 gl) {
        gl.glBindVertexArray(0);
    }

    /**
     * Deletes this vertex array object.
     *
     * @param gl is the current gl context
     */
    public void delete(GL3 gl) {
        IntBuffer intBuffer = IntBuffer.allocate(1);

        intBuffer.put(0, id);

        gl.glDeleteVertexArrays(1, intBuffer);
    }

    /**
     * Stores the layout information for a given instance of
     * {@code VertexBufferObject}.
     *
     * @param gl is the current gl context
     * @param vbo is the vertex buffer object
     * @param layout is its layout
     */
    public void addBuffer(GL3 gl, VertexBufferObject vbo, BufferLayout layout) {
        bind(gl);
        vbo.bind(gl);

        for (int i = 0; i < layout.size(); i++) {
            BufferAttribute attribute = layout.get(i);

            gl.glEnableVertexAttribArray(i);
            gl.glVertexAttribPointer(i, attribute.getCount(), attribute.getType(), attribute.isNormalized(), layout.getStride(), offset);
            offset += attribute.getSize();
        }
    }

}
