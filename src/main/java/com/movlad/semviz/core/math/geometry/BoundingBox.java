package com.movlad.semviz.core.math.geometry;

import com.github.quickhull3d.Point3d;

/**
 * Axis aligned bounding box from a source point cloud.
 */
public class BoundingBox {

    Point3d pointMin;
    Point3d pointMax;

    public BoundingBox(PointCloud cloud) {
        pointMin = new Point3d(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        pointMax = new Point3d(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);

        cloud.forEach(point -> {
            if (point.x < pointMin.x) {
                pointMin.x = point.x;
            }

            if (point.y < pointMin.y) {
                pointMin.y = point.y;
            }

            if (point.z < pointMin.z) {
                pointMin.z = point.z;
            }

            if (point.x > pointMax.x) {
                pointMax.x = point.x;
            }

            if (point.y > pointMax.y) {
                pointMax.y = point.y;
            }

            if (point.z > pointMax.z) {
                pointMax.z = point.z;
            }
        });
    }

    public Point3d getMinBounds() {
        return pointMin;
    }

    public Point3d getMaxBounds() {
        return pointMax;
    }

}
