package com.movlad.semviz;

import java.nio.FloatBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;

public class EventListener implements GLEventListener {

	@Override
	public void init(GLAutoDrawable drawable) {
		GL4 gl = drawable.getGL().getGL4();
		
		GLProfile profile = GLProfile.get(GLProfile.GL4);
		GLCapabilities caps = new GLCapabilities(profile);
		
		String[] vertexShaderSource =  new String[] { 
			"#version 400 core\r\n" +
			"in vec3 position;\r\n" +
			"\r\n" +
			"void main()\r\n" +
			"{\r\n" +
			"	gl_Position = vec4(position, 1.0f);\r\n" +
			"}\r\n"
		};
		
		String[] fragShaderSource = new String[] { 
			"#version 400 core\r\n" +
			"out vec4 frag_color;\r\n" +
			"\r\n" +
			"void main()\r\n" +
			"{\r\n" +
			"	frag_color = vec4(1.0f, 1.0f, 1.0f, 1.0f);\r\n" +
			"}\r\n"
		};
		
		FloatBuffer vertices = FloatBuffer.allocate(9); 
		float[] vertexArr = {
			-0.5f, -0.5f, 0.0f,
			0.5f, -0.5f, 0.0f,
			0.0f, 0.5f, 0.0f
		};
		
		vertices.put(vertexArr);
				
		try {
			VertexArrayObject vao = new VertexArrayObject(gl);
			VertexBufferObject vbo = new VertexBufferObject(gl, vertices, 6 * Float.BYTES, GL.GL_STATIC_DRAW);
			
			BufferLayout layout = new BufferLayout();
			
			layout.addFloat(3, false);
			vao.addBuffer(vbo, layout);
			
			ShaderProgram shader = new ShaderProgram(gl, vertexShaderSource, fragShaderSource);
			
			shader.use();
			
			gl.glClearColor(0, 0, 0, 1);
		} catch (GLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL4 gl = drawable.getGL().getGL4();
		
		gl.glDrawArrays(GL4.GL_TRIANGLES, 0, 3);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	}

}

