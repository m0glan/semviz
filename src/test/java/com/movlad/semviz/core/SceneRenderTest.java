package com.movlad.semviz.core;

import java.io.IOException;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import com.movlad.semviz.core.BufferLayout;
import com.movlad.semviz.core.BufferedGeometry;
import com.movlad.semviz.core.Camera;
import com.movlad.semviz.core.MeshRenderer;
import com.movlad.semviz.core.Renderer;
import com.movlad.semviz.core.Scene;
import com.movlad.semviz.core.io.VertexDataReader;

class SceneRenderTest {
	
	private static GLWindow window = null;
	private static GLProfile profile;
	private static GLCapabilities caps;
	
	private static float[] vertexData;
	private static BufferLayout layout;
	
	private static Vector3f[] geometryPositions = {
		new Vector3f(0.0f,  0.0f,  0.0f),
		new Vector3f(2.0f,  5.0f, -15.0f),
		new Vector3f(-1.5f, -2.2f, -2.5f),
		new Vector3f(-3.8f, -2.0f, -12.3f),
		new Vector3f(2.4f, -0.4f, -3.5f),
		new Vector3f(-1.7f,  3.0f, -7.5f),
		new Vector3f(1.3f, -2.0f, -2.5f),
		new Vector3f(1.5f,  2.0f, -2.5f),
		new Vector3f(1.5f,  0.2f, -1.5f),
		new Vector3f(-1.3f,  1.0f, -1.5f)
	};
	
	private static Scene scene;
	private static Camera camera;
	private static Renderer renderer;

	@Test
	void test() {
		initContext();
		loadVertexData();
		initRender();
		
		window = GLWindow.create(caps);
		window.setSize(640, 320);
		window.setResizable(false);
		window.addGLEventListener(renderer);
		
		window.setVisible(true);
		
		FPSAnimator animator = new FPSAnimator(window, 60);
		
		animator.start();
		
		// while (window.isVisible());
	}
	
	private static void initContext() {
		GLProfile.initSingleton();
		
		profile = GLProfile.get(GLProfile.GL4);
		caps = new GLCapabilities(profile);
	}
	
	private static void loadVertexData() {
		VertexDataReader vertexDataReader = new VertexDataReader(ClassLoader.getSystemClassLoader()
				.getResource("data/cube.txt").getFile());
		
		try {
			vertexDataReader.read();
			
			vertexData = vertexDataReader.getDataArray();
			layout = new BufferLayout();
			
			layout.push("position", 3, false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private static void initRender() {
		scene = new Scene();
		
		for (int i = 0; i < geometryPositions.length; i++) {
			BufferedGeometry geometry = new BufferedGeometry(vertexData, layout);
			
			geometry.setPosition(geometryPositions[i]);
			scene.add(geometry);
		}
		
		camera = new OrthogonalCamera(-20, 20, -20, 20, 0.1f, 1000.0f);
		
		camera.translate(new Vector3f(0.0f, 0.0f, -10.0f));
		camera.setTarget(new Vector3f(-10.0f, 0.0f, 0.0f));
		
		renderer = new MeshRenderer(scene, camera);
	}
	
}
