package com.movlad.semviz.core.graphics.engine;

import com.jogamp.opengl.GL4;
import com.movlad.semviz.core.graphics.BufferLayout;
import com.movlad.semviz.core.math.geometry.Point3dCN;
import com.movlad.semviz.core.math.geometry.PointCloud;

public class CloudGraphicDataExtractor {

	protected PointCloud cloud;
	protected Point3dCN centroid;
	
	private Geometry initialViewGeometry = null;
	private Geometry normalViewGeometry = null;
	private Geometry hullViewGeometry = null;
	
	public CloudGraphicDataExtractor(PointCloud cloud) {
		this.cloud = cloud;
		
		calculateCentroid();
	}
	
	public Geometry getInitialViewGeometry() {
		if (initialViewGeometry == null) {
			float[] data = new float[cloud.size() * 6];
			int i = 0;;
			
			for (Point3dCN point : cloud) {
				data[i] = (float) (point.x - centroid.x);
				data[i + 1] = (float) (point.y - centroid.y);
				data[i + 2] = (float) (point.z - centroid.z);
				
				data[i + 3] = (float) point.r;
				data[i + 4] = (float) point.g;
				data[i + 5] = (float) point.b;
				
				i += 6;
			}
			
			BufferLayout layout = new BufferLayout();
			
			layout.push("position", 3, true);
			layout.push("color", 3, true);
			
			initialViewGeometry = new Geometry(data, layout) {
				
				@Override
				public int getDrawingMode() {
					return GL4.GL_POINTS;
				}
				
			};
		}
		
		return initialViewGeometry;
	}
	
	public Geometry getNormalViewGeometry() {
		if (normalViewGeometry == null) {
			float[] data = new float[cloud.size() * 6];
			int i = 0;
			
			for (Point3dCN point : cloud) {
				data[i] = (float) (point.x - centroid.x);
				data[i + 1] = (float) (point.y - centroid.y);
				data[i + 2] = (float) (point.z - centroid.z);
				
				data[i + 3] = (float) point.normalX;
				data[i + 4] = (float) point.normalY;
				data[i + 5] = (float) point.normalZ;
				
				i += 6;
			}
			
			BufferLayout layout = new BufferLayout();
			
			layout.push("position", 3, true);
			layout.push("color", 3, false);
			
			normalViewGeometry = new Geometry(data, layout) {
				
				@Override
				public int getDrawingMode() {
					return GL4.GL_POINTS;
				}
				
			};
		}
		
		return normalViewGeometry;
	}
	
	public Geometry hullViewGeometry() { return hullViewGeometry; }
	
	private void calculateCentroid() {
		centroid = new Point3dCN();
		
		for (Point3dCN point : cloud) {
			centroid.x += point.x;
			centroid.y += point.y;
			centroid.z += point.z;
		}
		
		centroid.x /= cloud.size();
		centroid.y /= cloud.size();
		centroid.z /= cloud.size();
	}
	
}
