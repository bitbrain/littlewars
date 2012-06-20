/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * General game text as a GUIObject
 * 
 * @version 	0.0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import de.myreality.dev.littlewars.objects.Camera;
import de.myreality.dev.littlewars.objects.GUIObject;

public class GameText extends GUIObject {

	// Text to display on the screen
	private String text;
	
	// Font of the text
	private Font font;
	
	// text color
	private Color color;
	
	// Camera
	private Camera camera;
	
	
	
	/**
	 * Constructor of GameText
	 * 
	 * @param x x-coordinate on the screen
	 * @param y y-coordinate on the screen
	 * @param text text content
	 * @param font font
	 * @param gc GameContainer
	 */
	public GameText(int x, int y, String text, Font font, GameContainer gc) {
		super(x, y, gc);
		this.text = text;
		this.font = font;
		this.color = new Color(255, 255, 255, 255);
		area = new Rectangle(getX(), getY(), getWidth(), getHeight());
	}
	
	public GameText(int x, int y, String text, Font font, GameContainer gc, Camera camera) {
		this(x, y, text, font, gc);
		this.camera = camera;
	}

	@Override
	public int getWidth() {
		if (font != null) {
			if (font.getWidth(text) > 0) {
				return font.getWidth(text);
			} else {
				return font.getWidth("X");
			}
		} else {
			return 0;
		}
	}

	@Override
	public int getHeight() {
		if (font != null) {
			if (font.getHeight(text) > 0) {
				return font.getHeight(text);
			} else {
				return font.getHeight("X");
			}
		} else {
			return 0;
		}
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}

	@Override
	public void draw(Graphics g) {
		if (isVisible()) {
			if (color != null) {
				if (!color.equals(g.getColor())) {
					g.setColor(color);
				}
			}
	
			if (camera == null) {
				this.font.drawString(getX(), getY(), text, color);
			} else {
				this.font.drawString(getX() - camera.getX(), getY() - camera.getY(), text, color);
			}
		}
	}
	
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
		area = new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public void setOpacity(float opacity) {
		color.a = opacity;
	}
	
	
	
	

	
	
}
