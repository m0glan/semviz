package com.movlad.semviz.core.graphics.engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Collection of 3d geometries to be rendered.
 */
public class Scene implements Iterable<BufferedGeometry> {

	private List<BufferedGeometry> geometries = new ArrayList<>();
	
	public void add(BufferedGeometry object) { 
		geometries.add(object);
	}
	
	public void remove(BufferedGeometry object) {
		geometries.remove(object);
	}
	
	public void remove(int i) {
		geometries.remove(i);
	}
	
	public BufferedGeometry get(int i) { return geometries.get(i); }
	
	public int size() { return geometries.size(); }

	@Override
	public Iterator<BufferedGeometry> iterator() {
		return geometries.iterator();
	}
	
}
