package com.movlad.semviz;

import java.io.IOException;
import java.nio.FloatBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;

public class TestEventListener implements GLEventListener {

	private VertexArrayObject vao;
	private VertexBufferObject vbo;
	private ShaderProgram program;
	
	@Override
	public void init(GLAutoDrawable drawable) {
		GL4 gl = drawable.getGL().getGL4();
		
		// Shaders
		
		try {
			program = new ShaderProgram(gl, ClassLoader.getSystemClassLoader()
					.getResource("shaders/shader.glsl").getFile());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// Buffers
		
		float[] vertexArr = {
			-0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f,
			0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f,
			0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f
		};
		
		FloatBuffer vertices = FloatBuffer.wrap(vertexArr);
				
		try {
			vao = new VertexArrayObject(gl);
			vbo = new VertexBufferObject(gl, vertices, vertices.capacity() *  Float.BYTES, GL.GL_STATIC_DRAW);
			
			BufferLayout layout = new BufferLayout();
			
			layout.pushFloats(3, false);
			layout.pushFloats(3, false);
			
			vao.addBuffer(vbo, layout);
		} catch (GLException e) {
			e.printStackTrace();
		}
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
		
		gl.glDrawArrays(GL4.GL_TRIANGLES, 0, 3);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
	}
	
}

