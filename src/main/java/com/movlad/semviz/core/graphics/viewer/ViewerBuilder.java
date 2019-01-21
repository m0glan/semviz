/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.core.graphics.viewer;

public interface ViewerBuilder {

    void buildCamera();

    void buildScene();

    void buildRenderer();

    void buildControls();

    void buildAnimator();

    Viewer getViewer();

}
