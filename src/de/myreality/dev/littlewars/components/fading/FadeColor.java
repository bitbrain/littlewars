/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Fadable Color
 * 
 * @version 	0.4
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.components.fading;

import java.nio.FloatBuffer;

import org.newdawn.slick.Color;



public class FadeColor extends Color {	
	
	// Serial ID
	private static final long serialVersionUID = 1L;
	
	// Target color
	Color target;
	
	// time for process
	long miliseconds;

	public FadeColor(float r, float g, float b, float a) {
		super(r, g, b, a);
		init();
	}

	public FadeColor(Color color) {
		super(color);
		init();
	}

	public FadeColor(float r, float g, float b) {
		super(r, g, b);
		init();
	}

	public FadeColor(FloatBuffer buffer) {
		super(buffer);
		init();
	}

	public FadeColor(int r, int g, int b, int a) {
		super(r, g, b, a);
		init();
	}

	public FadeColor(int r, int g, int b) {
		super(r, g, b);
		init();
	}

	public FadeColor(int value) {
		super(value);
		init();
	}
	
	private void init() {
		target = null;
	}
	
	public void update(int delta) {
		float value = 0.001f;
		// TODO: Implement "real" fade calculation
		if (target != null) {
			// R
			if (r < target.r) {
				r += value;
			} else if (r > target.r) {
				r -= value;
			}
			// G
			if (g < target.g) {
				g += value;
			} else if (g > target.g) {
				g -= value;
			}
			
			// B
			if (b < target.b) {
				b += value;
			} else if (r > target.b) {
				b -= value;
			}
			
			// A			
			if (a < target.a && r != b && b != g && g != r) {
				a += value;
			} else if (a > target.a) {
				a -= value;
			}
		}
	}
	
	
	/**
	 * Set the new fade effect
	 * 
	 * @param target new fade target
	 * @param miliseconds miliseconds to process
	 */
	public void fadeTo(Color target, long miliseconds) {
		this.target = target;
		this.miliseconds = miliseconds;
	}
	
	

}
