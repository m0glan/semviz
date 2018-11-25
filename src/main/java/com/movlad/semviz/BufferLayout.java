package com.movlad.semviz;

import java.util.ArrayList;
import java.util.Iterator;

import com.jogamp.opengl.GL;

public class BufferLayout implements Iterable<BufferLayoutElement> {

	private ArrayList<BufferLayoutElement> elements = new ArrayList<>();
	private int stride = 0; 
	
	public int getStride() { return stride; }
	
	public BufferLayoutElement get(int i) { return elements.get(i); }
	
	public int size() { return elements.size(); }
	
	public void addInt(int count, boolean normalized) {
		BufferLayoutElement e = new BufferLayoutElement(GL.GL_UNSIGNED_INT, count, normalized);
		
		elements.add(e);
		stride += e.getSize();
	}
	
	public void addFloat(int count, boolean normalized) {
		BufferLayoutElement e = new BufferLayoutElement(GL.GL_FLOAT, count, normalized);
		
		elements.add(e);
		stride += e.getSize();
	}
	
	public void addByte(int count, boolean normalized) {
		BufferLayoutElement e = new BufferLayoutElement(GL.GL_UNSIGNED_BYTE, count, normalized);
		
		elements.add(e);
		stride += e.getSize();
	}
	
	@Override
	public Iterator<BufferLayoutElement> iterator() {
		return elements.iterator();
	}
	
}
