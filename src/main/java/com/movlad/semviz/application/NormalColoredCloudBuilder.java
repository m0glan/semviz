/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.application;

import com.jogamp.opengl.GL4;
import com.movlad.semviz.core.graphics.GraphicsUtils;
import com.movlad.semviz.core.graphics.engine.Geometry;
import com.movlad.semviz.core.math.geometry.Point;
import com.movlad.semviz.core.math.geometry.PointCloud;

public class NormalColoredCloudBuilder extends CloudGeometryBuilder {

    public NormalColoredCloudBuilder(PointCloud source) {
        super(source);
    }

    @Override
    public void buildDataBuffer() {
        data = new float[source.size() * 6];
        int offset = 0;

        int length = layout.length();

        for (Point point : source) {
            float[] rgb = GraphicsUtils.rgbFromNormals(point.normalX, point.normalY, point.normalZ);

            offset = GraphicsUtils.fillBufferSection(data, offset, (float) point.x, (float) point.y,
                    (float) point.z, rgb[0], rgb[1], rgb[2]);
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
