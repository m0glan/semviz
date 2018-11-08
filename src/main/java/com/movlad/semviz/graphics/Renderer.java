package com.movlad.semviz.graphics;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

public class Renderer {

	private static GLWindow window = null;
	
	public static void init() {
		GLProfile.initSingleton();
		
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities caps = new GLCapabilities(profile);
		
		window = GLWindow.create(caps);
		window.setSize(640, 320);
		window.setResizable(false);
		window.addGLEventListener(new EventListener());
		window.setVisible(true);
		
		FPSAnimator animator = new FPSAnimator(window, 60);
		
		animator.start();
	}
	
	public static void main(String[] args) {
		init();
	}
	
}
