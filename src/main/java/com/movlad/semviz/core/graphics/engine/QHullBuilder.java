package com.movlad.semviz.core.graphics.engine;

import com.github.quickhull3d.QuickHull3D;
import com.jogamp.opengl.GL3;
import com.movlad.semviz.core.graphics.GraphicsUtils;
import com.movlad.semviz.core.graphics.engine.Geometry;
import com.movlad.semviz.core.math.geometry.Point;
import com.movlad.semviz.core.math.geometry.PointCloud;

/**
 * This cloud builder generates the convex hull geometry of a point cloud; the
 * color of the hull is given by the normal vector to the plane given by the
 * cloud.
 */
public final class QHullBuilder extends CloudGeometryBuilder {

    public QHullBuilder(PointCloud source) {
        super(source);
    }

    @Override
    public void buildDataBuffer() {
        QuickHull3D hull = new QuickHull3D();
        Point[] points = source.getPoints();

        hull.build(points);

        int[][] faces = hull.getFaces();

        data = new float[faces.length * 3 * 6];

        int offset = 0;

        for (int[] face : faces) {
            for (int j = 0; j < face.length; j++) {
                Point point = source.get(face[j]);
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
    }

    @Override
    public void buildGeometry() {
        geometry = new Geometry(data, layout) {

            @Override
            public int getDrawingMode() {
                return GL3.GL_TRIANGLES;
            }

        };
    }

}
