package com.movlad.semviz.core.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.movlad.semviz.core.math.geometry.Point3dCN;
import com.movlad.semviz.core.math.geometry.PointCloud;

/**
 * Generates a float buffer from a point cloud text file.
 */
public class PointCloudLoaderText {

	private String path;
	private boolean normalsIncluded;
	
	private int numInvalidLines;
	
	private PointCloud pointCloud;
	
	/**
	 * @param path is the path of the cloud {@code .txt} file
	 * @param normalsIncluded is true if the file also contains the normal vectors in each point
	 */
	public PointCloudLoaderText(String path, boolean normalsIncluded) {
		this.path = path;
		this.normalsIncluded = normalsIncluded;
	}
	
	public void setPath(String path) { this.path = path; }
	
	public void setNormalsIncluded(boolean normalsIncluded) { this.normalsIncluded = normalsIncluded; }
	
	public int getNumInvalidLines() { return numInvalidLines; }
	
	/**
	 * @return the point cloud obtained as a result of running the {@code load() } operation
	 */
	public PointCloud getCloud() { return pointCloud; }
	
	/**
	 * Loads the contents of the file.
	 * @throws IOException
	 */
	public void load() throws IOException {
		int numFields;
		
		numInvalidLines = 0;
		
		if (normalsIncluded) numFields = 9; else numFields = 6;
		
		BufferedReader reader = new BufferedReader(new FileReader(path));
		
		pointCloud = new PointCloud();
		
		for (String line; (line = reader.readLine()) != null;) {
			String[] attributes = line.split("\\t");
			
			if (attributes.length == numFields) {
				Point3dCN point = new Point3dCN();
				
				point.x = Float.parseFloat(attributes[0]);
				point.y = Float.parseFloat(attributes[1]);
				point.z = Float.parseFloat(attributes[2]);
				
				point.r = Short.parseShort(attributes[3]);
				point.g = Short.parseShort(attributes[4]);
				point.b = Short.parseShort(attributes[5]);
				
				if (numFields == 9) {
					point.normalX = Float.parseFloat(attributes[6]);
					point.normalY = Float.parseFloat(attributes[7]);
					point.normalZ = Float.parseFloat(attributes[7]);
				}
				
				pointCloud.add(point);
			} else {
				numInvalidLines++;
			}
		}
		
		reader.close();
	}
	
}
