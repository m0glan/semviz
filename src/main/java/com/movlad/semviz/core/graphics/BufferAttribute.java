package com.movlad.semviz.core.graphics;

/**
 * Class representing one attribute of a buffer layout, such as position, color,
 * texture coordinates etc. .
 */
class BufferAttribute {

    private final String name;
    private final int size;
    private final boolean normalized;

    /**
     * Constructor.
     *
     * @param name is the name of the attribute as present in the shader
     * @param size is the number of components of the attribute
     * @param normalized is true if the value is between 0 and 1
     */
    public BufferAttribute(String name, int size, boolean normalized) {
        this.name = name;
        this.size = size;
        this.normalized = normalized;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public boolean getNormalized() {
        return normalized;
    }

    /**
     * @return the total size of the layout element in bytes
     * ({@code count * sizeof(type)})
     */
    public int sizeInBytes() {
        return size * 32;
    }

}
