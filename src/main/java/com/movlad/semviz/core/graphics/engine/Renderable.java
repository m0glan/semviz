package com.movlad.semviz.core.graphics.engine;

import com.movlad.semviz.core.graphics.VertexArrayObject;

/**
 * Pair of an object and its corresponding vertex array object, to be rendered
 * on screen.
 */
class Renderable {

    private final Object3d object;
    private final VertexArrayObject vertexArrayObject;

    public Renderable(Object3d object, VertexArrayObject vertexArrayObject) {
        this.object = object;
        this.vertexArrayObject = vertexArrayObject;
    }

    Object3d getObject() {
        return object;
    }

    VertexArrayObject getVertexArrayObject() {
        return vertexArrayObject;
    }

}
