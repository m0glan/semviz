package com.movlad.semviz.core;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

/**
 * Renders a scene with a camera on a canvas.
 */
public abstract class Renderer implements GLEventListener {
	
	protected Scene scene;
	protected Camera camera;
	
	protected GL4 gl;
	protected ShaderProgram program;
	
	public Renderer(Scene scene, Camera camera) {
		this.scene = scene;
		this.camera = camera;
	}
	
	@Override
	public final void display(GLAutoDrawable drawable) {
		GL4 gl = (GL4) drawable.getGL();
		
		gl.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
		
		program.use();
		
		draw();
	}
	
	/**
	 * Renderer specific drawing method.
	 */
	protected abstract void draw();
	
	@Override
	public final void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO
	}

}
