/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Interface that makes elements movable
 * 
 * @version 	0.1.3
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.objects;

public interface Movable {
	
	// Directions
	static int TOP = 3, BOTTOM = 2, LEFT = 0, RIGHT = 1;
	
	void move(int direction, int delta);
}
