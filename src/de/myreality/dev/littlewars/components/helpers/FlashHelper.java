/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://littlewars.my-reality.de
 * 
 * Helper for displaying flash messages
 * 
 * @version 	0.4.9
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.components.helpers;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.FlashBox;
import de.myreality.dev.littlewars.gui.GUIObject;
import de.myreality.dev.littlewars.gui.GameText;

public class FlashHelper {

	private static FlashHelper _instance;
	private FlashBox box;
	
	static {
		_instance = new FlashHelper();
	}
	
	private FlashHelper() {
		box = null;
	}
	
	
	public void render(Graphics g) {
		if (box != null) {
			box.draw(g);
		}
	}
	
	public void update(int delta) {
		if (box != null) {
			box.update(delta);
		}
	}
	
	
	public void flash(GUIObject content, int duration, GameContainer gc) {
		box = new FlashBox(content, duration, gc);
	}
	
	public void flash(String text, int duration, GameContainer gc) {
		flash(new GameText(0, 0, text, ResourceManager.getInstance().getFont("FONT_SMALL"), gc), duration, gc);
	}
	
	public static FlashHelper getInstance() {
		return _instance;
	}
}
