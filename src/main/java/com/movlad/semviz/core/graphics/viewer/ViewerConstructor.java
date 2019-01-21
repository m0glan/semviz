/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.core.graphics.viewer;

public class ViewerConstructor {

    ViewerBuilder builder;

    public ViewerConstructor(ViewerBuilder builder) {
        this.builder = builder;
    }

    public Viewer getViewer() {
        return builder.getViewer();
    }

    public void construct() {
        builder.buildCamera();
        builder.buildScene();
        builder.buildRenderer();
        builder.buildControls();
        builder.buildAnimator();
    }

}
