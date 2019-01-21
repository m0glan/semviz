package com.movlad.semviz.core.graphics.engine;

import com.movlad.semviz.core.math.geometry.TransformationUtils;
import java.awt.event.MouseWheelEvent;
import org.joml.Vector3f;

/**
 * Controls making the camera move around the world's origin.
 */
public class OrbitControls extends Controls {

    public OrbitControls(Camera camera) {
        super(camera);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        float zoom = camera.getZoom() - e.getWheelRotation() * zoomSpeed;

        camera.zoom(zoom);
        camera.updateProjectionMatrix();
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        oldX = 0;
        oldY = 0;
    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
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
            sphericalCoords.add(new Vector3f(0, -speedY, -speedX));

            // making sure the angles are not outside the [0, 2 * PI] interval
            sphericalCoords.y = TransformationUtils.wrapTo2Pi(sphericalCoords.y);
            sphericalCoords.z = TransformationUtils.wrapTo2Pi(sphericalCoords.z);

            // updating the position of the camera
            camera.setPosition(TransformationUtils.toCartesianCoords(sphericalCoords));
        }
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        return;
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        return;
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        return;
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        return;
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        return;
    }

}
