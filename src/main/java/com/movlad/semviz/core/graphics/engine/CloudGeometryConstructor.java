package com.movlad.semviz.core.graphics.engine;

/**
 * Director class for the
 * {@link com.movlad.semviz.core.graphics.engine.CloudGeometryBuilder}.
 */
public final class CloudGeometryConstructor {

    private final CloudGeometryBuilder builder;

    public CloudGeometryConstructor(CloudGeometryBuilder builder) {
        this.builder = builder;
    }

    /**
     * Constructs a cloud geometry, like a convex hull, based on the
     * implementation of
     * {@link com.movlad.semviz.core.graphics.engine.CloudGeometryBuilder}.
     */
    public void construct() {
        builder.buildDataBufferLayout();
        builder.buildDataBuffer();
        builder.buildGeometry();
    }

    public Geometry getGeometry() {
        return builder.getGeometry();
    }

}
