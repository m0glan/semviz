package com.movlad.semviz.core;

/**
 * Basic geometry grouping vertex data and buffer layout.
 */
public class BufferedGeometry extends Object3d {

	private BufferLayout layout;
	private float[] data;
	
	/**
	 * Constructor.
	 * 
	 * @param data is the vertex data
	 * @param layout is the layout for vertex data interpretation
	 */
	public BufferedGeometry(float[] data, BufferLayout layout) { 
		this.data = data;
		this.layout = layout;
		this.setVisible(true);
	}
	
	public float[] getData() { return data; }
	
	public int getDataSize() { return data.length * Float.BYTES; }
	
	public BufferLayout getLayout() { return layout; }
	
}
