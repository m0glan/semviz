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

    public void add(Point point) {
        points.add(point);
    }

    public void remove(Point point) {
        points.remove(point);
    }

    public void remove(int i) {
        points.remove(i);
    }

    public Point get(int i) {
        return points.get(i);
    }

    public int size() {
        return points.size();
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
