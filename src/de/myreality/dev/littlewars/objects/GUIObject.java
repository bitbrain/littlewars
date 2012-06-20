/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * General GUI object
 * 
 * @version 	0.0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.objects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;


public abstract class GUIObject extends GameObject {	
	
	// Padding
	protected int paddingLeft, paddingRight, paddingTop, paddingBottom, padding;
	
	// Margin
	protected int marginLeft, marginRight, marginTop, marginBottom, margin;
	protected boolean enabled;
	
	/**
	 * Constructor of GUIObject
	 * 
	 * @param x x-coordinate on the screen
	 * @param y y-coordinate on the screen
	 * @param gc GameContainer
	 */
	public GUIObject(int x, int y, GameContainer gc) {
		super(x, y, gc);
		enabled = true;
	}
	
	
	// TODO Create javadoc here
	@Override
	public boolean onClick() {
		return enabled && super.onClick();
	}


	// TODO Create javadoc here
	@Override
	public boolean onHover() {
		return enabled && super.onHover();
	}

	// TODO Create javadoc here
	public boolean isEnabled() {
		return enabled;
	}


	// TODO Create javadoc here
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	// TODO Create javadoc here
	@Override
	public boolean isHover() {
		return enabled && super.isHover();
	}



	public int getPaddingLeft() {
		return paddingLeft;
	}

	public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	public int getPaddingRight() {
		return paddingRight;
	}

	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}

	public int getPaddingTop() {
		return paddingTop;
	}

	public void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
	}

	public int getPaddingBottom() {
		return paddingBottom;
	}

	public void setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
	}

	public int getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(int marginLeft) {
		this.marginLeft = marginLeft;
	}

	public int getMarginRight() {
		return marginRight;
	}

	public void setMarginRight(int marginRight) {
		this.marginRight = marginRight;
	}

	public int getMarginTop() {
		return marginTop;
	}

	public void setMarginTop(int marginTop) {
		this.marginTop = marginTop;
	}

	public int getMarginBottom() {
		return marginBottom;
	}

	public void setMarginBottom(int marginBottom) {
		this.marginBottom = marginBottom;
	}

	@Override
	public int getWidth() {
		return width + getPaddingLeft() + getPaddingRight();
	}

	@Override
	public int getHeight() {
		return height + getPaddingTop() + getPaddingBottom();
	}

	@Override
	public abstract void draw(Graphics g);

	public int getPadding() {
		return padding;
	}


	public void setPadding(int padding) {
		this.padding = padding;
	}


	public int getMargin() {
		return margin;
	}
	
	public void setMargin(int margin) {
		this.margin = margin;
	}
	
	
	
	@Override
	public void update(int delta) {
		super.update(delta);	
	}

	public void setOpacity(float opacity) {
		// TODO: General opacity for all objects
	}
}
