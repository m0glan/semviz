/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.application;

import com.github.quickhull3d.QuickHull3D;
import com.jogamp.opengl.GL4;
import com.movlad.semviz.core.graphics.GraphicsUtils;
import com.movlad.semviz.core.graphics.engine.Geometry;
import com.movlad.semviz.core.math.geometry.Point;
import com.movlad.semviz.core.math.geometry.PointCloud;

public class QHullBuilder extends CloudGeometryBuilder {

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
                float[] rgb = GraphicsUtils.rgbFromNormals(point.normalX, point.normalY, point.normalZ);

                offset = GraphicsUtils.fillBufferSection(data, offset, (float) point.x, (float) point.y, (float) point.z,
                        rgb[0], rgb[1], rgb[2]);
            }
        }
    }

    @Override
    public void buildGeometry() {
        geometry = new Geometry(data, layout) {

            @Override
            public int getDrawingMode() {
                return GL4.GL_TRIANGLES;
            }

        };
    }

}
