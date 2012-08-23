/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://littlewars.my-reality.de
 * 
 * Helper for displaying a cursor
 * 
 * @version 	0.7.1
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.components.helpers;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

public class CursorHelper {

	private static CursorHelper _instance;
	private Image currentCursor;
	
	static {
		_instance = new CursorHelper();
	}
	
	public static CursorHelper getInstance() {
		return _instance;
	}
	
	public CursorHelper() {
		currentCursor = null;
	}
	
	public void draw(GameContainer gc, Graphics g) {
		if (currentCursor != null) {
			Input input = gc.getInput();
			currentCursor.draw(input.getMouseX(), input.getMouseY());
		}
	}
	
	public void setCursor(Image image) {
		if (currentCursor != null) {
			if (!currentCursor.equals(image)) {
				currentCursor = image;
			}
		} else {
			currentCursor = image;
		}
	}
}
