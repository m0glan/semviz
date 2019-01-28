package com.movlad.semviz.core.graphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Collection of scene objects to be rendered.
 */
public final class Scene implements Iterable<SceneObject> {

    private final List<SceneObject> children;

    public Scene() {
        children = new ArrayList<>();
    }

    public void add(SceneObject object) {
        children.add(object);
    }

    public void add(int i, SceneObject object) {
        children.add(i, object);
    }

    public void remove(SceneObject object) {
        children.remove(object);
    }

    public void remove(int i) {
        children.remove(i);
    }

    public void replace(int i, SceneObject object) {
        remove(i);
        add(i, object);
    }

    public SceneObject getById(String id) {
        ListIterator<SceneObject> it = children.listIterator();

        while (it.hasNext()) {
            SceneObject o = it.next();

            if (o.getId().equals(id)) {
                return o;
            }
        }

        return null;
    }

    public SceneObject getByName(String name) {
        ListIterator<SceneObject> it = children.listIterator();

        while (it.hasNext()) {
            SceneObject o = it.next();

            if (o.getName().equals(name)) {
                return o;
            }
        }

        return null;
    }

    public SceneObject get(int i) {
        return children.get(i);
    }

    public int size() {
        return children.size();
    }

    public void clear() {
        children.clear();
    }

    @Override
    public Iterator<SceneObject> iterator() {
        return children.iterator();
    }

}
