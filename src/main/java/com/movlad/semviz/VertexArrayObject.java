package com.movlad.semviz;

import java.nio.IntBuffer;

import com.jogamp.opengl.GL4;

public class VertexArrayObject {

	private int id;
	private int offset;
	private GL4 gl;
	
	public VertexArrayObject(GL4 gl) {
		this.gl = gl;
		
		IntBuffer intBuffer = IntBuffer.allocate(1);
		
		this.gl.glGenVertexArrays(1, intBuffer);
		
		id = intBuffer.get(0);
	}
	
	public void bind() {
		this.gl.glBindVertexArray(id);
	}
	
	public void unbind() {
		this.gl.glBindVertexArray(0);
	}
	
	public void addBuffer(VertexBufferObject vbo, BufferLayout layout) {
		bind();
		vbo.bind();
		
		for (int i = 0; i < layout.size(); i++) {
			BufferLayoutElement e = layout.get(i);
			
			gl.glVertexAttribPointer(i, e.getCount(), e.getType(), e.isNormalized(), layout.getStride(), offset);
			gl.glEnableVertexAttribArray(i);
			offset += e.getSize();
		}
	}
	
}
