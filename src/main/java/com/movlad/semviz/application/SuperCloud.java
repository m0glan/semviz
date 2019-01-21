/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.application;

import com.github.quickhull3d.Point3d;
import com.github.quickhull3d.Vector3d;
import com.movlad.semviz.core.math.geometry.PointCloud;
import com.movlad.semviz.core.semantic.QueryManager;
import com.movlad.semviz.core.semantic.QueryResult;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.joml.Vector3f;

public class SuperCloud implements Iterable<PointCloud> {

    private final List<PointCloud> clusters;
    private final QueryManager queryManager;
    private final List<QueryResult> queryResults;

    public SuperCloud(QueryManager queryManager, List<QueryResult> queryResults) {
        this.clusters = new ArrayList<>();
        this.queryManager = queryManager;
        this.queryResults = queryResults;
    }

    public PointCloud get(int i) {
        return clusters.get(i);
    }

    public void load() {
        queryResults.forEach(result -> {
            clusters.add(queryManager.retrieve(result.getIndividual()));
        });

        center();
        rotate();
    }

    private void center() {
        Point3d centroid = new Point3d(0, 0, 0);
        int size = 0;

        for (PointCloud cluster : clusters) {
            size += cluster.size();

            cluster.forEach(point -> {
                centroid.add(new Vector3d(point.x, point.y, point.z));
            });
        }

        centroid.set(centroid.x / size, centroid.y / size, centroid.z / size);

        clusters.forEach(cluster -> {
            cluster.forEach(point -> {
                point.sub(centroid);
            });
        });
    }

    private void rotate() {
        for (PointCloud cluster : clusters) {
            cluster.forEach(point -> {
                Vector3f v = new Vector3f((float) point.x, (float) point.y, (float) point.z);

                v.rotateAxis((float) (Math.PI / 2), 0.0f, 1.0f, 0.0f);

                point.set(v.x, v.y, v.z);
            });
        }
    }

    @Override
    public Iterator<PointCloud> iterator() {
        return clusters.iterator();
    }

}
