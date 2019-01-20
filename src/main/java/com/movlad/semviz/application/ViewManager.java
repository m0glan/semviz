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
import com.movlad.semviz.core.graphics.engine.*;
import com.movlad.semviz.core.graphics.engine.Renderer;
import com.movlad.semviz.core.graphics.engine.controls.OrbitControls;
import org.joml.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the necessary setup to view a point cloud with orbit controls.
 */
public class ViewManager {

    private final Scene scene;
    private final Camera camera;
    private final Renderer renderer;
    private final OrbitControls controls;

    private GLCanvas glCanvas;
    private FPSAnimator animator;

    private List<View> views;

    private boolean isRunning;

    /**
     * @param canvas is the Java component on which to draw
     */
    public ViewManager(JPanel canvas) {
        views = new ArrayList<>();

        isRunning = false;

        glCanvas = new GLCanvas(new GLCapabilities(GLProfile.get(GLProfile.GL4)));

        canvas.add(glCanvas, BorderLayout.CENTER);

        glCanvas.setVisible(true);

        camera = new OrthographicCamera(-glCanvas.getWidth(), glCanvas.getWidth(), -glCanvas.getHeight(),
                glCanvas.getHeight(), 0.1f, 1000.0f);

        camera.translate(new Vector3f(-10.0f, 0.0f, 0.0f));
        camera.zoom(400);

        scene = new Scene();

        scene.add(new AxisHelper(8.0f));

        renderer = new Renderer(scene, camera);
        controls = new OrbitControls(camera);

        controls.setZoomSpeed(3.0f);

        animator = new FPSAnimator(glCanvas, 60);
    }

    public GLCanvas getCanvas() {
        return glCanvas;
    }

    public void start() {
        if (!isRunning) {
            glCanvas.setVisible(true);
            glCanvas.addGLEventListener(renderer);
            glCanvas.addMouseListener(controls);
            glCanvas.addMouseMotionListener(controls);
            glCanvas.addMouseWheelListener(controls);
            animator.start();

            isRunning = true;
        }
    }

    public void stop() {
        if (isRunning) {
            glCanvas.setVisible(false);
            glCanvas.removeGLEventListener(renderer);
            glCanvas.removeMouseListener(controls);
            glCanvas.removeMouseMotionListener(controls);
            glCanvas.removeMouseWheelListener(controls);
            animator.stop();

            isRunning = false;
        }
    }

    public void add(View view) {
        views.add(view);
        update();
    }

    public void remove(View view) {
        views.remove(view);
        update();
    }

    public void remove(int i) {
        views.remove(i);
        update();
    }

    public void clear() {
        views.clear();
        update();
    }

    public void setViewAt(int i, int view) {
        views.get(i).setView(view);
        update();
    }

    public void setVisibilityAt(int i, boolean isVisible) {
        boolean wasRunning = isRunning;

        stop();
        scene.get(i).setVisible(isVisible);

        if (wasRunning) start();
    }

    private void update() {
        boolean wasRunning = isRunning;

        stop();
        scene.clear();

        views.forEach(view -> {
            Geometry geometry = view.getGeometry();

            geometry.setVisible(false);
            scene.add(geometry);
        });

        if (wasRunning) start();
    }

}
