package com.movlad.semviz.application;

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
import java.util.List;
import org.joml.Vector3f;

/**
 * Structure gathering a scene, camera, renderer configuration that allows
 * navigating a scene of three-dimensional object around an orbit.
 */
public class Viewer {

    private final Scene scene;
    private final Camera camera;
    private final Renderer renderer;
    private final Controls controls;

    private final GLCanvas glCanvas;

    private final FPSAnimator animator;

    private boolean isRunning;

    public Viewer(int width, int height) {
        glCanvas = new GLCanvas(new GLCapabilities(GLProfile.get(GLProfile.GL4)));
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
     * @param viewItems is a list of view items
     */
    public void fromViewItems(List<ViewItem> viewItems) {
        scene.clear();

        viewItems.forEach(cluster -> {
            scene.add(cluster.getGeometry(0));
        });
    }

    /**
     * Adds an object to the scene.
     *
     * @param object is the three-dimensional object to be added to the scene
     */
    public void addObject(Object3d object) {
        scene.add(object);
    }

    /**
     * Removes an object from the scene.
     *
     * @param i is the index of the three-dimensional object to be removed from
     * the scene
     */
    public void removeObject(int i) {
        scene.remove(i);
    }

    /**
     * Adds an object to the scene.
     *
     * @param id is the id of the three-dimensional object to be removed from
     * the scene
     */
    public void removeObject(String id) {
        scene.remove(id);
    }

    /**
     * Replace an object at the given index in the scene with another.
     *
     * @param i is the position where the replacement takes place
     * @param object is the replacement three-dimensional object
     */
    public void replaceObject(int i, Object3d object) {
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

    /**
     * Removes all objects from the scene.
     */
    public void clearScene() {
        scene.clear();
    }

}
