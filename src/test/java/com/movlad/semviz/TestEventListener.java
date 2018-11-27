package com.movlad.semviz;

import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.movlad.semviz.core.BufferLayout;
import com.movlad.semviz.core.Camera;
import com.movlad.semviz.core.PerspectiveCamera;
import com.movlad.semviz.core.ShaderProgram;
import com.movlad.semviz.core.VertexArrayObject;
import com.movlad.semviz.core.VertexBufferObject;
import com.movlad.semviz.core.io.VertexDataReader;

public class TestEventListener implements GLEventListener {

	private VertexArrayObject vao;
	private VertexBufferObject vbo;
	private ShaderProgram program;
	private Vector3f[] positions =  {
		new Vector3f(0.0f,  0.0f,  0.0f),
		new Vector3f(2.0f,  5.0f, -15.0f),
		new Vector3f(-1.5f, -2.2f, -2.5f),
		new Vector3f(-3.8f, -2.0f, -12.3f),
		new Vector3f(2.4f, -0.4f, -3.5f),
		new Vector3f(-1.7f,  3.0f, -7.5f),
		new Vector3f(1.3f, -2.0f, -2.5f),
		new Vector3f(1.5f,  2.0f, -2.5f),
		new Vector3f(1.5f,  0.2f, -1.5f),
		new Vector3f(-1.3f,  1.0f, -1.5f)
	};
	private Camera camera = new PerspectiveCamera(45.0f, 800/600, 0.1f, 100.0f);
	private Matrix4f model = new Matrix4f();
	int modelUniformLocation;
	int viewUniformLocation;
	int projectionUniformLocation;
	
	@Override
	public void init(GLAutoDrawable drawable) {
		GL4 gl = drawable.getGL().getGL4();
		
		// Shaders
		
		try {
			program = new ShaderProgram(gl, ClassLoader.getSystemClassLoader()
					.getResource("shaders/shader.glsl").getFile());
			
			modelUniformLocation = gl.glGetUniformLocation(program.getId(), "model");
			viewUniformLocation = gl.glGetUniformLocation(program.getId(), "view");
			projectionUniformLocation = gl.glGetUniformLocation(program.getId(), "projection");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// Buffers
		
		VertexDataReader vertexDataReader = new VertexDataReader(ClassLoader.getSystemClassLoader()
				.getResource("data/cube.txt").getFile());
		
		try {
			vertexDataReader.read();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		FloatBuffer vertices = vertexDataReader.getDataBuffer();
				
		try {
			vao = new VertexArrayObject(gl);
			vbo = new VertexBufferObject(gl, vertices, vertices.capacity() *  Float.BYTES, GL.GL_STATIC_DRAW);
			
			BufferLayout layout = new BufferLayout();
			
			layout.pushFloats(3, false);
			
			vao.addBuffer(vbo, layout);
		} catch (GLException e) {
			e.printStackTrace();
		}
		
		camera.translate(new Vector3f(0.0f, 0.0f, -25.0f));
		camera.setTarget(new Vector3f(12.0f, 0.0f, 0.0f));
		//camera.rotateY((float) Math.toRadians(70));
		
		gl.glEnable(GL4.GL_DEPTH_TEST);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		vbo.delete();
		vao.delete();
		program.delete();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL4 gl = drawable.getGL().getGL4();
		
		gl.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
		
		program.use();
		vao.bind();
		
		FloatBuffer viewFloatBuffer = BufferUtils.createFloatBuffer(16);
		FloatBuffer projectionFloatBuffer = BufferUtils.createFloatBuffer(16);
		
		camera.getMatrixWorld().get(viewFloatBuffer);
		camera.getProjectionMatrix().get(projectionFloatBuffer);
		
		gl.glUniformMatrix4fv(viewUniformLocation, 1, false, viewFloatBuffer);
		gl.glUniformMatrix4fv(projectionUniformLocation, 1, false, projectionFloatBuffer);

		for (int i = 0; i < 10; i++)
		{
			model = new Matrix4f();
			
			model.translate(positions[i]);

			/*
			if ((i + 1) % 3 == 0)
				model = model.rotate(50.0f, new Vector3f(0.5f, 1.0f, 0.0f));

			else
				model = model.rotate(20.f * i, new Vector3f(0.5f, 1.0f, 0.0f));*/

			FloatBuffer modelFloatBuffer = BufferUtils.createFloatBuffer(16);
			
			model.get(modelFloatBuffer);
			
			gl.glUniformMatrix4fv(modelUniformLocation, 1, false, modelFloatBuffer);

			gl.glDrawArrays(GL4.GL_TRIANGLES, 0, 36);
		}
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
	}
	
}

