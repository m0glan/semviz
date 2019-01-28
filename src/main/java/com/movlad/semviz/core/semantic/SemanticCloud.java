package com.movlad.semviz.core.semantic;

import com.github.quickhull3d.Point3d;
import com.github.quickhull3d.Vector3d;
import com.movlad.semviz.core.math.geometry.PointCloud;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.joml.Vector3f;

/**
 * When executing a SPARQL query via
 * {@link com.movlad.semviz.core.semantic.QueryManager}, multiple cloud
 * individuals are retrieved. These clusters that originate from the same base
 * cloud are not centered by default, and in order to center them, the centroid
 * of the base cloud needs to be subtracted from each of their points. This
 * class groups all of the retrieved clusters, calculates the centroid of their
 * base cloud and then centers them.
 */
public class SemanticCloud implements Iterable<PointCloud> {

    private final List<PointCloud> clusters;
    private final QueryManager queryManager;
    private final List<QueryResult> queryResults;

    /**
     * Constructor.
     *
     * @param queryManager holds the data of the currently loaded Semviz
     * directory
     * @param queryResults is the list of results obtained after the execution
     * of a SPARQL query on the query manager
     */
    public SemanticCloud(QueryManager queryManager, List<QueryResult> queryResults) {
        this.clusters = new ArrayList<>();
        this.queryManager = queryManager;
        this.queryResults = queryResults;
    }

    public PointCloud get(int i) {
        return clusters.get(i);
    }

    /**
     * Retrieves all the clusters described by the query results, centers them
     * and rotates them.
     */
    public void load() {
        queryResults.forEach(result -> {
            clusters.add(queryManager.retrieve(result.getIndividual()));
        });

        center();
        rotate();
    }

    /**
     * Subtracts the centroid of the base cloud from each point of each cluster.
     */
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

    /**
     * Rotates each point of each cloud 90 degrees around the X-Axis so that
     * they are well aligned with the world up vector.
     */
    private void rotate() {
        for (PointCloud cluster : clusters) {
            cluster.forEach(point -> {
                Vector3f v = new Vector3f((float) point.x, (float) point.y, (float) point.z);

                v.rotateAxis((float) (Math.PI / 2), 0.0f, 1.0f, 0.0f);

                point.set(v.x, v.y, v.z);
            });
        }
    }

    public int size() {
        return clusters.size();
    }

    @Override
    public Iterator<PointCloud> iterator() {
        return clusters.iterator();
    }

}
