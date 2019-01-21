/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.core.graphics.engine;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author Vlad
 */
public abstract class Controls implements MouseListener, MouseMotionListener, MouseWheelListener {

    protected final Camera camera;
    protected float oldX, oldY;
    protected float zoomSpeed;

    public Controls(Camera camera) {
        this.camera = camera;

        zoomSpeed = 1.0f;
    }

    public final void setZoomSpeed(float zoomSpeed) throws IllegalArgumentException {
        if (zoomSpeed < 0.0f) {
            throw new IllegalArgumentException("Zoom speed must be positive.");
        }

        this.zoomSpeed = zoomSpeed;
    }

}
