package com.movlad.semviz.core.graphics.engine;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.movlad.semviz.core.graphics.ShaderProgram;
import com.movlad.semviz.core.graphics.VertexArrayObject;
import com.movlad.semviz.core.graphics.VertexBufferObject;

/**
 * Renders a scene with a camera on a canvas.
 */
public class Renderer implements GLEventListener {
	
	protected Scene scene;
	protected Camera camera;
	
	protected GL4 gl;
	protected ShaderProgram program;
	
	private List<Renderable> renderables = new ArrayList<>();
	
	public Renderer(Scene scene, Camera camera) {
		this.scene = scene;
		this.camera = camera;
	}
	
	@Override
	public final void init(GLAutoDrawable drawable) {
		gl = (GL4) drawable.getGL();
		
		gl.glEnable(GL4.GL_DEPTH_TEST);
		
		try {
			program = new ShaderProgram(gl, this.getClass().getClassLoader()
					.getResource("shaders/shader.glsl").getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		program.use();
	}
	
	@Override
	public final void display(GLAutoDrawable drawable) {
		GL4 gl = (GL4) drawable.getGL();
		
		gl.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
		
		program.use();
		resetVertexArrays();
		initVertexArrays(scene);
		render();
	}
	
	@Override
	public void dispose(GLAutoDrawable drawable) {
		resetVertexArrays();
		program.delete();
	}
	
	@Override
	public final void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO
	}
	
	/**
	 * Draws renderables on the screen.
	 */
	private void render() {
		for (Renderable renderable : renderables) {
			if (renderable.getObject().isVisible()) {
				if (renderable.getObject() instanceof Geometry) {
					Geometry geometry = (Geometry) renderable.getObject();
					
					renderable.getVertexArrayObject().bind();
					
					setMatrixUniforms(renderable);
					
					gl.glDrawArrays(geometry.getDrawingMode(), 0, geometry.getVertexCount());
				}
			}
		}
	}
	
	/**
	 * Sets the matrix uniforms for the currently drawn renderable.
	 * 
	 * @param renderable is the currently draw renderable
	 */
	private void setMatrixUniforms(Renderable renderable) {
		int modelUniformLocation = gl.glGetUniformLocation(program.getId(), "model");
		int viewUniformLocation = gl.glGetUniformLocation(program.getId(), "view");
		int projectionUniformLocation = gl.glGetUniformLocation(program.getId(), "projection");
		
		FloatBuffer modelBuffer = BufferUtils.createFloatBuffer(16);
		FloatBuffer viewBuffer = BufferUtils.createFloatBuffer(16);
		FloatBuffer projectionBuffer = BufferUtils.createFloatBuffer(16);
		
		renderable.getObject().getMatrixWorld().get(modelBuffer);
		camera.getMatrixWorld().get(viewBuffer);
		camera.getProjectionMatrix().get(projectionBuffer);
		
		gl.glUniformMatrix4fv(modelUniformLocation, 1, false, modelBuffer);
		gl.glUniformMatrix4fv(viewUniformLocation, 1, false, viewBuffer);
		gl.glUniformMatrix4fv(projectionUniformLocation, 1, false, projectionBuffer);
	}
	
	/**
	 * Initializes the vertex arrays for the drawable geometries.
	 * 
	 * @param gl is the context
	 */
	private void initVertexArrays(Object3d object) {
		for (Object3d child : object) {
			if (child instanceof Geometry) {
				Geometry geometry = (Geometry) child;
				
				VertexArrayObject vertexArrayObject = new VertexArrayObject(gl);
				
				vertexArrayObject.bind();
				
				Buffer dataBuffer = FloatBuffer.wrap(geometry.getData());
				VertexBufferObject vertexBufferObject = new VertexBufferObject(gl, dataBuffer, dataBuffer.capacity() 
						* Float.BYTES, GL4.GL_STATIC_DRAW);
				
				vertexArrayObject.addBuffer(vertexBufferObject, geometry.getLayout());
				
				renderables.add(new Renderable(child, vertexArrayObject));
			}
			
			initVertexArrays(child);
		}
	}
	
	/**
	 * Binds vertex array to 0, deletes vertex arrays and reinitializes the {@code renderables} array list
	 */
	private void resetVertexArrays() {
		gl.glBindVertexArray(0);
		
		for (Renderable renderable : renderables) {
			renderable.getVertexArrayObject().delete();
		}
		
		renderables.clear();
	}

}
