package com.movlad.semviz.core.graphics.engine;

import com.jogamp.opengl.GL4;
import com.movlad.semviz.core.graphics.BufferLayout;
import com.movlad.semviz.core.graphics.engine.Geometry;
import com.movlad.semviz.core.math.geometry.PointCloud;

/**
 * Base class for cloud data extractor (base {@code rgb}, normal {@code rgb},
 * qhull ).
 */
public abstract class CloudGeometryBuilder {

    protected PointCloud source;
    protected float[] data;
    protected BufferLayout layout;
    protected Geometry geometry;

    public CloudGeometryBuilder(PointCloud source) {
        this.source = source;
    }

    /**
     * Generates the GL vertex buffer.
     */
    public abstract void buildDataBuffer();

    /**
     * Builds the layout of the vertex buffer, which is composed of a position
     * and a color.
     */
    public final void buildDataBufferLayout() {
        layout = new BufferLayout();

        layout.push("position", GL4.GL_FLOAT, 3, true);
        layout.push("color", GL4.GL_UNSIGNED_BYTE, 3, false);
    }

    /**
     * Generates the geometry from the data buffer and the layout; defines the
     * geometry's GL drawing mode (<i>e.g.</i>
     * {@code GL_LINES, GL_POINTS, GL_TRIANGLES}).
     */
    public abstract void buildGeometry();

    public final Geometry getGeometry() {
        return geometry;
    }

}
