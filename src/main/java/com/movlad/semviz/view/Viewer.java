package com.movlad.semviz.view;

import com.movlad.semviz.core.semantic.SemanticCloud;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.movlad.semviz.core.graphics.engine.Camera;
import com.movlad.semviz.core.graphics.engine.Controls;
import com.movlad.semviz.core.graphics.engine.Object3d;
import com.movlad.semviz.core.graphics.engine.OrbitControls;
import com.movlad.semviz.core.graphics.engine.OrthographicCamera;
import com.movlad.semviz.core.graphics.engine.Renderer;
import com.movlad.semviz.core.graphics.engine.Scene;
import com.movlad.semviz.core.graphics.engine.WireframeBox;
import com.movlad.semviz.core.math.geometry.BoundingBox;
import com.movlad.semviz.core.math.geometry.PointCloud;
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
    private final Camera camera;
    private final Renderer renderer;
    private final Controls controls;

    private final GLCanvas glCanvas;

    private final FPSAnimator animator;

    private boolean isRunning;

    public Viewer(int width, int height) {
        viewItems = new ArrayList<>();

        glCanvas = new GLCanvas(new GLCapabilities(GLProfile.get(GLProfile.GL3)));
        camera = new OrthographicCamera(-width, width,
                -height, height, 0.1f, 1000.0f);

        camera.translate(new Vector3f(-10.0f, -10.0f, 5.0f));
        camera.setTarget(new Vector3f(0.0f, 0.0f, 0.0f));
        camera.zoom(100);

        controls = new OrbitControls(camera);

        controls.setZoomSpeed(3.0f);

        scene = new Scene();

        renderer = new Renderer(scene, camera);
        animator = new FPSAnimator(glCanvas, 60);
    }

    public GLCanvas getGlCanvas() {
        return glCanvas;
    }

    /**
     * Starts the viewer animation.
     */
    public final void start() {
        if (!isRunning) {
            glCanvas.addGLEventListener(renderer);
            glCanvas.addMouseListener(controls);
            glCanvas.addMouseMotionListener(controls);
            glCanvas.addMouseWheelListener(controls);
            animator.start();

            isRunning = true;
        }
    }

    /**
     * Stops the viewer animation; used whenever the scene is modified to
     * prevent the resource from being accessed by multiple components
     * concurrently.
     */
    public final void stop() {
        if (isRunning) {
            glCanvas.removeGLEventListener(renderer);
            glCanvas.removeMouseListener(controls);
            glCanvas.removeMouseMotionListener(controls);
            glCanvas.removeMouseWheelListener(controls);
            animator.stop();

            isRunning = false;
        }
    }

    /**
     * Builds a scene from a list of view items (base geometry for all the
     * clouds).
     *
     * @param superCloud contains the clusters to display
     */
    public void load(SemanticCloud superCloud) {
        reset();

        superCloud.forEach(cluster -> {
            viewItems.add(new ViewItem(cluster));

            scene.add(viewItems.get(viewItems.size() - 1).getGeometrySelection());
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
        scene.remove("selection");

        if (i != -1) {
            PointCloud cloud = viewItems.get(i).getCloud();
            BoundingBox bbox = new BoundingBox(cloud);
            WireframeBox geometry = new WireframeBox(bbox, new Vector3f(255, 255, 0));

            geometry.setId("selection");
            scene.add(geometry);
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

        Object3d geometry = viewItems.get(i).getGeometrySelection();

        replaceObject(i, geometry);
    }

    /**
     * Replace an object at the given index in the scene with another.
     *
     * @param i is the position where the replacement takes place
     * @param object is the replacement three-dimensional object
     */
    private void replaceObject(int i, Object3d object) {
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
