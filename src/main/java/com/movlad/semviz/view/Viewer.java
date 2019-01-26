package com.movlad.semviz.view;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import com.movlad.semviz.core.graphics.Controls;
import com.movlad.semviz.core.graphics.OrbitControls;
import com.movlad.semviz.core.graphics.OrthographicCamera;
import com.movlad.semviz.core.graphics.Renderer;
import com.movlad.semviz.core.graphics.Scene;
import com.movlad.semviz.core.graphics.SceneObject;
import com.movlad.semviz.core.graphics.WireframeBoxGeometry;
import com.movlad.semviz.core.math.geometry.BoundingBox;
import com.movlad.semviz.core.math.geometry.PointCloud;
import com.movlad.semviz.core.semantic.SemanticCloud;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;

/**
 * Structure gathering a scene, camera, renderer configuration that allows
 * navigating a scene of three-dimensional object around an orbit.
 */
public class Viewer {

    private final List<ViewItem> viewItems;

    private final Scene scene;
    private final OrthographicCamera camera;
    private final Renderer renderer;
    private final Controls controls;

    private final FPSAnimator animator;

    public Viewer(GLJPanel panel) {
        viewItems = new ArrayList<>();

        int width = panel.getWidth();
        int height = panel.getHeight();

        camera = new OrthographicCamera(-width / 2, width / 2, -height / 2, height / 2,
                0.1f, 1000.0f);

        camera.translate(new Vector3f(-10.0f, -10.0f, 5.0f));
        camera.setTarget(new Vector3f(0.0f, 0.0f, 0.0f));
        camera.zoom(50);

        controls = new OrbitControls(camera);

        controls.setZoomSpeed(3.0f);

        panel.addMouseListener(controls);
        panel.addMouseMotionListener(controls);
        panel.addMouseWheelListener(controls);

        scene = new Scene();

        renderer = new Renderer(scene, camera) {

            @Override
            public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
                drawable.getGL().glViewport(x, y, width, height);
                ((OrthographicCamera) camera).setFrustum(-width / 2, width / 2, -height / 2, height / 2);
                camera.updateProjectionMatrix();
            }

        };

        panel.addGLEventListener(renderer);

        animator = new FPSAnimator(panel, 60);
    }

    /**
     * Starts the viewer animation.
     */
    public final void start() {
        animator.start();
    }

    /**
     * Stops the viewer animation; used whenever the scene is modified to
     * prevent the resource from being accessed by multiple components
     * concurrently.
     */
    public final void stop() {
        animator.stop();
    }

    /**
     * Builds a scene from a list of view items (base geometry for all the
     * clouds).
     *
     * @param semanticCloud contains the clusters to display
     */
    public void load(SemanticCloud semanticCloud) {
        reset();

        semanticCloud.forEach(cluster -> {
            viewItems.add(new ViewItem(cluster));

            scene.add(viewItems.get(viewItems.size() - 1).getSceneObject());
        });
    }

    /**
     * Clears the list of view items and the scene.
     */
    public void reset() {
        viewItems.clear();
        scene.clear();
    }

    /**
     * Sets the selected view item in the scene by building its axis aligned
     * bounding box around it.
     *
     * @param i is the index of the view item to be selected
     */
    public void setSelectedViewItem(int i) {
        scene.remove(scene.getByName("selection"));

        if (i != -1) {
            PointCloud cloud = viewItems.get(i).getCloud();
            BoundingBox bbox = new BoundingBox(cloud);
            WireframeBoxGeometry geometry = new WireframeBoxGeometry(bbox, new Vector3f(255, 255, 0));
            SceneObject box = new SceneObject(geometry);

            box.setName("selection");
            scene.add(box);
        }
    }

    /**
     * Returns the selected geometry for the view item at a given index.
     *
     * @param i is the index of the view item
     * @return the index of the selected geometry for a view item
     */
    public int getSelectedGeometryIndex(int i) {
        return viewItems.get(i).getSelectedGeometryIndex();
    }

    /**
     * Sets the selected geometry for the view item at a given index.
     *
     * @param i is the view item to be selected
     * @param geometryIndex the index of the geometry selection for the view
     * item
     */
    public void setSelectedGeometryIndex(int i, int geometryIndex) {
        viewItems.get(i).setSelectedGeometryIndex(geometryIndex);

        SceneObject object = viewItems.get(i).getSceneObject();

        replaceObject(i, object);
    }

    /**
     * Replace an object at the given index in the scene with another.
     *
     * @param i is the position where the replacement takes place
     * @param object is the replacement three-dimensional object
     */
    private void replaceObject(int i, SceneObject object) {
        scene.remove(i);
        scene.add(i, object);
    }

    /**
     * Sets the visibility of the object at a given index.
     *
     * @param i is the index of the object
     * @param isVisible is true if the object is visible
     */
    public void setObjectVisibility(int i, boolean isVisible) {
        scene.get(i).setVisible(isVisible);
    }

    public int numViewItems() {
        return viewItems.size();
    }

}
