package com.movlad.semviz.core.graphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Collection of scene objects to be rendered.
 */
public class Scene implements Iterable<SceneObject> {

    private final List<SceneObject> children;

    public Scene() {
        children = new ArrayList<>();
    }

    public final void add(SceneObject object) {
        children.add(object);
    }

    public final void add(int i, SceneObject object) {
        children.add(i, object);
    }

    public final void remove(SceneObject object) {
        children.remove(object);
    }

    public final void remove(int i) {
        children.remove(i);
    }

    public final SceneObject getById(String id) {
        ListIterator<SceneObject> it = children.listIterator();

        while (it.hasNext()) {
            SceneObject o = it.next();

            if (o.getId().equals(id)) {
                return o;
            }
        }

        return null;
    }

    public final SceneObject getByName(String name) {
        ListIterator<SceneObject> it = children.listIterator();

        while (it.hasNext()) {
            SceneObject o = it.next();

            if (o.getName().equals(name)) {
                return o;
            }
        }

        return null;
    }

    public final SceneObject get(int i) {
        return children.get(i);
    }

    public final int size() {
        return children.size();
    }

    public final void clear() {
        children.clear();
    }

    @Override
    public final Iterator<SceneObject> iterator() {
        return children.iterator();
    }

}
