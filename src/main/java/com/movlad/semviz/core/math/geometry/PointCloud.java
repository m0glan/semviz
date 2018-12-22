package com.movlad.semviz.core.math.geometry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Collection of points.
 */
public class PointCloud implements Iterable<Point3dCN> {

	private List<Point3dCN> points = new ArrayList<>();

	public void add(Point3dCN point) {
		points.add(point);
	}
	
	public void remove(Point3dCN point) {
		points.remove(point);
	}
	
	public void remove(int i) {
		points.remove(i);
	}
	
	public Point3dCN get(int i) {
		return points.get(i);
	}
	
	@Override
	public Iterator<Point3dCN> iterator() {
		return points.iterator();
	}
	
}
