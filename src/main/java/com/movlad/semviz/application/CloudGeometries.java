package com.movlad.semviz.application;

import com.github.quickhull3d.QuickHull3D;
import com.jogamp.opengl.GL4;
import com.movlad.semviz.core.graphics.BufferLayout;
import com.movlad.semviz.core.graphics.engine.Geometry;
import com.movlad.semviz.core.math.geometry.Point;
import com.movlad.semviz.core.math.geometry.PointCloud;
import com.movlad.semviz.core.math.geometry.TransformationUtils;

import java.nio.Buffer;

/**
 * Given a 3D point cloud, this class is used to extract the different
 * geometries stored within for drawing with OpenGL.
 */
class CloudGeometries {

    private PointCloud cloud;
    private Point centroid;

    private Geometry hires;
    private Geometry hiresNormals;
    private Geometry qhull;

    /**
     * @param cloud is the cloud for which to extract different geometries
     */
    public CloudGeometries(PointCloud cloud) {
        this.cloud = cloud;
        this.qhull = null;
        this.hiresNormals = null;
        this.hires = null;
    }

    /**
     * @return the geometry corresponding to the original hi-res cloud
     */
    public Geometry getHiRes() {
        if (hires == null) {
            float[] data = new float[cloud.size() * 6];
            int offset = 0;
            BufferLayout layout = genLayout();

            for (Point point : cloud) {
                float[] rgb = normalizeRGB(point.r, point.g, point.b);

                offset = fillBufferSection(data, offset, (float) point.x, (float) (point.y), (float) (point.z),
                        rgb[0], rgb[1], rgb[2]);
            }


            hires = new Geometry(data, layout) {

                @Override
                public int getDrawingMode() {
                    return GL4.GL_POINTS;
                }

            };
        }

        return hires;
    }

    /**
     * @return the geometry corresponding to the original hi-res cloud, the
     * color of each point being given by the cloud's normal vector in that
     * point
     */
    public Geometry getHiResNormals() {
        if (hiresNormals == null) {
            float[] data = new float[cloud.size() * 6];
            int offset = 0;

            BufferLayout layout = genLayout();

            int length = layout.length();

            for (Point point : cloud) {
                float[] rgb = rgbFromNormals(point.normalX, point.normalY, point.normalZ);

                offset = fillBufferSection(data, offset, (float) point.x, (float) point.y,
                        (float) point.z, rgb[0], rgb[1], rgb[2]);
            }

            hiresNormals = new Geometry(data, layout) {

                @Override
                public int getDrawingMode() {
                    return GL4.GL_POINTS;
                }

            };
        }

        return hiresNormals;
    }

    /**
     * @return the convex hull geometry of the point cloud
     */
    public Geometry getQHull() {
        if (qhull == null) {
            QuickHull3D hull = new QuickHull3D();
            Point[] points = cloud.getPoints();

            hull.build(points);

            int[][] faces = hull.getFaces();
            float[] data = new float[faces.length * 3 * 6];

            int offset = 0;
            BufferLayout layout = genLayout();

            for (int[] face : faces)
                for (int j = 0; j < face.length; j++) {
                    Point point = cloud.get(face[j]);
                    float[] rgb = rgbFromNormals(point.normalX, point.normalY, point.normalZ);

                    offset = fillBufferSection(data, offset, (float) point.x, (float) point.y, (float) point.z,
                            rgb[0], rgb[1], rgb[2]);
                }

            qhull = new Geometry(data, layout) {

                @Override
                public int getDrawingMode() {
                    return GL4.GL_TRIANGLES;
                }

            };
        }

        return qhull;
    }

    private BufferLayout genLayout() {
        BufferLayout layout = new BufferLayout();

        layout.push("position", GL4.GL_FLOAT, 3, true);
        layout.push("color", GL4.GL_UNSIGNED_BYTE, 3, false);

        return layout;
    }

    private int fillBufferSection(float[] buffer, int offset, float... data) {
        for (int i = 0; i < data.length; i++) {
            buffer[offset + i] = data[i];
        }

        return offset + data.length;
    }

    private float[] normalizeRGB(float r, float g, float b) {
        float normalizedR = TransformationUtils.map(r, 0.0f, 255.0f, 0.0f, 1.0f);
        float normalizedG = TransformationUtils.map(g, 0.0f, 255.0f, 0.0f, 1.0f);
        float normalizedB = TransformationUtils.map(b, 0.0f, 255.0f, 0.0f, 1.0f);

        return new float[]{normalizedR, normalizedG, normalizedB};
    }

    private float[] rgbFromNormals(float n0, float n1, float n2) {
        return new float[]{Math.abs(n0), Math.abs(n1), Math.abs(n2)};
    }

}
