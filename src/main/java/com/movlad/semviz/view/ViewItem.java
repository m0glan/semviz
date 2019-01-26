package com.movlad.semviz.view;

import com.movlad.semviz.core.graphics.CloudGeometryBuilder;
import com.movlad.semviz.core.graphics.CloudGeometryConstructor;
import com.movlad.semviz.core.graphics.Geometry;
import com.movlad.semviz.core.graphics.HighResolutionCloudBuilder;
import com.movlad.semviz.core.graphics.NormalColoredCloudBuilder;
import com.movlad.semviz.core.graphics.QHullBuilder;
import com.movlad.semviz.core.graphics.SceneObject;
import com.movlad.semviz.core.math.geometry.PointCloud;

/**
 * Stores geometric information about a given cloud, so that it is not
 * recalculated each time the display mode of the cloud is changed.
 */
class ViewItem {

    private enum CloudGeometryType {
        HIRES, N_HIRES, QHULL
    }

    private final PointCloud cloud;
    private final Geometry[] geometries;

    private int selectedIndex;

    public ViewItem(PointCloud cloud) {
        this.cloud = cloud;
        this.geometries = new Geometry[3];

        this.geometries[0] = extractGeometry(CloudGeometryType.HIRES);

        this.selectedIndex = 0;
    }

    public PointCloud getCloud() {
        return cloud;
    }

    public SceneObject getSceneObject() {
        if (geometries[selectedIndex] == null) {
            Geometry geometry;

            switch (selectedIndex) {
                case 1:
                    geometry = extractGeometry(CloudGeometryType.N_HIRES);

                    break;

                case 2:
                    geometry = extractGeometry(CloudGeometryType.QHULL);

                    break;

                default:
                    geometry = null;
            }

            geometries[selectedIndex] = geometry;
        }

        return new SceneObject(geometries[selectedIndex]);
    }

    public int getSelectedGeometryIndex() {
        return selectedIndex;
    }

    /**
     * @param geometryIndex is the cloud display selectedIndex
     * (<i>e.geometry.</i> high resolution)
     */
    public void setSelectedGeometryIndex(int geometryIndex) {
        if (geometryIndex < 0 || geometryIndex >= geometries.length) {
            throw new ArrayIndexOutOfBoundsException("No geometry at that index.");
        }

        this.selectedIndex = geometryIndex;
    }

    /**
     * Builds and constructs the specified cloud geometry.
     *
     * @param type is the type of geometry to build
     * @return the constructed cloud geometry
     */
    private Geometry extractGeometry(CloudGeometryType type) {
        CloudGeometryConstructor director;
        CloudGeometryBuilder builder;

        switch (type) {
            case HIRES:
                builder = new HighResolutionCloudBuilder(cloud);
                break;

            case N_HIRES:
                builder = new NormalColoredCloudBuilder(cloud);
                break;

            case QHULL:
                builder = new QHullBuilder(cloud);
                break;

            default:
                builder = null;
        }

        director = new CloudGeometryConstructor(builder);

        director.construct();

        return director.getGeometry();
    }

}
