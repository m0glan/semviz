package com.movlad.semviz.core.io;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class PointCloudLoaderTextTest {

	@Test
	void test() {
		PointCloudLoaderText loader = new PointCloudLoaderText(ClassLoader.getSystemClassLoader()
				.getResource("clouds/cloud-1.txt").getFile(), true);
		
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
