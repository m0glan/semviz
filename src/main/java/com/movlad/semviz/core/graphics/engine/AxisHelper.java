package com.movlad.semviz.core.graphics.engine;

import org.joml.Vector3f;

import com.jogamp.opengl.GL4;
import com.movlad.semviz.core.graphics.BufferLayout;

/**
 * This is an {@code Object3d} that contains data for the {@code x}, {@code y} and {@code z} axis.
 */
public class AxisHelper extends Geometry {
	
    public AxisHelper(float scale) {
        this.setPosition(new Vector3f(0.0f, 0.0f, 0.0f));
        this.setTarget(new Vector3f(1.0f, 0.0f, 0.0f));
        this.setScale(scale);

        float[] data = {
                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 
                0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
        };

        this.data = data;

        BufferLayout layout = new BufferLayout();

        layout.push("position", GL4.GL_FLOAT, 3, false);
        layout.push("color", GL4.GL_UNSIGNED_BYTE, 3, false);

        this.layout = layout;
    }

    @Override
    public int getDrawingMode() { return GL4.GL_LINES; }
	
}
