package com.movlad.semviz.core.graphics;

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
     * Generates the geometry from the data buffer and the layout; defines the
     * geometry's GL drawing mode (<i>e.g.</i>
     * {@code GL_LINES, GL_POINTS, GL_TRIANGLES}).
     */
    public abstract void buildGeometry();

    public final Geometry getGeometry() {
        return geometry;
    }

}
