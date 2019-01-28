package com.movlad.semviz.core.graphics;

import com.jogamp.opengl.util.GLBuffers;

/**
 * Class representing one attribute of a buffer layout, such as position, color
 * or texture coordinates.
 */
class BufferAttribute {

    private final String name;
    private final int size;
    private final int type;
    private final boolean normalized;

    /**
     * Constructor.
     *
     * @param name is the name of the attribute as present in the shader
     * @param size is the number of components of the attribute
     * @param type is the GL type
     * @param normalized is true if the value is between 0 and 1
     */
    public BufferAttribute(String name, int size, int type, boolean normalized) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.normalized = normalized;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public int getType() {
        return type;
    }

    public boolean getNormalized() {
        return normalized;
    }

    /**
     * @return the size of the attribute in bytes
     */
    public int sizeInBytes() {
        return size * GLBuffers.sizeOfGLType(type);
    }

}
