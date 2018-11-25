package com.movlad.semviz;

import com.jogamp.opengl.GL;

class BufferLayoutElement {
	
	private int type;
	private int count;
	private boolean normalized;
	
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
	
	public int getSize() {
		switch (type) {
		case GL.GL_UNSIGNED_INT:
			return count * 4;
			
		case GL.GL_FLOAT:
			return count * 4;
			
		case GL.GL_UNSIGNED_BYTE:
			return count * 2;
		}
		
		return 0;
	}
	
}
