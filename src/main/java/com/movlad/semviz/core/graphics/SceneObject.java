package com.movlad.semviz.core.graphics;

/**
 * Three-dimensional object that has a geometry.
 */
public class SceneObject extends Object3d {

    private String name;
    private Geometry geometry;
    private boolean isVisible;

    public SceneObject() {
        this.name = "Object";
        this.isVisible = true;
    }

    public SceneObject(String name) {
        this.name = name;
        this.isVisible = true;
    }

    public SceneObject(Geometry geometry) {
        this.name = "Object";
        this.geometry = geometry;
        this.isVisible = true;
    }

    public SceneObject(String name, Geometry geometry) {
        this.name = name;
        this.geometry = geometry;
        this.isVisible = true;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final Geometry getGeometry() {
        return geometry;
    }

    public final void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public final boolean isVisible() {
        return isVisible;
    }

    public final void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

}
