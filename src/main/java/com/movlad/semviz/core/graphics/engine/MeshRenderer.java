package com.movlad.semviz.core.graphics.engine;

import com.jogamp.opengl.GL4;

/**
 * Renderer used for rendering basic geometries (triangles).
 */
public class MeshRenderer extends Renderer {

	public MeshRenderer(Scene scene, Camera camera) {
		super(scene, camera);
	}

	@Override
	protected final void draw(BufferedGeometry drawable) {
		gl.glDrawArrays(GL4.GL_TRIANGLES, 0, drawable.getData().length);
	}

}
