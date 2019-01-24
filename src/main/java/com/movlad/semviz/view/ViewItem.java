package com.movlad.semviz.view;

import com.movlad.semviz.core.graphics.engine.QHullBuilder;
import com.movlad.semviz.core.graphics.engine.CloudGeometryConstructor;
import com.movlad.semviz.core.graphics.engine.NormalColoredCloudBuilder;
import com.movlad.semviz.core.graphics.engine.CloudGeometryBuilder;
import com.movlad.semviz.core.graphics.engine.HighResolutionCloudBuilder;
import com.movlad.semviz.core.graphics.engine.Geometry;
import com.movlad.semviz.core.math.geometry.PointCloud;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores geometric information about a given cloud, so that it is not
 * recalculated each time the display mode of the cloud is changed.
 */
class ViewItem {

    private enum CloudGeometryType {
        HIRES, N_HIRES, QHULL
    }

    private final PointCloud cloud;
    private final List<Geometry> geometries;

    private int selectedIndex;

    public ViewItem(PointCloud cloud) {
        this.cloud = cloud;
        this.geometries = new ArrayList<>();

        this.geometries.add(extractGeometry(CloudGeometryType.HIRES));
        this.geometries.add(extractGeometry(CloudGeometryType.N_HIRES));
        this.geometries.add(extractGeometry(CloudGeometryType.QHULL));

        this.selectedIndex = 0;
    }

    public PointCloud getCloud() {
        return cloud;
    }

    public Geometry getGeometrySelection() {
        return geometries.get(selectedIndex);
    }

    public int getSelectedGeometryIndex() {
        return selectedIndex;
    }

    /**
     * @param geometryIndex is the cloud display selectedIndex (<i>e.g.</i> high
     * resolution)
     */
    public void setSelectedGeometryIndex(int geometryIndex) {
        if (geometryIndex < 0 || geometryIndex >= geometries.size()) {
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
