/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Provides a button with zoom effect
 * 
 * @version 	0.0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class ZoomButton extends Button {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Zoom and zoom range
	private double zoom, limit;
	
	// State of the zoom (zoom in or zoom out)
	private boolean zoomIn;
	
	// Strength of zooming
	private double factor;
	
	
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
	public ZoomButton(int x, int y, int width, int height, String text,
			GameContainer gc) throws SlickException {
		super(x, y, width, height, text, gc);
		this.zoom = 0;
		this.limit = 3;
		this.factor = 0.02;
	}
	
	public double getLimit() {
		return limit;
	}

	public void setLimit(double limit) {
		this.limit = limit;
		zoom = 0;
	}

	public double getFactor() {
		return factor;
	}

	public void setFactor(double factor) {
		this.factor = factor;
	}

	@Override
	public void draw(Graphics g) {
		if (isVisible()) {
			if (background != null) {
				if (isMouseOver() && clrFocus == null) {
					background.draw(getX(), getY(), getWidth(), getHeight(), hoverColor);
				} else if (isEnabled() ){
					if (clrFocus != null) {
						background.draw(getX(), getY(), getWidth(), getHeight(), clrFocus);
					} else {
						background.draw(getX(), getY(), getWidth(), getHeight(), defaultColor);
					}
				} else {
					background.draw(getX(), getY(), getWidth(), getHeight(), Color.gray);
				}
			}
			
			if (font != null) {
				font.drawString(getX() + (getWidth() / 2 - font.getWidth(text) / 2), 
						getY() +  Math.round(zoom) + (getHeight() / 2 - font.getHeight(text) / 2), text);
			}
		}
	}

	@Override
	public void update(int delta) {		
		
		// Solving hover bug
		area.setX(getX());
		area.setY(getY());
		
		super.update(delta);
		
		if (isMouseOver()) {
			zoomEffect(delta);
		} else {
			if (zoom != 0) {
				zoom = 0;
			}
		}
	}
	
	@Override
	public float getX() {
		return (float) (super.getX() - Math.ceil(zoom));
	}

	@Override
	public float getY() {
		return (float) (super.getY() - Math.ceil(zoom));
	}

	@Override
	public int getWidth() {
		return (int) Math.ceil(super.getWidth() + Math.ceil(zoom) * 2);
	}

	@Override
	public int getHeight() {
		return (int) Math.ceil(super.getHeight() + Math.ceil(zoom) * 2);
	}

	private void zoomEffect(int delta) {
		if (zoomIn) {
			if (zoom >= limit) {
				zoomIn = false;				
			} else zoom += delta * factor;
		} else {
			if (zoom <= -limit) {
				zoomIn = true;
			} else zoom -= delta * factor;
		}
	}
	
	
	

}
