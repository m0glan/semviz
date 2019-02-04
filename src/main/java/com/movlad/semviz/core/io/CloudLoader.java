package com.movlad.semviz.core.io;

import com.movlad.semviz.core.math.geometry.Point;
import com.movlad.semviz.core.math.geometry.PointCloud;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Generates a float buffer from a point cloud text file.
 */
public class CloudLoader {

    private final int POINT_ATTR_COUNT = 9;
    private String path;
    private int failCount;
    private PointCloud pointCloud;

    /**
     * @param path is the path of the cloud {@code .txt} file
     */
    public CloudLoader(String path) {
        this.path = path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getFailCount() {
        return failCount;
    }

    /**
     * @return the point cloud obtained as a result of running the {@code load()
     * } operation
     */
    public PointCloud getCloud() {
        return pointCloud;
    }

    /**
     * Loads the contents of the file.
     *
     * @throws IOException if the cloud file is not found
     */
    public void load() throws IOException {
        failCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            pointCloud = new PointCloud();

            for (String line; (line = reader.readLine()) != null;) {
                String[] attributes = line.split("\\t");

                if (attributes.length == POINT_ATTR_COUNT) {
                    try {
                        Point point = new Point();

                        point.x = Float.parseFloat(attributes[0]);
                        point.y = Float.parseFloat(attributes[1]);
                        point.z = Float.parseFloat(attributes[2]);

                        point.r = Short.parseShort(attributes[3]);
                        point.g = Short.parseShort(attributes[4]);
                        point.b = Short.parseShort(attributes[5]);

                        point.normalX = Float.parseFloat(attributes[6]);
                        point.normalY = Float.parseFloat(attributes[7]);
                        point.normalZ = Float.parseFloat(attributes[8]);

                        pointCloud.add(point);
                    } catch (NumberFormatException e) {
                        failCount++;
                    }
                } else {
                    failCount++;
                }
            }
        }
    }

}
