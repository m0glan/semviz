package com.movlad.semviz.application;

import com.movlad.semviz.core.graphics.engine.Geometry;
import com.movlad.semviz.core.math.geometry.PointCloud;

import java.io.Serializable;

class View {

    private static final int HIRES = 0;
    private static final int HIRES_NORMALS = 1;
    private static final int QHULL = 2;

    private PointCloud cloud;
    private CloudGeometries geometries;

    private int view;
    private boolean isVisible;

    public View(PointCloud cloud) {
        this.view = HIRES;
        this.isVisible = true;
        this.cloud = cloud;
        this.geometries = new CloudGeometries(cloud);
    }

    public void setView(int view) {
        this.view = view;
    }

    public void setVisibility(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Geometry getGeometry() {
        Geometry geometry = null;

        switch (view) {
            case HIRES:
                geometry = geometries.getHiRes();
                break;

            case HIRES_NORMALS:
                geometry = geometries.getHiResNormals();
                break;

            case QHULL:
                geometry = geometries.getQHull();
                break;
        }

        return geometry;
    }

}
