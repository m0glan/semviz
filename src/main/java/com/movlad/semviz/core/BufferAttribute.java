package com.movlad.semviz.core;

/**
 * Class representing one attribute of a buffer layout, such
 * as position, color, texture coordinates etc. .
 */
class BufferAttribute {
	
	private String name;
	private int count;
	private boolean normalized;
	
	/**
	 * Constructor.
	 * 
	 * @param name is the name of the attribute as present in the shader
	 * @param data is the data that the attribute contains
	 * @param normalized is true if the value is between 0 and 1
	 */
	public BufferAttribute(String name, int count, boolean normalized) {
		this.name = name;
		this.count = count;
		this.normalized = normalized;
	}
	
	public String getName() { return name; }
	
	public int getCount() { return count; }
	
	public boolean isNormalized() { return normalized; }
	
	/**
	 * @return the total size of the layout element in bytes ({@code count * sizeof(type)})
	 */
	public int getSize() {
		return Float.BYTES * count;
	}
	
}
