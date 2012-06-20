/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Geometric 2D Vector class
 * 
 * @version 	0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.components;

public class Vector2d {
	
	// x-coordinate, y-coordinate
	public float x, y;
	
	
	/**
	 * Constructor of Vector2d
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public Vector2d(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	
	
	/**
	 * @return Value of the vector
	 */
	public float getLength() {
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	
	/**
	 * @return unit vector with length=1
	 */
	public Vector2d getUnitVector() {
		Vector2d tmpvec = new Vector2d(x, y);
		
		if (getLength() > 0) {
			tmpvec.x = x / getLength();
			tmpvec.y = y / getLength();
		}
		
		return tmpvec;
	}
	
	
	
	/**
	 * @param vector other vector for calculation
	 * @return scalar product of the 2 vectors
	 */
	public float getScalar(Vector2d vector) {
		return x * vector.x + y * vector.y;
	}
}
