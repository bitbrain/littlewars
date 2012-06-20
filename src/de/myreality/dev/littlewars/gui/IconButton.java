/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * General game text as a GUIObject
 * 
 * @version 	0.3.10
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class IconButton extends Button {
	
	// Icon in the middle
	private Image icon;
	
	// Color of the button
	Color clrIcon;

	public IconButton(int x, int y, int size, Image icon,
			GameContainer gc) throws SlickException {
		super(x, y, size, size, "", gc);
		this.icon = icon;
		clrIcon = new Color(255, 255, 255, 255);		
	}

	@Override
	public void draw(Graphics g) {
		if (isVisible()) {
			super.draw(g);
			icon.draw(getX() + getPadding(), getY() + getPadding(), getWidth() - getPadding() * 2, getHeight() - getPadding() * 2, clrIcon);
		}
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		if (!isEnabled()) {
			clrIcon.a = 0.2f;
		} else {
			clrIcon.a = 1.0f;
		}
	}
	
	

}
