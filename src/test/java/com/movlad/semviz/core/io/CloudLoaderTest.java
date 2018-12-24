package com.movlad.semviz.core.io;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class CloudLoaderTest {

	@Test
	void test() {
		CloudLoader loader = new CloudLoader(ClassLoader.getSystemClassLoader()
				.getResource("data/clouds/cloud-2.txt").getFile(), true);
		
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
