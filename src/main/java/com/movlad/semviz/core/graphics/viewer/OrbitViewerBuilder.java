/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.core.graphics.viewer;

import com.jogamp.opengl.util.FPSAnimator;
import com.movlad.semviz.core.graphics.engine.AxisHelper;
import com.movlad.semviz.core.graphics.engine.Camera;
import com.movlad.semviz.core.graphics.engine.Controls;
import com.movlad.semviz.core.graphics.engine.OrbitControls;
import com.movlad.semviz.core.graphics.engine.OrthographicCamera;
import com.movlad.semviz.core.graphics.engine.Renderer;
import com.movlad.semviz.core.graphics.engine.Scene;
import org.joml.Vector3f;

/**
 *
 * @author Vlad
 */
public class OrbitViewerBuilder implements ViewerBuilder {

    private Viewer viewer;

    private Camera camera;
    private Scene scene;
    private Renderer renderer;
    private Controls controls;
    private FPSAnimator animator;

    private int width;
    private int height;

    public OrbitViewerBuilder(int width, int height) {
        viewer = new Viewer();
    }

    @Override
    public Viewer getViewer() {
        return viewer;
    }

    @Override
    public void buildCamera() {
        camera = new OrthographicCamera(-width, width,
                -height, height, 0.1f, 1000.0f);

        camera.translate(new Vector3f(-10.0f, 0.0f, 0.0f));
        camera.zoom(400);
        viewer.setCamera(camera);
    }

    @Override
    public void buildScene() {
        scene = new Scene();

        scene.add(new AxisHelper(8.0f));
        viewer.setHelper((AxisHelper) scene.get(0));
        viewer.setScene(scene);
    }

    @Override
    public void buildRenderer() {
        renderer = new Renderer(scene, camera);

        viewer.setRenderer(renderer);
    }

    @Override
    public void buildControls() {
        controls = new OrbitControls(camera);

        controls.setZoomSpeed(3.0f);
        viewer.setControls(controls);
    }

    @Override
    public void buildAnimator() {
        animator = new FPSAnimator(viewer.getGlCanvas(), 60);

        viewer.setAnimator(animator);
    }

}
