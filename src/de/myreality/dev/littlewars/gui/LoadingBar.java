package de.myreality.dev.littlewars.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import de.myreality.dev.littlewars.components.resources.ResourceManager;

public class LoadingBar extends GUIObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float percent;
	private Image shader;
	private boolean unsized;
	private float barPos;
	private float barWidth;

	public LoadingBar(int x, int y, int width, int height, GameContainer gc) {
		super(x, y, gc);
		percent = 0.0f;
		this.width = width;
		this.height = height;
		shader = ResourceManager.getInstance().getImage("GUI_VALUEBAR_LIGHT");
		unsized = false;
		barPos = 0;
		barWidth = 20.0f;
	}

	
	public void sizable(boolean value) {
		unsized = !value;
	}
	@Override
	public void draw(Graphics g) {
		if (isVisible()) {
			g.setColor(Color.black);
			g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 5);
			g.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
			
			if (!unsized) {
				g.fillRect(getX() + 3, getY() + 3, percent * (getWidth() - 6), getHeight() - 6);
				if (shader != null) {
					shader.draw(getX() + 2, getY() + 2, percent * (getWidth() - 6), getHeight() - 6);
				}
			} else {			
				int renderWidth = (int) (getWidth() * (barWidth / 100));
				
				if (barPos + 3 + renderWidth < getWidth()) {
					g.fillRect(getX() + barPos + 3, getY() + 3, renderWidth - 6, getHeight() - 6);
					if (shader != null) {
						shader.draw(getX() + barPos + 3, getY() + 3, renderWidth - 6, getHeight() - 6);
					}
				} else {				
					
					g.fillRect(getX() + barPos + 3, getY() + 3, getWidth() - barPos - 6, getHeight() - 6);
					if (shader != null) {
						shader.draw(getX() + barPos + 3, getY() + 3, getWidth() - barPos - 6, getHeight() - 6);
					}
					
					int restWidth = (int) (renderWidth - ((getWidth() - 6) - (barPos + 3)));
	
					g.fillRect(getX() + 3, getY() + 3, restWidth - 6, getHeight() - 6);
					if (shader != null) {
						shader.draw(getX() + 3, getY() + 3, restWidth - 6, getHeight() - 6);
					}
				}
			}		
		}
	}
	
	
	
	@Override
	public void update(int delta) {
		super.update(delta);
		if (unsized) {
			barPos += 0.5f * delta;
			if (barPos > getWidth() - 6) {
				barPos = 0;
			}
		}
	}

	public void setPercent(float newVal) {
		if (!(newVal > 100.0f || newVal < 0)) {
			percent = newVal / 100;
		}
	}
	
}
