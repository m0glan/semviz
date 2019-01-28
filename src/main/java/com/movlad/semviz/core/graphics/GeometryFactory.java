package com.movlad.semviz.core.graphics;

import com.github.quickhull3d.Point3d;
import com.github.quickhull3d.QuickHull3D;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.movlad.semviz.core.math.geometry.BoundingBox;
import com.movlad.semviz.core.math.geometry.Point;
import com.movlad.semviz.core.math.geometry.PointCloud;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class GeometryFactory {

    private static GeometryFactory instance = null;

    private GeometryFactory() {
    }

    public static GeometryFactory getInstance() {
        if (instance == null) {
            instance = new GeometryFactory();
        }

        return instance;
    }

    /**
     * @return the geometry for the graphical representation of the
     * three-dimensional coordinate system.
     */
    public Geometry createAxisHelperGeometry() {
        float[] data = new float[]{
            0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f
        };

        BufferLayout layout = new BufferLayout();

        layout.add(new BufferAttribute("position", 3, GL3.GL_FLOAT, true));
        layout.add(new BufferAttribute("color", 3, GL3.GL_FLOAT, true));

        Geometry geometry = new Geometry(FloatBuffer.wrap(data), layout) {
            @Override
            public int getDrawingMode() {
                return GL.GL_LINES;
            }
        };

        return geometry;
    }

    /**
     * @param bbox contains the coordinates of the bounding box
     * @param r is the r component of the color
     * @param g is the g component of the color
     * @param b is the b component of the color
     * @return the geometry for the graphical representation of a bounding box
     */
    public Geometry createBoundingBoxGeometry(BoundingBox bbox, float r, float g, float b) {
        List<Point3d> vertices = new ArrayList<>();

        Point3d min = bbox.getMinBounds();
        Point3d max = bbox.getMaxBounds();

        // calculating other vertices of the bounding box
        vertices.add(min); // A
        vertices.add(new Point3d(min.x, max.y, min.z)); // B
        vertices.add(new Point3d(min.x, min.y, max.z)); // C
        vertices.add(new Point3d(min.x, max.y, max.z)); // D
        vertices.add(new Point3d(max.x, min.y, min.z)); // E
        vertices.add(new Point3d(max.x, max.y, min.z)); // F
        vertices.add(new Point3d(max.x, min.y, max.z)); // G
        vertices.add(max); // H

        // creating and storing the edges
        int[][] edges = {{0, 1}, {0, 2}, {0, 4}, {1, 3}, {1, 5}, {2, 3}, {2, 6},
        {3, 7}, {4, 5}, {4, 6}, {5, 7}, {6, 7}};

        // extracting the graphical data
        BufferLayout layout = new BufferLayout();

        layout.add(new BufferAttribute("position", 3, GL3.GL_FLOAT, true));
        layout.add(new BufferAttribute("color", 3, GL3.GL_FLOAT, true));

        float[] data = new float[edges.length * 2 * layout.rowLength()];

        int offset = 0;

        for (int[] edge : edges) {
            for (int j = 0; j < edge.length; j++) {
                Point3d v = vertices.get(edge[j]);
                float[] rgb = GLUtils.normalizeColor(r, g, b);
                float[] section = new float[]{(float) v.x, (float) v.y, (float) v.z,
                    rgb[0], rgb[1], rgb[2]};

                System.arraycopy(section, 0, data, offset, section.length);

                offset += section.length;
            }
        }

        Geometry geometry = new Geometry(FloatBuffer.wrap(data), layout) {
            @Override
            public int getDrawingMode() {
                return GL3.GL_LINES;
            }
        };

        return geometry;
    }

    /**
     * Extracts the {@code xyzrgb} buffer data from the cloud and creates a
     * geometry from it.
     *
     * @param cloud is the cloud containing the graphical data
     * @return the geometry for the graphical representation of the cloud
     */
    public Geometry createHighResolutionCloudGeometry(PointCloud cloud) {
        BufferLayout layout = new BufferLayout();

        layout.add(new BufferAttribute("position", 3, GL3.GL_FLOAT, true));
        layout.add(new BufferAttribute("color", 3, GL3.GL_FLOAT, true));

        float[] data = new float[cloud.size() * layout.rowLength()];

        int offset = 0;

        for (Point point : cloud) {
            float[] rgb = GLUtils.normalizeColor((float) point.r, (float) point.g, (float) point.b);
            float[] section = new float[]{(float) point.x, (float) point.y, (float) point.z,
                rgb[0], rgb[1], rgb[2]};

            System.arraycopy(section, 0, data, offset, section.length);

            offset += section.length;
        }

        Geometry geometry = new Geometry(FloatBuffer.wrap(data), layout) {
            @Override
            public int getDrawingMode() {
                return GL3.GL_POINTS;
            }
        };

        return geometry;
    }

    /**
     * Extracts the {@code xyzrgb} buffer data from the cloud and creates a
     * geometry from it; the {@code rgb} data is given by the values of the
     * components of the normal vector within each point.
     *
     * @param cloud is the cloud containing the graphical data
     * @return geometry for the graphical representation of the cloud
     */
    public Geometry createNormalColoredCloudGeometry(PointCloud cloud) {
        BufferLayout layout = new BufferLayout();

        layout.add(new BufferAttribute("position", 3, GL3.GL_FLOAT, true));
        layout.add(new BufferAttribute("color", 3, GL3.GL_FLOAT, true));

        float[] data = new float[cloud.size() * layout.rowLength()];

        int offset = 0;

        for (Point point : cloud) {
            float[] section = new float[]{(float) point.x, (float) point.y, (float) point.z,
                Math.abs(point.normalX), Math.abs(point.normalY), Math.abs(point.normalZ)};

            System.arraycopy(section, 0, data, offset, section.length);

            offset += section.length;
        }

        Geometry geometry = new Geometry(FloatBuffer.wrap(data), layout) {
            @Override
            public int getDrawingMode() {
                return GL3.GL_POINTS;
            }
        };

        return geometry;
    }

    /**
     * Creates a convex hull from the cloud and creates a geometry from it.
     *
     * @param cloud is the cloud containing the graphical data
     * @return the geometry of the generated hull
     */
    public Geometry createQHull(PointCloud cloud) {
        int offset = 0;

        QuickHull3D hull = new QuickHull3D();

        hull.build(cloud.getPoints());
        hull.triangulate();

        int[][] faces = hull.getFaces();
        int[] vertexPointIndices = hull.getVertexPointIndices();

        // calculating average color for the hull
        short r = 0, g = 0, b = 0;

        for (int i = 0; i < vertexPointIndices.length; i++) {
            Point point = cloud.get(i);

            r += point.r;
            g += point.g;
            b += point.b;
        }

        r /= vertexPointIndices.length;
        g /= vertexPointIndices.length;
        b /= vertexPointIndices.length;

        BufferLayout layout = new BufferLayout();

        layout.add(new BufferAttribute("position", 3, GL3.GL_FLOAT, true));
        layout.add(new BufferAttribute("color", 3, GL3.GL_FLOAT, true));

        float[] data = new float[faces.length * faces[0].length * layout.rowLength()];

        for (int[] face : faces) {
            for (int cornerIndex : face) {
                Point point = cloud.get(vertexPointIndices[cornerIndex]);
                float[] rgb = GLUtils.normalizeColor(r, g, b);
                float[] section = new float[]{(float) point.x, (float) point.y,
                    (float) point.z, rgb[0], rgb[1], rgb[2]};

                System.arraycopy(section, 0, data, offset, section.length);

                offset += section.length;
            }
        }

        Geometry geometry = new Geometry(FloatBuffer.wrap(data), layout) {
            @Override
            public int getDrawingMode() {
                return GL3.GL_TRIANGLES;
            }
        };

        return geometry;
    }

}
