package com.movlad.semviz.core.graphics;

import com.jogamp.opengl.GL;

/**
 * This is an {@code Object3d} that contains data for the {@code x}, {@code y}
 * and {@code z} axis.
 */
public class AxisHelper extends SceneObject {

    public AxisHelper() {
        super("AxisHelper");

        float[] data = new float[]{
            0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f
        };

        Geometry geometry = new Geometry(data) {
            @Override
            public int getDrawingMode() {
                return GL.GL_LINES;
            }
        };

        this.setGeometry(geometry);
    }

}
