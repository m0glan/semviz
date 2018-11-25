package com.movlad.semviz;

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
		
		String[] vertexShaderSource =  new String[] { 
			"#version 400 core\r\n" +
			"in vec3 position;\r\n" +
			"in vec3 in_color;\r\n" +
			"out vec3 out_color;\r\n" +
			"\r\n" +
			"void main()\r\n" +
			"{\r\n" +
			"	gl_Position = vec4(position, 1.0f);\r\n" +
			"   out_color = in_color;\r\n" +
			"}\r\n"
		};
		
		String[] fragShaderSource = new String[] { 
			"#version 400 core\r\n" +
			"in vec3 out_color;\r\n" +
			"out vec4 frag_color;\r\n" +
			"\r\n" +
			"void main()\r\n" +
			"{\r\n" +
			"	frag_color = vec4(out_color, 1.0f);\r\n" +
			"}\r\n"
		};
		
		program = new ShaderProgram(gl, vertexShaderSource, fragShaderSource);
		
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

