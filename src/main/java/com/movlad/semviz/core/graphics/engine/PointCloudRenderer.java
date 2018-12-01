package com.movlad.semviz.core.graphics.engine;

import com.jogamp.opengl.GL4;

public class PointCloudRenderer extends Renderer {

	public PointCloudRenderer(Scene scene, Camera camera) {
		super(scene, camera);
	}

	@Override
	protected void draw(BufferedGeometry drawable) {
		gl.glDrawArrays(GL4.GL_POINTS, 0, drawable.getData().length);
	}

}
