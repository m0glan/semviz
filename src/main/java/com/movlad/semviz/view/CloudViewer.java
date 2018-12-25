/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.view;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import com.movlad.semviz.core.graphics.engine.AxisHelper;
import com.movlad.semviz.core.graphics.engine.Camera;
import com.movlad.semviz.core.graphics.engine.CloudGeometryExtractor;
import com.movlad.semviz.core.graphics.engine.Geometry;
import com.movlad.semviz.core.graphics.engine.OrthographicCamera;
import com.movlad.semviz.core.graphics.engine.Renderer;
import com.movlad.semviz.core.graphics.engine.Scene;
import com.movlad.semviz.core.graphics.engine.controls.OrbitControls;
import com.movlad.semviz.core.math.geometry.PointCloud;
import org.joml.Vector3f;

/**
 * Provides the necessary setup to view a point cloud with orbit controls.
 */
public class CloudViewer {
    
    // GL
    
    private GLCapabilities glCaps;
    private GLProfile glProfile;
    private GLWindow glWindow;
    private NewtCanvasAWT glCanvas;
    private FPSAnimator animator;
    
    // Display data
    
    private Scene scene;
    private Camera camera;
    private Renderer renderer;
    private OrbitControls orbit;
    private int oldViewSelection;
    
    // Cloud to display
    
    private PointCloud cloud;
    
    /**
     * @param width is the width of the viewer
     * @param height is the height of the viewer
     */
    public CloudViewer(int width, int height) {
        oldViewSelection = -1;
        
        // GL Init
        
        glProfile = GLProfile.get(GLProfile.GL4);
        glCaps = new GLCapabilities(glProfile);
        glWindow = GLWindow.create(glCaps);
        
        glCanvas = new NewtCanvasAWT(glWindow);
        glWindow.setPosition(0, 0);
        glWindow.setSize(width, height);
        
        // Camera, Scene, Renderer, Controls...
        
        camera = new OrthographicCamera(-width, width, -height, height, 0.1f, 1000.0f);
        
        camera.translate(new Vector3f(-10.0f, 0.0f, 0.0f));
        camera.setMinZoom(0.001f);
        camera.setMaxZoom(0.3f);
        camera.zoom(0.9f);
        
        scene = new Scene();
		
        scene.add(new AxisHelper(4.0f));
        
        renderer = new Renderer(scene, camera);
		
        orbit = new OrbitControls(camera);
        
        // Adding event listeners
        
        glWindow.addGLEventListener(renderer);
        glWindow.setVisible(true);
        glWindow.addMouseListener(orbit);
		
	animator = new FPSAnimator(glWindow, 60);
		
        animator.start();
    }
    
    public NewtCanvasAWT getCanvas() { return glCanvas; }
    
    public void setCloud(PointCloud cloud) {
        this.cloud = cloud;
        this.oldViewSelection = -1;
        
        setView(0);
    }
    
    /**
     * @param viewSelection is the view to display the cloud in (<i>e.g.</i> 0 for high resolution base view)
     */
    public void setView(int viewSelection) {
        pauseRendering();
        
        for (int i = 1; i < scene.numChildren(); i++) {
            scene.remove(i);
        }
        
        if (cloud != null) {
            if (viewSelection != oldViewSelection) {
                Geometry geometry;
                CloudGeometryExtractor geometryExtractor = new CloudGeometryExtractor(cloud);

                switch (viewSelection) {
                    case 0:
                        geometry = geometryExtractor.extractBaseGeometry();
                        break;
                    case 1:
                        geometry = geometryExtractor.extractNormalRGBGeometry();
                        break;
                    default:
                        geometry = null;
                        break;
                }

                if (geometry != null) { scene.add(geometry); }

                oldViewSelection = viewSelection;
            }
        }
        
        resumeRendering();
    }
    
    /**
     * Pauses the rendering, thus allowing for scene modification.
     */
    private void pauseRendering() {
        animator.stop();
        glWindow.removeGLEventListener(renderer);
    }
    
    /**
     * Resumes the rendering after scene modification.
     */
    private void resumeRendering() {
        glWindow.addGLEventListener(renderer);
        animator.start();
    }
    
    @Override
    public void finalize() throws Throwable {
        super.finalize();
       
        pauseRendering();
        glWindow.destroy();
    }
    
}
