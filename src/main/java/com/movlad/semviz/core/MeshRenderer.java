package com.movlad.semviz.core;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;

/**
 * Renderer used for rendering basic geometries (triangles).
 */
public class MeshRenderer extends Renderer {

	private List<VertexArrayObject> vertexArrayObjects = new ArrayList<>();
	
	public MeshRenderer(Scene scene, Camera camera) {
		super(scene, camera);
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		gl = (GL4) drawable.getGL();
		
		gl.glEnable(GL4.GL_DEPTH_TEST);
		
		try {
			program = new ShaderProgram(gl, this.getClass().getClassLoader()
					.getResource("shaders/shader.glsl").getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		program.use();
		
		initVertexArrays(gl);
	}
	
	/**
	 * Initializes the vertex arrays for the drawable geometries.
	 * 
	 * @param gl is the context
	 */
	private void initVertexArrays(GL4 gl) {
		for (int i = 0; i < scene.size(); i++) {
			VertexArrayObject vertexArrayObject = new VertexArrayObject(gl);
			
			vertexArrayObjects.add(vertexArrayObject);
			vertexArrayObject.bind();
			
			Buffer dataBuffer = FloatBuffer.wrap(scene.get(i).getData());
			
			VertexBufferObject vertexBufferObject = new VertexBufferObject(gl, dataBuffer, dataBuffer.capacity() 
					* Float.BYTES, GL4.GL_STATIC_DRAW);
			BufferLayout layout = scene.get(i).getLayout();
			
			vertexArrayObject.addBuffer(vertexBufferObject, layout);
		}
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		GL4 gl = (GL4) drawable.getGL();
		
		gl.glBindVertexArray(0);
		
		for (VertexArrayObject vertexArrayObject : vertexArrayObjects) {
			vertexArrayObject.delete();
		}
		
		program.delete();
	}

	@Override
	protected final void draw() {
		for (int i = 0; i < scene.size(); i++) {
			if (scene.get(i).isVisible()) {
				vertexArrayObjects.get(i).bind();
				
				int modelUniformLocation = gl.glGetUniformLocation(program.getId(), "model");
				int viewUniformLocation = gl.glGetUniformLocation(program.getId(), "view");
				int projectionUniformLocation = gl.glGetUniformLocation(program.getId(), "projection");
				
				FloatBuffer modelBuffer = BufferUtils.createFloatBuffer(16);
				FloatBuffer viewBuffer = BufferUtils.createFloatBuffer(16);
				FloatBuffer projectionBuffer = BufferUtils.createFloatBuffer(16);
				
				Matrix4f model = new Matrix4f();
				
				model.translate(scene.get(i).getPosition());
				
				model.get(modelBuffer);
				camera.getMatrixWorld().get(viewBuffer);
				camera.getProjectionMatrix().get(projectionBuffer);
				
				gl.glUniformMatrix4fv(modelUniformLocation, 1, false, modelBuffer);
				gl.glUniformMatrix4fv(viewUniformLocation, 1, false, viewBuffer);
				gl.glUniformMatrix4fv(projectionUniformLocation, 1, false, projectionBuffer);
				
				gl.glDrawArrays(GL4.GL_TRIANGLES, 0, scene.get(i).getData().length);
			}
		}
	}

}
