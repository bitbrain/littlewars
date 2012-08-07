/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * GUI button element
 * 
 * @version 	0.1.3
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import de.myreality.dev.littlewars.components.resources.ResourceManager;

public class Button extends GUIObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// background image
	protected Image background;
	
	// Sound configuration
	protected Sound hoverSound, clickSound;
	
	// text content
	protected String text;
	
	// Target font
	protected Font font;
	
	protected boolean soundOn;
	
	// Colors
	protected Color clrFocus, hoverColor, defaultColor;	
	
	
	/**
	 * Constructor of Button
	 * 
	 * @param x x-coordinate on the screen
	 * @param y y-coordinate on the screen
	 * @param width button width
	 * @param height button height
	 * @param text text content
	 * @param gc GameContainer
	 * @throws SlickException
	 */
	public Button(int x, int y, int width, int height, String text, GameContainer gc) throws SlickException {
		super(x, y, gc);
		background = ResourceManager.getInstance().getImage("GUI_BUTTON_NORMAL");
		this.width = width;
		this.height = height;		
		this.gc = gc;
		this.text = text;	
		this.clrFocus = null;
		
		area = new Rectangle(getX(), getY(), getWidth(), getHeight());
		
		// Sounds 
		hoverSound = ResourceManager.getInstance().getSound("SOUND_HOVER");
		clickSound = ResourceManager.getInstance().getSound("SOUND_ONCLICK");
		soundOn = true;
		font = ResourceManager.getInstance().getFont("FONT_MENU");
		hoverColor = ResourceManager.getInstance().getColor("COLOR_MAIN");
		defaultColor = Color.white;
	}
	
	public void enableSound(boolean value) {
		soundOn = value;
	}
	
	public Color getHoverColor() {
		return hoverColor;
	}

	public void setHoverColor(Color hoverColor) {
		this.hoverColor = hoverColor;
	}
	
	public Color getDefaultColor() {
		return defaultColor;
	}

	public void setDefaultColor(Color defaultColor) {
		this.defaultColor = defaultColor;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	public Color getClrFocus() {
		return clrFocus;
	}

	public void setClrFocus(Color clrFocus) {
		this.clrFocus = clrFocus;
	}
	
	public void setFont(Font newFont) {
		font = newFont;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void draw(Graphics g) {	
		if (isVisible()) {
			if (background != null) {
				if (isMouseOver() && clrFocus == null) {				
					background.draw(getX(), getY(), width, height, hoverColor);
				} else if (isEnabled()) {
					if (clrFocus != null) {
						background.draw(getX(), getY(), width, height, clrFocus);
					} else {
						background.draw(getX(), getY(), width, height, defaultColor);
					}
				} else {
					background.draw(getX(), getY(), width, height, Color.gray);
				}
			}
			
			if (font != null) {
				font.drawString(getX() + (getWidth() / 2 - font.getWidth(text) / 2), 
						getY() + (getHeight() / 2 - font.getHeight(text) / 2), text);	
			}
		}
	}
	
	public String getText() {
		return text;
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		
		if (soundOn) {
			if (onMouseOver()) {
				hoverSound.play(1.0f, 0.5f);
			}
			
			if (onMouseClick()) {
				clickSound.play(1.0f, 0.5f);
			}
		}
	}
}
