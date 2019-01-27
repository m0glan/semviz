package com.movlad.semviz.view;

import com.movlad.semviz.core.graphics.Geometry;
import com.movlad.semviz.core.graphics.GeometryFactory;
import com.movlad.semviz.core.graphics.SceneObject;
import com.movlad.semviz.core.math.geometry.PointCloud;

/**
 * Stores geometric information about a given cloud, so that it is not
 * recalculated each time the display mode of the cloud is changed.
 */
class ViewItem {

    private final PointCloud cloud;
    private final Geometry[] geometries;

    private int selectedIndex;

    public ViewItem(PointCloud cloud) {
        this.cloud = cloud;
        this.geometries = new Geometry[3];

        this.geometries[0] = GeometryFactory.getInstance()
                .createHighResolutionCloudGeometry(cloud);

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
                    geometry = GeometryFactory.getInstance()
                            .createNormalColoredCloudGeometry(cloud);

                    break;

                case 2:
                    geometry = GeometryFactory.getInstance().createQHull(cloud);

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

}
