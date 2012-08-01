/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Provides a description for a GUIObject
 * 
 * @version 	0.1.3
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.components.resources.ResourceManager;

public class DescriptionField extends GUIObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Target GUIOBject
	GUIObject guiObject;
	
	// Description text content
	GameText description;
	
	// offset x
	int xOffset;
	
	// offset y
	int yOffset;

	
	
	/**
	 * Constructor of DescriptionField
	 * 
	 * @param x x-coordinate on the screen
	 * @param y y-coordinate on the screen
	 * @param xOffset offset (x)
	 * @param yOffset offset (y)
	 * @param description text description
	 * @param gc GameContainer
	 */
	public DescriptionField(int x, int y, int xOffset, int yOffset, String description, GameContainer gc) {
		super(x, y, gc);
		this.description = new GameText(x, y, description, ResourceManager.getInstance().getFont("FONT_MENU"), gc);
		this.description.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	
	
	/**
	 * Constructor of DescriptionField
	 * 
	 * @param x x-coordinate on the screen
	 * @param y y-coordinate on the screen
	 * @param description text description
	 * @param gc GameContainer
	 */
	public DescriptionField(int x, int y, String description, GameContainer gc) {
		this(x, y, 0, 0, description, gc);
	}
	
	
	
	/**
	 * Constructor of DescriptionField
	 * 
	 * @param x x-coordinate on the screen
	 * @param y y-coordinate on the screen
	 * @param xOffset offset (x)
	 * @param description text description
	 * @param gc GameContainer
	 */
	public DescriptionField(int x, int y, int xOffset, String description, GameContainer gc) {
		this(x, y, xOffset, 0, description, gc);
	}
	
	
	
	/**
	 * Append a new object to the field
	 * 
	 * @param object target object
	 */
	public void append(GUIObject object) {
		guiObject = object;
		guiObject.setX(getX() + xOffset);
		
		if (yOffset == 0) {
			guiObject.setY(getY() - guiObject.getHeight() / 4);	
		} else {
			guiObject.setY(getY() + yOffset);
		}
	}

	@Override
	public void draw(Graphics g) {
		if (isVisible()) {
			description.draw(g);
			
			if (guiObject != null) {
				guiObject.draw(g);
			}
		}
	}
	
	
	
	/**
	 * @return current appended object
	 */
	public GUIObject getAppended() {
		return guiObject;
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		
		if (guiObject != null) {
			guiObject.update(delta);
		}
		description.update(delta);
	}
	
	

}
