package com.movlad.semviz.application;

import com.movlad.semviz.core.graphics.engine.Geometry;
import com.movlad.semviz.core.math.geometry.PointCloud;
import java.util.ArrayList;
import java.util.List;

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

    public int getSelection() {
        return selection;
    }

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
