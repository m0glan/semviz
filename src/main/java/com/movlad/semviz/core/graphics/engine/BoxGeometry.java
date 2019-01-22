/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.core.graphics.engine;

import com.github.quickhull3d.Point3d;
import com.jogamp.opengl.GL4;
import com.movlad.semviz.core.graphics.BufferLayout;
import com.movlad.semviz.core.graphics.GraphicsUtils;
import com.movlad.semviz.core.math.geometry.BoundingBox;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;

public class BoxGeometry extends Geometry {

    public BoxGeometry(BoundingBox bbox, Vector3f rgb) {
        float[] rgbNormalized = GraphicsUtils.normalizeRGB(rgb.x, rgb.y, rgb.z);
        List<Point3d> vertices = new ArrayList<>();

        Point3d min = bbox.getMinBounds();
        Point3d max = bbox.getMaxBounds();

        vertices.add(min); // A
        vertices.add(new Point3d(min.x, max.y, min.z)); // B
        vertices.add(new Point3d(min.x, min.y, max.z)); // C
        vertices.add(new Point3d(min.x, max.y, max.z)); // D
        vertices.add(new Point3d(max.x, min.y, min.z)); // E
        vertices.add(new Point3d(max.x, max.y, min.z)); // F
        vertices.add(new Point3d(max.x, min.y, max.z)); // G
        vertices.add(max); // H

        int[][] edges = {
            {0, 1},
            {0, 2},
            {0, 4},
            {1, 3},
            {1, 5},
            {2, 3},
            {2, 6},
            {3, 7},
            {4, 5},
            {4, 6},
            {5, 7},
            {6, 7}
        };

        data = new float[edges.length * 2 * 6];

        int offset = 0;

        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges[i].length; j++) {
                Point3d v = vertices.get(edges[i][j]);

                offset = GraphicsUtils.fillBufferSection(data, offset, (float) v.x, (float) v.y, (float) v.z,
                        rgbNormalized[0], rgbNormalized[1], rgbNormalized[2]);
            }
        }

        layout = new BufferLayout();

        layout.push("position", GL4.GL_FLOAT, 3, false);
        layout.push("color", GL4.GL_UNSIGNED_BYTE, 3, false);
    }

    @Override
    public int getDrawingMode() {
        return GL4.GL_LINES;
    }

}
