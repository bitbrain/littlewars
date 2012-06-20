/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Fading interface
 * 
 * @version 	0.1
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.components.fading;


public interface Fadable {
	
	// Fade states
	public static final int FADE_IN = 0, VISIBLE = 1, FADE_OUT = 2; 
	
	public void fadeIn(int delta);	
	public void fadeOut(int delta);
}
