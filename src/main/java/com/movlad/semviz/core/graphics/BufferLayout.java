package com.movlad.semviz.core.graphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Keeps structural and size-related information concerning the layout of a
 * {@code VertexBufferObject}, such as position, color, texture coordinates etc.
 * .
 */
public class BufferLayout implements Iterable<BufferAttribute> {

    private final List<BufferAttribute> attributes;
    private int stride = 0;

    public BufferLayout() {
        this.attributes = new ArrayList<>();
    }

    /**
     * @return sum of the sizes of all the layout elements
     */
    public int getStride() {
        return stride;
    }

    public BufferAttribute get(int i) {
        return attributes.get(i);
    }

    /**
     * Adds an attribute to the layout and increases the stride.
     *
     * @param attribute is the attribute to be added
     */
    public void add(BufferAttribute attribute) {
        attributes.add(attribute);
        stride += attribute.sizeInBytes();
    }

    /**
     * @return the sum of all attribute sizes
     */
    public int rowLength() {
        int length = 0;

        for (BufferAttribute attribute : this) {
            length += attribute.getSize();
        }

        return length;
    }

    public int size() {
        return attributes.size();
    }

    @Override
    public Iterator<BufferAttribute> iterator() {
        return attributes.iterator();
    }

}
