package de.myreality.dev.littlewars.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.objects.GUIObject;

public class PopupBox extends GUIObject {
	
	private GameText text;
	private Color clrBackground, clrBorder;
	private boolean visible;
	private static final int CUR_SIZE = 25;

	public PopupBox(String content, GameContainer gc) {
		super(0, 0, gc);
		setMargin(5);
		text = new GameText(0, 0, content, ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		text.attachTo(this);
		text.setColor(new Color(255, 255, 255, 0.0f));
		width = text.getWidth() + margin * 2;
		height = text.getHeight() + margin * 2;
		clrBackground = new Color(0, 0, 0, 0.0f);
		clrBorder = new Color(0, 0, 0, 0.0f);
		visible = false;
	}
	
	
	

	@Override
	public void draw(Graphics g) {
		if (getParent() != null) {
			if (isVisible() && getParent().isVisible()) {
				if (visible) {
					g.setColor(clrBackground);
					g.fillRoundRect(getX() - margin, getY() - margin, getWidth(), getHeight(), 5);
					g.setColor(clrBorder);
					g.drawRoundRect(getX() - margin, getY() - margin, getWidth(), getHeight(), 5);
					text.draw(g);
				}
			}
		}
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		
		if (getParent() != null) {
			if (getParent().isHover()) {
				visible = true;
				if (clrBackground.a < 0.5f) {
					clrBackground.a += 0.005f * delta;					
				}
				if (text.getColor().a < 1.0f) {
					text.getColor().a += 0.01f * delta;			
				}
			} else if (clrBackground.a <= 0.0f && text.getColor().a <= 0.0f) {
				visible = false;
			} else {
				if (clrBackground.a > 0.0f || text.getColor().a > 0.0f) {
					clrBackground.a -= 0.005f * delta;
					text.getColor().a -= 0.01f * delta;
				}
			}
			if (gc.getInput().getMouseX() < gc.getWidth() / 2) {
				x = gc.getInput().getMouseX() - getParent().getX() + CUR_SIZE;
			} else {
				x = gc.getInput().getMouseX() - getParent().getX() - getWidth();
			}
			
			if (gc.getInput().getMouseY() < gc.getHeight() / 2) {
				y = gc.getInput().getMouseY() - getParent().getY() + CUR_SIZE;
			} else {
				y = gc.getInput().getMouseY() - getParent().getY() - getHeight();
			}
			
			clrBorder.a = text.getColor().a;
		}
	}
}
