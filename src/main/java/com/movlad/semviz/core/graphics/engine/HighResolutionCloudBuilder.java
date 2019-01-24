package com.movlad.semviz.core.graphics.engine;

import com.jogamp.opengl.GL3;
import com.movlad.semviz.core.graphics.GraphicsUtils;
import com.movlad.semviz.core.graphics.engine.Geometry;
import com.movlad.semviz.core.math.geometry.Point;
import com.movlad.semviz.core.math.geometry.PointCloud;

/**
 * Concrete cloud builder that extracts the {@code x, y, z, r, g, b} GL vertex
 * buffer data from a point cloud; this generates the high resolution geometry
 * of the base cloud.
 */
public final class HighResolutionCloudBuilder extends CloudGeometryBuilder {

    public HighResolutionCloudBuilder(PointCloud source) {
        super(source);
    }

    @Override
    public void buildDataBuffer() {
        data = new float[source.size() * 6];
        int offset = 0;

        for (Point point : source) {
            float[] rgb = GraphicsUtils.normalizeRGB(point.r, point.g, point.b);
            float[] section = {
                (float) point.x, (float) (point.y), (float) (point.z),
                rgb[0], rgb[1], rgb[2]
            };

            System.arraycopy(section, 0, data, offset, section.length);

            offset += section.length;
        }
    }

    @Override
    public void buildGeometry() {
        geometry = new Geometry(data, layout) {

            @Override
            public int getDrawingMode() {
                return GL3.GL_POINTS;
            }

        };
    }

}
