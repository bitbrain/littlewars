/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Class that contains information of display resolution 
 * 
 * @version 	0.1.5
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.components.config;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Resolution implements Serializable {

	// ID for serialization
	private static final long serialVersionUID = 1L;
	
	// x-coordinate, y-coordinate
	private int x, y;
	
	
	/**
	 * Constructor of Resolution
	 * 
	 * @param x x-coordinate on the screen
	 * @param y y-coordinate on the screen
	 */
	public Resolution(int x, int y) {
		this.x = x;
		this.y = y;
	}

	
	
	/**
	 * @return x-position on the screen
	 */
	public int getX() {
		return x;
	}

	
	
	/**
	 * Setter of the x-coordinate
	 * 
	 * @param x new x-coordinate on the screen
	 */
	public void setX(int x) {
		this.x = x;
	}

	
	
	/**
	 * @return y-position on the screen
	 */
	public int getY() {
		return y;
	}

	
	
	/**
	 * Setter of the y-coordinate
	 * 
	 * @param y new y-coordinate on the screen
	 */
	public void setY(int y) {
		this.y = y;
	}
	

	
	@Override
	public String toString() {
		return x + "x" + y;
	}

	
	
	@Override
	public boolean equals(Object obj) {
		return x == ((Resolution) obj).x && y == ((Resolution) obj).y;
	}
	
	
	
	/**
	 * Function that returns all available system display modes
	 * 
	 * @return List containing all available resolutions
	 */
	public static List<Resolution> getAvailableResolutions() {
		List<Resolution> modes = new ArrayList<Resolution>();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice display = ge.getDefaultScreenDevice();
		DisplayMode[] availableModes = display.getDisplayModes();
		
		for (DisplayMode mode : availableModes) {
			boolean alreadyInserted = false;			
			for (Resolution res : modes) {
				if (res.getX() == mode.getWidth() && res.getY() == mode.getHeight()) {
					alreadyInserted = true;
					break;
				}
			}
			
			if (!alreadyInserted) {
				modes.add(new Resolution(mode.getWidth(), mode.getHeight()));
			}
		}
		
		return modes;
	}
}
