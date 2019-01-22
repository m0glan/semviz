package com.movlad.semviz.core.graphics;

import com.movlad.semviz.core.math.geometry.TransformationUtils;

/**
 * Offers some tools allowing to manipulated some graphical data.
 */
public class GraphicsUtils {

    /**
     * Maps {@code rgb} values from [0,255] to [0,1]
     *
     * @param r is the red component
     * @param g is the green component
     * @param b is the blue component
     * @return an array containing the normalized {@code rgb} components
     */
    public static final float[] normalizeRGB(float r, float g, float b) {
        float normalizedR = TransformationUtils.map(r, 0.0f, 255.0f, 0.0f, 1.0f);
        float normalizedG = TransformationUtils.map(g, 0.0f, 255.0f, 0.0f, 1.0f);
        float normalizedB = TransformationUtils.map(b, 0.0f, 255.0f, 0.0f, 1.0f);

        return new float[]{normalizedR, normalizedG, normalizedB};
    }

    /**
     * Maps the values of normal vector components [-1, 1] to [0, 1]
     *
     * @param n0 is the normal vector x component
     * @param n1 is the normal vector y component
     * @param n2 is the normal vector z component
     * @return an array containing the normalized {@code rgb} components
     */
    public static final float[] normalsToRGB(float n0, float n1, float n2) {
        return new float[]{Math.abs(n0), Math.abs(n1), Math.abs(n2)};
    }

}
