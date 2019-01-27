package com.movlad.semviz.core.graphics;

import com.movlad.semviz.core.math.geometry.TransformationUtils;

/**
 * Offers some tools allowing to manipulated some graphical data.
 */
public class GLUtils {

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
