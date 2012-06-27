/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Implements an image with a bouncing effect (up and down)
 * 
 * @version 	0.0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.components;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import de.myreality.dev.littlewars.objects.GameObject;

public class BouncingImage extends GameObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Target image
	private Image image;
	// Factor of bouncing
	private int bounceFactor;
	// Bounce position
	double imY;
	// Copy of the original Y in order to pretend it
	int rootY;
	// Bounce flip flop
	boolean top = true;
	
	
	
	/**
	 * Constructor of BouncingImage
	 * 
	 * @param x x-coordinate on the screen
	 * @param y y-coordinate on the screen
	 * @param image target image
	 * @param gc GameContainer
	 */
	public BouncingImage(int x, int y, Image image, GameContainer gc) {
		super(x, y, gc);
		this.image = image;
		bounceFactor = 10;
		rootY = y;
		imY = y;
	}

	
	
	/**
	 * Returns the width of the object
	 */
	@Override
	public int getWidth() {
		if (image != null) {
			return image.getWidth();
		} else {
			return 0;
		}
	}

	
	
	/**
	 * Returns the height of the object
	 */
	@Override
	public int getHeight() {
		if (image != null) {
			return image.getHeight();
		} else {
			return 0;
		}
	}

	
	
	@Override
	public void draw(Graphics g) {		
		image.draw(x, Math.round(imY));
	}

	
	
	@Override
	public void update(int delta) {
		if (top) {
			if (imY > rootY - bounceFactor) {
				imY -= 0.5;
			} else {
				top = false;
			}
		} else {
			if (imY < rootY + bounceFactor) {
				imY += 0.5;
			} else {
				top = true;
			}
		}
	}
	
	
	
	/**
	 * Sets the bouncing factor to another value
	 * 
	 * @param bounceFactor Strength of bouncing
	 */
	public void setBounceFactor(int bounceFactor) {
		this.bounceFactor = bounceFactor;
	}
}
