/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

public class Viewer {

    private Scene scene;
    private final Camera camera;
    private Renderer renderer;
    private Controls controls;

    private final GLCanvas glCanvas;

    private FPSAnimator animator;

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

    public void fromViewItems(List<ViewItem> viewItems) {
        clearScene();

        viewItems.forEach(cluster -> {
            scene.add(cluster.getGeometry(0));
        });
    }

    public void addObject(Object3d object) {
        stop();
        scene.add(object);
    }

    public void removeObject(int i) {
        stop();
        scene.remove(i);
    }

    public void removeObject(String id) {
        stop();
        scene.remove(id);
    }

    public void replaceObject(int i, Object3d object) {
        removeObject(i);

        scene.add(i, object);
    }

    public void setObjectVisibility(int i, boolean isVisible) {
        stop();
        scene.get(i).setVisible(isVisible);
    }

    public void clearScene() {
        stop();
        scene.clear();
    }

}
