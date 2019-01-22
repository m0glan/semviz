package com.movlad.semviz.application;

import com.movlad.semviz.core.graphics.engine.Geometry;
import com.movlad.semviz.core.math.geometry.PointCloud;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores geometric information about a given cloud, so that it is not
 * recalculated each time the display mode of the cloud is changed.
 */
public class ViewItem {

    private static final int HIRES_NORMALS = 1;
    private static final int QHULL = 2;

    private final PointCloud cloud;
    private final List<Geometry> geometries;

    private int selection;

    public ViewItem(PointCloud cloud) {
        this.cloud = cloud;
        this.geometries = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            this.geometries.add(null);
        }

        this.selection = 0;
    }

    public PointCloud getCloud() {
        return cloud;
    }

    public int getSelection() {
        return selection;
    }

    /**
     * @param selection is the cloud display selection (<i>e.g.</i> high
     * resolution)
     * @return the cloud geometry associated with a given display selection.
     */
    public Geometry getGeometry(int selection) {
        this.selection = selection;

        if (geometries.get(selection) == null) {
            CloudGeometryConstructor director;
            CloudGeometryBuilder builder;

            switch (selection) {
                default:
                    builder = new HighResolutionCloudBuilder(cloud);
                    this.selection = 0;
                    break;

                case HIRES_NORMALS:
                    builder = new NormalColoredCloudBuilder(cloud);
                    break;

                case QHULL:
                    builder = new QHullBuilder(cloud);
                    break;
            }

            director = new CloudGeometryConstructor(builder);

            director.construct();

            geometries.set(selection, director.getGeometry());
        }

        return geometries.get(selection);
    }

}
