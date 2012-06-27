package de.myreality.dev.littlewars.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;


public class ColorButton extends StateButton<Color> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Color lastColor;
	
	public ColorButton(int x, int y, int width, int height, GameContainer gc)
			throws SlickException {
		super(x, y, width, height, gc);
		lastColor = null;
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		try {
			if (!getCurrentValue().equals(defaultColor)) {
				
				if (lastColor == null) {
					lastColor = getCurrentValue();
				} else {
					lastColor = defaultColor;
				}
				defaultColor = getCurrentValue();	
				hoverColor = getCurrentValue();
			}
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
	}
	
	public Color getLastColor() {
		return lastColor;
	}
	
	

}
