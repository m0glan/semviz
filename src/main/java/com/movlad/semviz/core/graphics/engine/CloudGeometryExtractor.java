package com.movlad.semviz.core.graphics.engine;

import com.github.quickhull3d.QuickHull3D;
import com.jogamp.opengl.GL4;
import com.movlad.semviz.core.graphics.BufferLayout;
import com.movlad.semviz.core.math.geometry.TransformationUtils;
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
    private Geometry convexHullViewGeometry = null;

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

                data[i + 3] = TransformationUtils.map(point.r, 0.0f, 255.0f, 0.0f, 1.0f);
                data[i + 4] = TransformationUtils.map(point.g, 0.0f, 255.0f, 0.0f, 1.0f);
                data[i + 5] = TransformationUtils.map(point.b, 0.0f, 255.0f, 0.0f, 1.0f);

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
                
                data[i + 3] = Math.abs((float) point.normalX);
                data[i + 4] = Math.abs((float) point.normalY);
                data[i + 5] = Math.abs((float) point.normalZ);

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
        if (convexHullViewGeometry == null) {
            QuickHull3D hull = new QuickHull3D();
            Point[] points = cloud.getPoints();
        
            hull.build(points);

            int[][] faces = hull.getFaces();
            float[] data = new float[faces.length * 3 * 6];
            int offset = 0;

            for (int i = 0; i < faces.length; i++) {
                for (int j = 0; j < faces[i].length; j++) {
                    Point p = cloud.get(faces[i][j]);

                    data[offset] = (float) (p.x - centroid.x);
                    data[offset + 1] = (float) (p.y - centroid.y);
                    data[offset + 2] = (float) (p.z - centroid.z);
                    data[offset + 3] = TransformationUtils.map(p.r, 0.0f, 255.0f, 0.0f, 1.0f);
                    data[offset + 4] = TransformationUtils.map(p.g, 0.0f, 255.0f, 0.0f, 1.0f);
                    data[offset + 5] = TransformationUtils.map(p.b, 0.0f, 255.0f, 0.0f, 1.0f);
                    
                    offset += 6;
                }
            }

            BufferLayout layout = new BufferLayout();

            layout.push("position", GL4.GL_FLOAT, 3, true);
            layout.push("color", GL4.GL_UNSIGNED_BYTE, 3, false);
            
            convexHullViewGeometry = new Geometry(data, layout) {
                
                @Override
                public int getDrawingMode() {
                        return GL4.GL_TRIANGLES;
                }
                
            };
        }
        
        return convexHullViewGeometry;
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
