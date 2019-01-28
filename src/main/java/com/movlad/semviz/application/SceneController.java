package com.movlad.semviz.application;

import com.movlad.semviz.core.graphics.Geometry;
import com.movlad.semviz.core.graphics.GeometryFactory;
import com.movlad.semviz.core.graphics.Scene;
import com.movlad.semviz.core.graphics.SceneObject;
import com.movlad.semviz.core.math.geometry.BoundingBox;
import com.movlad.semviz.core.math.geometry.PointCloud;
import com.movlad.semviz.core.semantic.SemanticCloud;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the modification of a {@code Scene} and view in general.
 */
public final class SceneController extends Controller {

    private final Scene scene;
    private final List<PointCloud> clusters;    // cloud clusters

    /*
    A list for arrays of length 3 corresponding to each of the three possible
    ways for viewing a cloud: original geometry, normal colors and convex hull
     */
    private SceneObject[][] views;

    private int[] selectedViewIndices;  // the list of selected views for each cloud

    public SceneController() {
        this.scene = new Scene();
        this.clusters = new ArrayList<>();
    }

    public Scene getScene() {
        return scene;
    }

    /**
     * Loads the base geometry for each cloud in a semantic cloud
     *
     * @param cloud is the cloud containing the clusters
     */
    public void loadDisplayInformation(SemanticCloud cloud) {
        resetDisplayInformation();

        views = new SceneObject[cloud.size()][3];
        selectedViewIndices = new int[cloud.size()];

        for (int i = 0; i < cloud.size(); i++) {
            clusters.add(cloud.get(i));

            SceneObject object = new SceneObject(GeometryFactory.getInstance()
                    .createHighResolutionCloudGeometry(clusters.get(i)));

            views[i][0] = object;
            selectedViewIndices[i] = 0;
            scene.add(object);
        }

        changeSupport.firePropertyChange("SceneUpdate", null, scene);
    }

    /**
     * Clears the scene and the clusters.
     */
    public void resetDisplayInformation() {
        scene.clear();
        clusters.clear();

        changeSupport.firePropertyChange("SceneUpdate", null, scene);
    }

    /**
     * Gets the selected view for the cluster at index {@code i}.
     *
     * @param i is the index of the cluster
     * @return the selected view for the cluster at index {@code i}
     */
    public int getSelectedViewIndex(int i) {
        return selectedViewIndices[i];
    }

    /**
     * Sets the selected view for the cluster at index {@code i}.
     *
     * @param i is the index of the cluster
     * @param view is the view selection for the cluster at index {@code i}
     */
    public void setSelectedViewIndex(int i, int view) {
        int prev = selectedViewIndices[i];

        selectedViewIndices[i] = view;

        if (views[i][view] == null) {
            Geometry geometry;

            switch (view) {
                case 1:
                    geometry = GeometryFactory.getInstance()
                            .createNormalColoredCloudGeometry(clusters.get(i));

                    break;

                case 2:
                    geometry = GeometryFactory.getInstance().createQHull(clusters.get(i));

                    break;

                default:
                    geometry = null;
            }

            views[i][view] = new SceneObject(geometry);
        }

        SceneObject object = views[i][view];

        scene.replace(i, object);

        changeSupport.firePropertyChange("SelectedViewIndexUpdate", prev, selectedViewIndices[i]);
    }

    /**
     * Selects cluster at index {@code i} in the scene.
     *
     * @param i is the index of the selected cluster
     */
    public void setDisplaySelection(int i) {
        if (i != -1) {
            scene.remove(scene.getByName("selection"));
            BoundingBox bbox = new BoundingBox(clusters.get(i));
            Geometry geometry = GeometryFactory.getInstance()
                    .createBoundingBoxGeometry(bbox, 255, 255, 0);

            scene.add(new SceneObject("selection", geometry));
        }

        changeSupport.firePropertyChange("SceneSelectionUpdate", null, i);
    }

}
