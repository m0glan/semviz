package com.movlad.semviz.core.graphics;

import org.joml.Vector3f;

public class MathUtils {

	/**
	 * Converts a position in the Cartesian coordinate system to a position in the spherical coordinate system.
	 * 
	 * @param cartesianCoords is the position to be converted
	 * @return the spherical coordinates
	 */
	public static Vector3f toSphericalCoords(Vector3f cartesianCoords) {
		float r = cartesianCoords.length();
		float theta = (float) Math.acos(cartesianCoords.z / r);
		float phi = (float) Math.atan2(cartesianCoords.y, cartesianCoords.x);
		
		return new Vector3f(r, theta, phi);
	}
	
	/**
	 * Converts a position in the spherical coordinate system to a position in the Cartesian coordinate system.
	 * 
	 * @param sphericalCoords is the position to be converted
	 * @return the Cartesian coordinates
	 */
	public static Vector3f toCartesianCoords(Vector3f sphericalCoords) {
		float x = (float) (sphericalCoords.x * Math.sin(sphericalCoords.y) * Math.cos(sphericalCoords.z));
		float y = (float) (sphericalCoords.x * Math.sin(sphericalCoords.y) * Math.sin(sphericalCoords.z));
		float z = (float) (sphericalCoords.x * Math.cos(sphericalCoords.y));
		
		return new Vector3f(x, y, z);
	}
	
	/**
	 * @param ang is the angle to be normalized in radians
	 * @return an angle between 0 and {@code 2 * PI}
	 */
	public static float wrapTo2Pi(float ang) {
		float wrap = ang;
		
		if (ang > (2 * Math.PI)) {
			ang -= 2 * Math.PI;
		} else if (ang < 0) {
			ang += 2 * Math.PI;
		}
		
		return wrap;
	}
	
	public static float map(float x, float sourceMin, float sourceMax, float destMin, float destMax) {
		return destMin + ((destMax - destMin)/(sourceMax - sourceMin)) * (x - sourceMin);
	}
	
}
