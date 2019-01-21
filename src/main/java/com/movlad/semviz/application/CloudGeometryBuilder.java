/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.application;

import com.jogamp.opengl.GL4;
import com.movlad.semviz.core.graphics.BufferLayout;
import com.movlad.semviz.core.graphics.engine.Geometry;
import com.movlad.semviz.core.math.geometry.PointCloud;

public abstract class CloudGeometryBuilder {

    protected PointCloud source;
    protected float[] data;
    protected BufferLayout layout;
    protected Geometry geometry;

    public CloudGeometryBuilder(PointCloud source) {
        this.source = source;
    }

    public abstract void buildDataBuffer();

    public final void buildDataBufferLayout() {
        layout = new BufferLayout();

        layout.push("position", GL4.GL_FLOAT, 3, true);
        layout.push("color", GL4.GL_UNSIGNED_BYTE, 3, false);
    }

    public abstract void buildGeometry();

    public final Geometry getGeometry() {
        return geometry;
    }

}
