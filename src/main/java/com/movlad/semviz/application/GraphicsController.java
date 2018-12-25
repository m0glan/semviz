/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.application;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.event.awt.AWTMouseAdapter;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.movlad.semviz.core.graphics.engine.AxisHelper;
import com.movlad.semviz.core.graphics.engine.Camera;
import com.movlad.semviz.core.graphics.engine.Geometry;
import com.movlad.semviz.core.graphics.engine.OrthographicCamera;
import com.movlad.semviz.core.graphics.engine.Renderer;
import com.movlad.semviz.core.graphics.engine.Scene;
import com.movlad.semviz.core.graphics.engine.controls.OrbitControls;
import java.awt.event.MouseListener;
import java.util.Observable;
import org.joml.Vector3f;

public class GraphicsController extends Observable {
    
    private static GraphicsController instance = null;
    
    private GLCapabilities glCaps;
    private GLProfile glProfile;
    private GLWindow glWindow;
    private NewtCanvasAWT glCanvas;
    
    private Geometry geometry;
    private Scene scene;
    private Camera camera;
    private Renderer renderer;
    private OrbitControls orbit;
    
    private GraphicsController() { }
    
    public static GraphicsController get() {
        if (instance == null) {
            instance = new GraphicsController();
        }
        
        return instance;
    }
    
    public void init(int width, int height, float near, float far) {
        // GL Init
        
        glProfile = GLProfile.get(GLProfile.GL4);
        glCaps = new GLCapabilities(glProfile);
        glWindow = GLWindow.create(glCaps);
        
        glWindow.setSize(width, height);
        
        // Camera, Scene, Renderer, Controls...
        
        camera = new OrthographicCamera(-width, width, -height, height, near, far);
        
        camera.translate(new Vector3f(-10.0f, 0.0f, 0.0f));
        camera.setMinZoom(0.0001f);
        camera.setMaxZoom(5.0f);
        
        scene = new Scene();
		
        scene.add(new AxisHelper(4.0f));
        
        renderer = new Renderer(scene, camera);
		
        orbit = new OrbitControls(camera);
        
        glWindow.addGLEventListener(renderer);
        glWindow.setVisible(true);
        glWindow.addMouseListener(orbit);
		
	FPSAnimator animator = new FPSAnimator(glWindow, 60);
		
        animator.start();
        
        glCanvas = new NewtCanvasAWT(glWindow);
    }
    
    public NewtCanvasAWT getCanvas() {
        return glCanvas;
    }
    
}
