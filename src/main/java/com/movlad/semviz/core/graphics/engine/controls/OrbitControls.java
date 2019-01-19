package com.movlad.semviz.core.graphics.engine.controls;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.movlad.semviz.core.graphics.engine.Camera;
import com.movlad.semviz.core.math.geometry.TransformationUtils;
import org.joml.Vector3f;

/**
 * Controls making the camera move around the world's origin.
 */
public class OrbitControls implements MouseListener {

    private final Camera camera;
    private float oldX, oldY;
    private float zoomSpeed = 1.0f;

    public OrbitControls(Camera camera) {
        this.camera = camera;
    }

    public final void setZoomSpeed(float zoomSpeed) throws IllegalArgumentException {
        if (zoomSpeed < 0.0f) {
            throw new IllegalArgumentException("Zoom speed must be positive.");
        }

        this.zoomSpeed = zoomSpeed;
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
    public final void mouseDragged(MouseEvent e) {
        if (oldX < 0.0005 && oldY < 0.0005) {
            oldX = e.getX();
            oldY = e.getY();
        } else {
            float differenceX = e.getX() - oldX;
            float differenceY = e.getY() - oldY;

            oldX = e.getX();
            oldY = e.getY();

            // getting the current position of the camera in the polar coordinate system
            Vector3f sphericalCoords = TransformationUtils.toSphericalCoords(camera.getPosition());

            float speedY = (float) Math.toRadians(differenceY / 4.0f);
            float speedX = (float) Math.toRadians(differenceX / 4.0f);

            // adding speedY to the theta angle and speedX to the phi angle
            sphericalCoords.add(new Vector3f(0, speedY, speedX));

            // making sure the angles are not outside the [0, 2 * PI] interval
            sphericalCoords.y = TransformationUtils.wrapTo2Pi(sphericalCoords.y);
            sphericalCoords.z = TransformationUtils.wrapTo2Pi(sphericalCoords.z);

            // updating the position of the camera
            camera.setPosition(TransformationUtils.toCartesianCoords(sphericalCoords));
        }
    }

    @Override
    public final void mouseWheelMoved(MouseEvent e) {
        float zoom = camera.getZoom() + (e.getRotation()[1] * zoomSpeed);

        camera.zoom(zoom);
        camera.updateProjectionMatrix();
    }

}
