package com.movlad.semviz.core.math.geometry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Collection of points.
 */
public class PointCloud implements Iterable<Point> {

    private final List<Point> points;

    public PointCloud() {
        this.points = new ArrayList<>();
    }

    public PointCloud(PointCloud cloud) {
        this.points = new ArrayList<>();

        cloud.forEach(point -> {
            this.points.add(point);
        });
    }

    public void add(Point point) {
        points.add(point);
    }

    public void remove(Point point) {
        points.remove(point);
    }

    public void remove(int i) {
        points.remove(i);
    }

    public void remove(int... indices) {
        List<Point> toRemove = new ArrayList<>();

        for (int i = 0; i < indices.length; i++) {
            toRemove.add(get(indices[i]));
        }

        toRemove.forEach(point -> {
            remove(point);
        });
    }

    public Point get(int i) {
        return points.get(i);
    }

    public int size() {
        return points.size();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public Point[] getPoints() {
        Point[] arr = new Point[size()];

        return points.toArray(arr);
    }

    @Override
    public Iterator<Point> iterator() {
        return points.iterator();
    }

}
