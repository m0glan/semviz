/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.application;

import com.movlad.semviz.core.graphics.engine.Geometry;

/**
 *
 * @author Vlad
 */
public class CloudGeometryConstructor {

    private CloudGeometryBuilder builder;

    public CloudGeometryConstructor(CloudGeometryBuilder builder) {
        this.builder = builder;
    }

    public void construct() {
        builder.buildDataBufferLayout();
        builder.buildDataBuffer();
        builder.buildGeometry();
    }

    public Geometry getGeometry() {
        return builder.getGeometry();
    }

}
