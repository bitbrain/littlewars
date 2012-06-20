package de.myreality.dev.littlewars.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.components.Timer;
import de.myreality.dev.littlewars.objects.GUIObject;

public class FlashBox extends GUIObject {
	
	private GUIObject content;
	private Color clrBackground, clrBorder;
	private Timer timer;
	private int duration;

	public FlashBox(GUIObject content, int duration, GameContainer gc) {
		super(0, 0, gc);
		this.content = content;
		content.attachTo(this);
		width = gc.getWidth() / 3 * 2;
		height = gc.getHeight() / 3;
		x = gc.getWidth() /  2 - width / 2;
		y = gc.getHeight() /  2 - height / 2 - 55;
		content.setX(getWidth() / 2 - content.getWidth() / 2);
		content.setY(getHeight() / 2 - content.getHeight() / 2);
		clrBackground = new Color(0, 0, 0, 0.6f);
		clrBorder = new Color(0, 0, 0, 1.0f);
		timer = new Timer();
		timer.start();
		this.duration = duration;
	}

	@Override
	public void draw(Graphics g) {		
		if (isVisible()) {
			if (clrBackground.a > 0 || clrBorder.a > 0) {
				g.setColor(clrBackground);
				g.fillRoundRect(getX() - margin, getY() - margin, getWidth(), getHeight(), 5);
				g.setColor(clrBorder);
				g.drawRoundRect(getX() - margin, getY() - margin, getWidth(), getHeight(), 5);
				content.draw(g);			
			}
		}
	}

	@Override
	public void update(int delta) {
		super.update(delta);		
		
		if (timer.getMiliseconds() > duration) {
			if (clrBackground.a > 0.0f) {
				clrBackground.a -= 0.0003f * delta;
			}
			if (clrBorder.a > 0.0f) {
				clrBorder.a -= 0.0006f * delta;
			}
			
			content.setOpacity(clrBorder.a);
		} else timer.update(delta);
		
	}

	
}
