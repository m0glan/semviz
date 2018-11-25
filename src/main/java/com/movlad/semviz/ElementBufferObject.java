package com.movlad.semviz;

import java.nio.Buffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

public class ElementBufferObject {

	private int id;
	private GL4 gl;
	
	public ElementBufferObject(GL4 gl, Buffer data, int count, int usage) {
		this.gl = gl;
		
		IntBuffer intBuffer = IntBuffer.allocate(1);
		
		this.gl.glGenBuffers(GL.GL_ELEMENT_ARRAY_BUFFER, intBuffer);
		
		this.id = intBuffer.get(0);
		
		this.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id);
		this.gl.glBufferData(id, count * Integer.BYTES, data, usage);
	}
	
	public void bind() {
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id);
	}
	
	public void unbind() {
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public void delete() {
		IntBuffer intBuffer = IntBuffer.allocate(1);
		
		intBuffer.put(0, id);
		
		gl.glDeleteBuffers(1, intBuffer);
	}
	
}
