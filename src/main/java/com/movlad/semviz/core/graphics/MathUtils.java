package com.movlad.semviz.core.graphics;

import org.joml.Vector3f;

public class MathUtils {

	/**
	 * Converts a position in the Cartesian coordinate system to a position in the Polar coordinate system.
	 * 
	 * @param cartesianCoords is the position to be converted
	 * @return the polar coordinates
	 */
	public static Vector3f toPolarCoords(Vector3f cartesianCoords) {
		float r = cartesianCoords.length();
		float theta = (float) Math.acos(cartesianCoords.z / r);
		float phi = (float) Math.atan2(cartesianCoords.y, cartesianCoords.x);
		
		return new Vector3f(r, theta, phi);
	}
	
	/**
	 * Converts a position in the Polar coordinate system to a position in the Polar coordinate system.
	 * 
	 * @param polarCoords is the position to be converted
	 * @return the Cartesian coordinates
	 */
	public static Vector3f toCartesianCoords(Vector3f polarCoords) {
		float x = (float) (polarCoords.x * Math.sin(polarCoords.y) * Math.cos(polarCoords.z));
		float y = (float) (polarCoords.x * Math.sin(polarCoords.y) * Math.sin(polarCoords.z));
		float z = (float) (polarCoords.x * Math.cos(polarCoords.y));
		
		return new Vector3f(x, y, z);
	}
	
	/**
	 * @param ang is the angle to be normalized in radians
	 * @return an angle between 0 and {@code 2 * PI}
	 */
	public static float normalizeAngle(float ang) {
		float nang = ang;
		
		if (ang > (2 * Math.PI)) {
			ang -= 2 * Math.PI;
		} else if (ang < 0) {
			ang += 2 * Math.PI;
		}
		
		return nang;
	}
	
}
