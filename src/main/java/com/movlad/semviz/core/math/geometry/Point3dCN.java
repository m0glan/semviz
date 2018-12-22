package com.movlad.semviz.core.math.geometry;

import com.github.quickhull3d.Point3d;

/**
 * Structure containing the color and the normals in addition to a point's coordinates.
 */
public class Point3dCN extends Point3d {
	
	// color
	
	public short r; 
	public short g; 
	public short b;
	
	// normal
	
	public float normalX; 
	public float normalY;  
	public float normalZ;
	
	public Point3dCN() {
		r = g = b = 0;
		normalX = normalY = normalZ = 1;
	}
	
	public Point3dCN(short r, short g, short b, float normalX, float normalY, float normalZ) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.normalX = normalX;
		this.normalY = normalY;
		this.normalZ = normalZ;
	}
	
}
