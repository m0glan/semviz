package com.movlad.semviz.core.graphics.engine;

import com.jogamp.opengl.GL3;
import com.movlad.semviz.core.graphics.BufferLayout;
import org.joml.Vector3f;

/**
 * This is an {@code Object3d} that contains data for the {@code x}, {@code y}
 * and {@code z} axis.
 */
public class AxisHelper extends Geometry {

    public AxisHelper() {
        this.setPosition(new Vector3f(0.0f, 0.0f, 0.0f));
        this.setTarget(new Vector3f(1.0f, 0.0f, 0.0f));

        this.data = new float[]{
            0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f
        };

        layout = new BufferLayout();

        layout.push("position", GL3.GL_FLOAT, 3, false);
        layout.push("color", GL3.GL_UNSIGNED_BYTE, 3, false);
    }

    @Override
    public int getDrawingMode() {
        return GL3.GL_LINES;
    }

}
