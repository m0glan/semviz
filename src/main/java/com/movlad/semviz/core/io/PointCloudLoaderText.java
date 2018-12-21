package com.movlad.semviz.core.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Vector;

/**
 * Generates a float buffer from a point cloud text file.
 */
public class PointCloudLoaderText {

	private String path;
	private boolean normalsIncluded;
	
	private int numInvalidLines;
	
	private FloatBuffer buffer;
	
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
	 * @return the float buffer obtained as a result of running the {@code load() } operation
	 */
	public FloatBuffer getDataBuffer() { return buffer; }
	
	public void load() throws IOException {
		int numFields;
		
		numInvalidLines = 0;
		
		if (normalsIncluded) numFields = 9; else numFields = 6;
		
		BufferedReader reader = new BufferedReader(new FileReader(path));
		
		Vector<Float> vector = new Vector<>();
		
		for (String line; (line = reader.readLine()) != null;) {
			String[] attributes = line.split("\\t");
			
			if (attributes.length == numFields) {
				for (int i = 0; i < attributes.length; i++) {
					vector.add(Float.valueOf(attributes[i]));
				}
			} else {
				numInvalidLines++;
			}
		}
		
		reader.close();
		
		buffer = FloatBuffer.allocate(vector.size());
		
		for (int i = 0; i < vector.size(); i++) {
			buffer.put(i, vector.get(i));
		}
	}
	
}
