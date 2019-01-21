/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.core.graphics.viewer;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.movlad.semviz.core.graphics.engine.AxisHelper;
import com.movlad.semviz.core.graphics.engine.Camera;
import com.movlad.semviz.core.graphics.engine.Controls;
import com.movlad.semviz.core.graphics.engine.Object3d;
import com.movlad.semviz.core.graphics.engine.Renderer;
import com.movlad.semviz.core.graphics.engine.Scene;

public class Viewer {

    private Scene scene;
    private AxisHelper helper;
    private Camera camera;
    private Renderer renderer;
    private Controls controls;

    private GLCanvas glCanvas;

    private FPSAnimator animator;

    private boolean isRunning;

    public Viewer() {
        glCanvas = new GLCanvas(new GLCapabilities(GLProfile.get(GLProfile.GL4)));
    }

    public GLCanvas getGlCanvas() {
        return glCanvas;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setHelper(AxisHelper helper) {
        this.helper = helper;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void setControls(Controls controls) {
        this.controls = controls;
    }

    public void setAnimator(FPSAnimator animator) {
        this.animator = animator;
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

    public void addObject(Object3d object) {
        boolean wasRunning = isRunning;

        stop();

        scene.add(object);

        if (wasRunning) {
            start();
        }
    }

    public void removeObject(Object3d object) {
        boolean wasRunning = isRunning;

        stop();

        scene.remove(object);

        if (wasRunning) {
            start();
        }
    }

    public void removeObject(int i) {
        boolean wasRunning = isRunning;

        stop();

        scene.remove(i);

        if (wasRunning) {
            start();
        }
    }

    public void setObjectVisibility(int i, boolean isVisible) {
        boolean wasRunning = isRunning;

        stop();
        scene.get(i).setVisible(isVisible);

        if (wasRunning) {
            start();
        }
    }

    public void clearScene() {
        boolean wasRunning = isRunning;

        stop();

        scene.clear();

        if (helper != null) {
            scene.add(helper);
        }

        if (wasRunning) {
            start();
        }
    }

}
