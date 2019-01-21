/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.core.graphics;

import com.movlad.semviz.core.math.geometry.TransformationUtils;

public class GraphicsUtils {

    public static final int fillBufferSection(float[] buffer, int offset, float... data) {
        for (int i = 0; i < data.length; i++) {
            buffer[offset + i] = data[i];
        }

        return offset + data.length;
    }

    public static final float[] normalizeRGB(float r, float g, float b) {
        float normalizedR = TransformationUtils.map(r, 0.0f, 255.0f, 0.0f, 1.0f);
        float normalizedG = TransformationUtils.map(g, 0.0f, 255.0f, 0.0f, 1.0f);
        float normalizedB = TransformationUtils.map(b, 0.0f, 255.0f, 0.0f, 1.0f);

        return new float[]{normalizedR, normalizedG, normalizedB};
    }

    public static final float[] rgbFromNormals(float n0, float n1, float n2) {
        return new float[]{Math.abs(n0), Math.abs(n1), Math.abs(n2)};
    }

}
