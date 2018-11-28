package com.movlad.semviz.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Keeps structural and size-related information concerning the layout of a {@code VertexBufferObject},
 * such as position, color, texture coordinates etc. .
 */
public class BufferLayout implements Iterable<BufferAttribute> {

	private List<BufferAttribute> attributes = new ArrayList<>();
	private int stride = 0; 
	
	public List<BufferAttribute> getAttributes() { return attributes; }

	/**
	 * @return sum of the sizes of all the layout elements
	 */
	public int getStride() { return stride; }
	
	public BufferAttribute get(int i) { return attributes.get(0); }
	
	public int size() { return attributes.size(); }
	
	/**
	 * Adds a layout component which is a sequence of floats.
	 * 
	 * @param count is the length of the sequence
	 * @param normalized is true if the sequence elements are normalized
	 */
	public void push(String name, int count, boolean normalized) {
		BufferAttribute attribute = new BufferAttribute(name, count, normalized);
		
		attributes.add(attribute);
		stride += attribute.getSize();
	}
	
	@Override
	public Iterator<BufferAttribute> iterator() {
		return attributes.iterator();
	}
	
}
