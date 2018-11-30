package com.movlad.semviz.core.graphics.engine.controls;

import org.joml.Vector3f;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.movlad.semviz.core.graphics.MathUtils;
import com.movlad.semviz.core.graphics.engine.Camera;

public class OrbitControls implements MouseListener {
	
	private Camera camera;
	private float oldX, oldY;
	
	public OrbitControls(Camera camera) {
		this.camera = camera;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		oldX = 0;
		oldY = 0;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (oldX < 0.0005 && oldY < 0.0005) {
			oldX = e.getX();
			oldY = e.getY();
		} else {
			float differenceX = e.getX() - oldX;
			float differenceY = e.getY() - oldY;
			
			oldX = e.getX();
			oldY = e.getY();

			Vector3f polarCoords = MathUtils.toPolarCoords(camera.getPosition());
			
			float speedY = (float)Math.toRadians(differenceY / 4.0f);
			float speedX = (float)Math.toRadians(differenceX / 4.0f);
			
			polarCoords.add(new Vector3f(0, speedY, speedX));
			polarCoords.y = MathUtils.normalizeAngle(polarCoords.y);
			polarCoords.z = MathUtils.normalizeAngle(polarCoords.z);
			
			camera.setPosition(MathUtils.toCartesianCoords(polarCoords));
		}
	}

	@Override
	public void mouseWheelMoved(MouseEvent e) {
		float zoom = e.getRotation()[1] / 12;
		
		camera.zoom(zoom);
	}

}
