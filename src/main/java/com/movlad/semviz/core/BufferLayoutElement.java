package com.movlad.semviz.core;

import com.jogamp.opengl.GL;

/**
 * Class gathering information about one element of a buffer attribute layout, such
 * as position, color, texture coordinates etc. .
 */
class BufferLayoutElement {
	
	private int type;
	private int count;
	private boolean normalized;
	
	/**
	 * Constructor.
	 * 
	 * @param type is the GLenum corresponding to the type of the layout element (<i>e.g.</i> {@code GL_FLOAT})
	 * @param count is the number of components of the layout element (<i>e.g.</i> a position has three elements,
	 * {@code x}, {@code y} and {@code z})
	 * @param normalized is true if the value is between 0 and 1
	 */
	public BufferLayoutElement(int type, int count, boolean normalized) {
		if (count < 0) 
			throw new IllegalArgumentException("The buffer layout element count cannot be negative.");
		
		this.type = type;
		this.count = count;
		this.normalized = normalized;
	}
	
	public int getType() { return type; }
	
	public int getCount() { return count; }
	
	public boolean isNormalized() { return normalized; }
	
	/**
	 * @return the total size of the layout element in bytes ({@code count * sizeof(type)})
	 */
	public int getSize() {
		switch (type) {
		case GL.GL_UNSIGNED_INT:
			return count * Integer.BYTES;
			
		case GL.GL_FLOAT:
			return count * Float.BYTES;
			
		case GL.GL_UNSIGNED_BYTE:
			return count * Byte.BYTES;
		}
		
		return 0;
	}
	
}
