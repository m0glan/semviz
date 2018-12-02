package com.movlad.semviz.core.graphics.engine;

import com.movlad.semviz.core.graphics.BufferLayout;
import com.movlad.semviz.core.graphics.VertexArrayObject;

/**
 * Basic geometry grouping vertex data and buffer layout.
 */
public class Geometry extends Object3d {

	protected BufferLayout layout;
	protected float[] data;
	protected VertexArrayObject vertexArrayObject;
	
	protected Geometry() { this.setVisible(true); }
	
	/**
	 * Constructor.
	 * 
	 * @param data is the vertex data
	 * @param layout is the layout for vertex data interpretation
	 */
	public Geometry(float[] data, BufferLayout layout) { 
		this.data = data;
		this.layout = layout;
		this.setVisible(true);
	}
	
	public float[] getData() { return data; }
	
	public void setData(float[] data) { this.data = data; }
	
	public int getDataSize() { return data.length * Float.BYTES; }
	
	public int getVertexCount() { return data.length / layout.getAttributes().size(); }
	
	public BufferLayout getLayout() { return layout; }
	
}
