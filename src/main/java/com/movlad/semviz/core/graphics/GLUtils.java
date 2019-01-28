package com.movlad.semviz.core.graphics;

import com.jogamp.opengl.GL3;
import com.movlad.semviz.core.math.geometry.TransformationUtils;
import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

/**
 * Offers some tools to speed-up certain OpenGL routines.
 */
public class GLUtils {

    /**
     * Configures vertex buffer and vertex array objects from a {@code Geometry}
     * object.
     *
     * @param gl is the gl context
     * @param geometry is the geometry containing the vertex information
     * @param vbo is the vertex buffer object associated with the vertex data
     * @param vao is the vertex array object associated with the vertex buffer
     * object
     */
    public static void geometryToGL(GL3 gl, Geometry geometry, int vbo, int vao) {
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo);
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, geometry.sizeOfBufferData(), geometry.getBufferData(),
                GL3.GL_STATIC_DRAW);

        gl.glBindVertexArray(vao);

        BufferLayout layout = geometry.getLayout();
        int offset = 0;

        for (int i = 0; i < layout.size(); i++) {
            /**
             * For each vertex attribute, the associate attribute array is
             * enabled; then the pointer is generated.
             */

            BufferAttribute attribute = layout.get(i);

            gl.glEnableVertexAttribArray(i);
            gl.glVertexAttribPointer(i, attribute.getSize(), attribute.getType(),
                    attribute.getNormalized(), layout.getStride(), offset);

            offset += attribute.sizeInBytes();
        }
    }

    /**
     * Sets a Matrix4fv uniform in a program to the given value.
     *
     * @param gl is the gl context
     * @param program is the program containing the uniform
     * @param name is the name of the uniform
     * @param matrix is the value to set the uniform to
     */
    public static void setUniformMatrix4fv(GL3 gl, int program, String name, Matrix4f matrix) {
        int location = gl.glGetUniformLocation(program, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);

        matrix.get(buffer);
        gl.glUniformMatrix4fv(location, 1, false, buffer);
    }

    /**
     * Maps {@code rgb} values from [0,255] to [0,1]
     *
     * @param r is the red component
     * @param g is the green component
     * @param b is the blue component
     * @return an array containing the normalized {@code rgb} components
     */
    public static final float[] normalizeColor(float r, float g, float b) {
        float normalizedR = TransformationUtils.map(r, 0.0f, 255.0f, 0.0f, 1.0f);
        float normalizedG = TransformationUtils.map(g, 0.0f, 255.0f, 0.0f, 1.0f);
        float normalizedB = TransformationUtils.map(b, 0.0f, 255.0f, 0.0f, 1.0f);

        return new float[]{normalizedR, normalizedG, normalizedB};
    }

}
