package com.movlad.semviz.core.graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * Basic text file reader, retrieving vertex buffer data separated
 * by spaces and lines.
 */
public class VertexDataReader {
	
	private String path;
	private float[] dataArray;
	private FloatBuffer dataBuffer;

	public VertexDataReader(String path) { this.path = path; }
	
	/**
	 * @return the data as an array of floats
	 */
	public float[] getDataArray() { return dataArray; }
	
	/**
	 * @return the data as a float buffer
	 */
	public FloatBuffer getDataBuffer() { return dataBuffer; }
	
	/**
	 * Reads the file containing the buffer data.
	 * 
	 * @throws IOException if the file is non existent or there are problems opening
	 * the different buffers
	 */
	public void read() throws IOException {
		List<Float> dataList = new ArrayList<>();
		
		File file = new File(path);
		InputStream inputStream = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		
		while ((line = reader.readLine()) != null) {
			String[] elements = line.split(" ");
			
			for (String element : elements) {
				if (NumberUtils.isParsable(element))
					dataList.add(Float.parseFloat(element));
			}
		}
		
		reader.close();
		
		dataArray = new float[dataList.size()];
		
		for (int i = 0; i < dataArray.length; i++) {
			dataArray[i] = dataList.get(i);
		}
		
		dataBuffer = FloatBuffer.wrap(dataArray);
	}
	
}
