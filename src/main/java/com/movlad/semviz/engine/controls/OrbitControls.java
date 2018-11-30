package com.movlad.semviz.engine.controls;

import org.joml.Vector3f;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.movlad.semviz.engine.Camera;

/**
 * Controls allowing the camera to orbit around its target.
 */
public class OrbitControls implements MouseListener {
	
	Camera camera;
	float oldX, oldY;
	
	public OrbitControls(Camera camera) {
		this.camera = camera;
	}

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) {
		oldX = 0;
		oldY = 0;
	}

	@Override
	public void mouseMoved(MouseEvent e) { }

	@Override
	public void mouseDragged(MouseEvent e) {
		if (oldX < 0.0005 && oldY < 0.0005) {
			// mouse has not yet been clicked or has been released
			
			oldX = e.getX();
			oldY = e.getY();
		} else {
			float differenceX = e.getX() - oldX;
			float differenceY = e.getY() - oldY;
			
			oldX = e.getX();
			oldY = e.getY();
			
			float speedX = differenceX / 2;
			float speedY = differenceY / 2;
			
			Vector3f velocityX = new Vector3f();
			Vector3f velocityY = new Vector3f();
			
			Vector3f oldTarget = camera.getTarget();
			Vector3f cameraRight = new Vector3f();
			
			/*
			 * the cross product between the camera direction and the world up
			 * vector gives the camera right vector
			 */
			
			camera.getDirection().cross(camera.getWorldUp(), cameraRight);
			
			// translating the camera along its right vector, then restoring its original target
			
			cameraRight.mul(-speedX, velocityX);
			camera.translate(velocityX);
			camera.setTarget(oldTarget);
			
			// translating the camera along its up vector, then restoring its original target
			
			camera.getUp().mul(-speedY, velocityY);
			camera.translate(velocityY);
			camera.setTarget(oldTarget);
		}
	}

	@Override
	public void mouseWheelMoved(MouseEvent e) {
		float zoom = e.getRotation()[1] / 12;
		
		camera.zoom(zoom);
	}

}
