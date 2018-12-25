package com.movlad.semviz.core.graphics.engine;

import com.jogamp.opengl.GL4;
import com.movlad.semviz.core.graphics.BufferLayout;
import com.movlad.semviz.core.graphics.MathUtils;
import com.movlad.semviz.core.math.geometry.Point;
import com.movlad.semviz.core.math.geometry.PointCloud;

/**
 * Given a 3D point cloud, this class is used to extract the different geometries stored within
 * for drawing with OpenGL.
 */
public class CloudGeometryExtractor {

    private PointCloud cloud;
    private Point centroid;

    private Geometry baseGeometry = null;
    private Geometry normalViewGeometry = null;

    /**
     * @param cloud is the cloud for which to extract different geometries
     */
    public CloudGeometryExtractor(PointCloud cloud) {
        this.cloud = cloud;

        calculateCentroid();
    }

    /**
     * @return the geometry corresponding to the original hi-res cloud
     */
    public Geometry extractBaseGeometry() {
        if (baseGeometry == null) {
            float[] data = new float[cloud.size() * 6];
            int i = 0;;

            for (Point point : cloud) {
                data[i] = (float) (point.x - centroid.x);
                data[i + 1] = (float) (point.y - centroid.y);
                data[i + 2] = (float) (point.z - centroid.z);

                data[i + 3] = MathUtils.map(point.r, 0.0f, 255.0f, 0.0f, 1.0f);
                data[i + 4] = MathUtils.map(point.g, 0.0f, 255.0f, 0.0f, 1.0f);
                data[i + 5] = MathUtils.map(point.b, 0.0f, 255.0f, 0.0f, 1.0f);

                i += 6;
            }

            BufferLayout layout = new BufferLayout();

            layout.push("position", GL4.GL_FLOAT, 3, true);
            layout.push("color", GL4.GL_UNSIGNED_BYTE, 3, true);

            baseGeometry = new Geometry(data, layout) {

                @Override
                public int getDrawingMode() {
                        return GL4.GL_POINTS;
                }

            };
        }

        return baseGeometry;
    }

    /**
     * @return the geometry corresponding to the original hi-res cloud, the color of each point
     * being given by the cloud's normal vector in that point
     */
    public Geometry extractNormalRGBGeometry() {
        if (normalViewGeometry == null) {
            float[] data = new float[cloud.size() * 6];
            int i = 0;

            for (Point point : cloud) {
                data[i] = (float) (point.x - centroid.x);
                data[i + 1] = (float) (point.y - centroid.y);
                data[i + 2] = (float) (point.z - centroid.z);

                data[i + 3] = (float) point.normalX;
                data[i + 4] = (float) point.normalY;
                data[i + 5] = (float) point.normalZ;

                i += 6;
            }

            BufferLayout layout = new BufferLayout();

            layout.push("position", GL4.GL_FLOAT, 3, true);
            layout.push("color", GL4.GL_UNSIGNED_BYTE, 3, false);

            normalViewGeometry = new Geometry(data, layout) {

                @Override
                public int getDrawingMode() {
                        return GL4.GL_POINTS;
                }

            };
        }

        return normalViewGeometry;
    }

    /**
     * @return the convex hull geometry of the point cloud
     */
    public Geometry extractConvexHullGeometry() { 
        throw new UnsupportedOperationException();
    }

    private void calculateCentroid() {
        centroid = new Point();

        for (Point point : cloud) {
            centroid.x += point.x;
            centroid.y += point.y;
            centroid.z += point.z;
        }

        centroid.x /= cloud.size();
        centroid.y /= cloud.size();
        centroid.z /= cloud.size();
    }
	
}
