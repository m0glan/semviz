package com.movlad.semviz;

import org.junit.jupiter.api.Test;

import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

class GLAbstractionTest {
	
	public static GLWindow window = null;

	@Test
	void test() {
		GLProfile.initSingleton();
		
		GLProfile profile = GLProfile.get(GLProfile.GL4);
		GLCapabilities caps = new GLCapabilities(profile);
		
		window = GLWindow.create(caps);
		window.setSize(640, 320);
		window.setResizable(false);
		window.addGLEventListener(new TestEventListener());
		
		window.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {  // handler
			    if(ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
			    	window.setVisible(false);
			    }
			}
		 });
		
		window.setVisible(true);
		
		FPSAnimator animator = new FPSAnimator(window, 60);
		
		animator.start();
		
		while (window.isVisible());
	}

}
