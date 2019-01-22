/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.core.math.geometry;

import com.github.quickhull3d.Point3d;

public class BoundingBox {

    Point3d pointMin;
    Point3d pointMax;

    public BoundingBox(PointCloud cloud) {
        pointMin = new Point3d(0, 0, 0);
        pointMax = new Point3d(0, 0, 0);

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
