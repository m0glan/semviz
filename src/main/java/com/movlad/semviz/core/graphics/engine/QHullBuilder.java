package com.movlad.semviz.core.graphics.engine;

import com.github.quickhull3d.QuickHull3D;
import com.jogamp.opengl.GL3;
import com.movlad.semviz.core.graphics.GraphicsUtils;
import com.movlad.semviz.core.math.geometry.Point;
import com.movlad.semviz.core.math.geometry.PointCloud;
import org.joml.Vector3f;

public final class QHullBuilder extends CloudGeometryBuilder {

    public QHullBuilder(PointCloud source) {
        super(source);
    }

    @Override
    public void buildDataBuffer() {
        int offset = 0;

        QuickHull3D hull = new QuickHull3D();

        hull.build(source.getPoints());
        hull.triangulate();

        int[][] faces = hull.getFaces();
        int[] vertexPointIndices = hull.getVertexPointIndices();
        Vector3f colorAvg = new Vector3f();

        for (int i = 0; i < vertexPointIndices.length; i++) {
            Point point = source.get(i);

            colorAvg.add(point.r, point.g, point.b);
        }

        colorAvg.div(vertexPointIndices.length);

        data = new float[faces.length * 3 * 6];

        for (int[] face : faces) {
            for (int cornerIndex : face) {
                Point point = source.get(vertexPointIndices[cornerIndex]);

                float[] rgb = GraphicsUtils.normalizeRGB(colorAvg.x, colorAvg.y, colorAvg.z);
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
