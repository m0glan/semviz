package com.movlad.semviz;

import java.nio.Buffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

public class VertexBufferObject {

	private int id;
	private GL4 gl;
	
	public VertexBufferObject(GL4 gl, Buffer data, int size, int usage) {
		this.gl = gl;
		
		IntBuffer intBuffer = IntBuffer.allocate(1);
		
		this.gl.glGenBuffers(1, intBuffer);
		
		this.id = intBuffer.get(0);
		
		this.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id);
		this.gl.glBufferData(GL.GL_ARRAY_BUFFER, size, data, usage);
	}
	
	public int getId() { return id; }
	
	public GL4 getGL() { return gl; }
	
	public void bind() {
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id);
	}
	
	public void unbind() {
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER,  0);
	}
	
	public void delete() {
		IntBuffer intBuffer = IntBuffer.allocate(1);
		
		intBuffer.put(0, id);
		
		gl.glDeleteBuffers(1, intBuffer);
	}
	
}
