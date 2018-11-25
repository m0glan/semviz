package com.movlad.semviz;

import java.nio.IntBuffer;

import com.jogamp.opengl.GL4;

/**
 * Stores the layout for one or more {@code VertexBufferObject} instances.
 */
public class VertexArrayObject {

	private int id;
	private int offset;
	private GL4 gl;
	
	/**
	 * Constructor.
	 * 
	 * @param gl is the context
	 */
	public VertexArrayObject(GL4 gl) {
		this.gl = gl;
		
		IntBuffer intBuffer = IntBuffer.allocate(1);
		
		this.gl.glGenVertexArrays(1, intBuffer);
		
		id = intBuffer.get(0);
	}
	
	public int getId() { return id; }
	
	/**
	 * Binds this vertex array object.
	 */
	public void bind() {
		gl.glBindVertexArray(id);
	}
	
	/**
	 * Binds the vertex array object 0.
	 */
	public void unbind() {
		gl.glBindVertexArray(0);
	}
	
	/**
	 * Deletes this vertex array object.
	 */
	public void delete() {
		IntBuffer intBuffer = IntBuffer.allocate(1);
		
		intBuffer.put(0, id);
		
		gl.glDeleteVertexArrays(1, intBuffer);
	}
	
	/**
	 * Stores the layout information for a given instance of {@code VertexBufferObject}.
	 * 
	 * @param vbo is the vertex buffer object
	 * @param layout is its layout
	 */
	public void addBuffer(VertexBufferObject vbo, BufferLayout layout) {
		bind();
		vbo.bind();
		
		for (int i = 0; i < layout.size(); i++) {
			BufferLayoutElement e = layout.get(i);
			
			gl.glEnableVertexAttribArray(i);
			gl.glVertexAttribPointer(i, e.getCount(), e.getType(), e.isNormalized(), layout.getStride(), offset);
			offset += e.getSize();
		}
	}
	
}
