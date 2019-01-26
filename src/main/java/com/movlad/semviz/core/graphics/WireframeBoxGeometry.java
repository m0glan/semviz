package com.movlad.semviz.core.graphics;

import com.github.quickhull3d.Point3d;
import com.jogamp.opengl.GL3;
import com.movlad.semviz.core.math.geometry.BoundingBox;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;

/**
 * Geometry containing the necessary buffer data to draw the edges of a box.
 */
public class WireframeBoxGeometry extends Geometry {

    /**
     * @param bbox is the bounding box containing the box information
     * @param rgb is the color of the edges of the box
     */
    public WireframeBoxGeometry(BoundingBox bbox, Vector3f rgb) {
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

        float[] rgbNormalized = GraphicsUtils.normalizeRGB(rgb.x, rgb.y, rgb.z);

        data = new float[edges.length * 2 * 6];

        int offset = 0;

        for (int[] edge : edges) {
            for (int j = 0; j < edge.length; j++) {
                Point3d v = vertices.get(edge[j]);
                float[] section = {
                    (float) v.x, (float) (v.y), (float) (v.z),
                    rgbNormalized[0], rgbNormalized[1], rgbNormalized[2]
                };

                System.arraycopy(section, 0, data, offset, section.length);

                offset += section.length;
            }
        }
    }

    @Override
    public int getDrawingMode() {
        return GL3.GL_LINES;
    }

}
