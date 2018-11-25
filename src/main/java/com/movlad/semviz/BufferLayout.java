package com.movlad.semviz;

import java.util.ArrayList;
import java.util.Iterator;

import com.jogamp.opengl.GL4;

/**
 * Keeps structural and size-related information concerning the layout of a {@code VertexBufferObject},
 * such as position, color, texture coordinates etc. .
 */
public class BufferLayout implements Iterable<BufferLayoutElement> {

	private ArrayList<BufferLayoutElement> elements = new ArrayList<>();
	private int stride = 0; 

	/**
	 * @return sum of the sizes of all the layout elements
	 */
	public int getStride() { return stride; }
	
	public BufferLayoutElement get(int i) { return elements.get(i); }
	
	public int size() { return elements.size(); }
	
	/**
	 * Adds a layout component which is a sequence of unsigned integers.
	 * 
	 * @param count is the length of the sequence
	 * @param normalized is true if the sequence elements are normalized
	 */
	public void pushUInts(int count, boolean normalized) {
		BufferLayoutElement e = new BufferLayoutElement(GL4.GL_UNSIGNED_INT, count, normalized);
		
		elements.add(e);
		stride += e.getSize();
	}
	
	/**
	 * Adds a layout component which is a sequence of floats.
	 * 
	 * @param count is the length of the sequence
	 * @param normalized is true if the sequence elements are normalized
	 */
	public void pushFloats(int count, boolean normalized) {
		BufferLayoutElement e = new BufferLayoutElement(GL4.GL_FLOAT, count, normalized);
		
		elements.add(e);
		stride += e.getSize();
	}
	
	/**
	 * Adds a layout component which is a sequence of unsigned bytes.
	 * 
	 * @param count is the length of the sequence
	 * @param normalized is true if the sequence elements are normalized
	 */
	public void pushUBytes(int count, boolean normalized) {
		BufferLayoutElement e = new BufferLayoutElement(GL4.GL_UNSIGNED_BYTE, count, normalized);
		
		elements.add(e);
		stride += e.getSize();
	}
	
	@Override
	public Iterator<BufferLayoutElement> iterator() {
		return elements.iterator();
	}
	
}
