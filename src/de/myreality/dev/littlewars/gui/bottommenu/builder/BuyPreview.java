/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Shows small preview of an unit
 * 
 * @version 	0.5
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.gui.bottommenu.builder;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.objects.GUIObject;

public class BuyPreview extends GUIObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArmyUnit unit;
	private int credits;
	@SuppressWarnings("unused")
	private BasicUnitBuilder builder;
	protected Color clrNotAvailable;

	public BuyPreview(BasicUnitBuilder builder, ArmyUnit unit, int credits, GameContainer gc) {
		super(0, 0, gc);
		this.builder = builder;
		this.unit = unit;
		this.credits = credits;
		clrNotAvailable = new Color(100, 100, 100, 255);
	}

	@Override
	public void draw(Graphics g) {
		if (isHover()) {			
			if (isBuyable()) {
				g.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
				g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 2);
				unit.getImgAvatar().draw(getX() + 1, getY() + 1, getWidth() - 2, getHeight() - 2, unit.getPlayer().getColor());		
			} else {
				g.setColor(Color.black);
				g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 2);
				unit.getImgAvatar().draw(getX() + 1, getY() + 1, getWidth() - 2, getHeight() - 2, clrNotAvailable);		
			}
		} else {
			g.setColor(Color.black);
			g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 2);
			if (isBuyable()) {
				unit.getImgAvatar().draw(getX() + 2, getY() + 2, getWidth() - 4, getHeight() - 4, unit.getPlayer().getColor());
			} else {
				unit.getImgAvatar().draw(getX() + 2, getY() + 2, getWidth() - 4, getHeight() - 4, clrNotAvailable);
			}
		}
	}

	public int getCredits() {
		return credits;
	}
	
	public ArmyUnit getUnit() {
		return unit;
	}
	
	public boolean isBuyable() {
		return unit.getPlayer().getMoney().getRealCredits() >= credits;
	}
}
