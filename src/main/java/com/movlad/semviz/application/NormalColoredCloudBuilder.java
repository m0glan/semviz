package com.movlad.semviz.application;

import com.jogamp.opengl.GL4;
import com.movlad.semviz.core.graphics.GraphicsUtils;
import com.movlad.semviz.core.graphics.engine.Geometry;
import com.movlad.semviz.core.math.geometry.Point;
import com.movlad.semviz.core.math.geometry.PointCloud;

/**
 * Concrete cloud builder that extracts the
 * {@code x, y, z, normalX, normalY, normalZ} GL vertex buffer data from a point
 * cloud; this generates the high resolution geometry of the base cloud, but
 * instead of displaying each vertex in its original color, its color is
 * generated from the components of the normal vector in it.
 */
public final class NormalColoredCloudBuilder extends CloudGeometryBuilder {

    public NormalColoredCloudBuilder(PointCloud source) {
        super(source);
    }

    @Override
    public void buildDataBuffer() {
        data = new float[source.size() * 6];
        int offset = 0;

        for (Point point : source) {
            float[] rgb = GraphicsUtils.normalsToRGB(point.normalX, point.normalY,
                    point.normalZ);
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
                return GL4.GL_POINTS;
            }

        };
    }

}
