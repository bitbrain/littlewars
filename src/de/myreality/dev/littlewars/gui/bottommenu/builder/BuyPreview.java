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
import org.newdawn.slick.Image;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.GUIObject;
import de.myreality.dev.littlewars.gui.bottommenu.UnitShortcut;
import de.myreality.dev.littlewars.objects.ArmyUnit;

public class BuyPreview extends GUIObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArmyUnit unit;
	private int credits;
	private BasicUnitBuilder builder;
	protected Color clrNotAvailable;
	private Image background, backgroundHover;

	public BuyPreview(BasicUnitBuilder builder, ArmyUnit unit, int credits, GameContainer gc) {
		super(0, 0, gc);
		this.builder = builder;
		this.unit = unit;
		this.credits = credits;
		clrNotAvailable = new Color(100, 100, 100, 255);
		background = ResourceManager.getInstance().getImage("GUI_BOTTOM_SHORTCUT");
		backgroundHover = ResourceManager.getInstance().getImage("GUI_BOTTOM_SHORTCUT_HOVER");
	}

	@Override
	public void draw(Graphics g) {
		Color colorOriginal = ResourceManager.getInstance().getColor("COLOR_MAIN");
		Color color = new Color(colorOriginal.r, colorOriginal.g, colorOriginal.b, (float) 0.2);
		if (isMouseOver()) {		
			
			if (isBuyable()) {
				if (equals(builder.getSelected())) {
					backgroundHover.draw(getX(), getY(), getWidth(), getHeight(), colorOriginal);
				} else {
					backgroundHover.draw(getX(), getY(), getWidth(), getHeight());
				}
				unit.getImgAvatar().draw(getX() + UnitShortcut.BORDER, getY() + UnitShortcut.BORDER, getWidth() - UnitShortcut.BORDER * 2, getHeight() - UnitShortcut.BORDER * 2, unit.getPlayer().getColor());		
			} else {
				background.draw(getX(), getY(), getWidth(), getHeight());
				unit.getImgAvatar().draw(getX() + UnitShortcut.BORDER, getY() + UnitShortcut.BORDER, getWidth() - UnitShortcut.BORDER * 2, getHeight() - UnitShortcut.BORDER * 2, clrNotAvailable);		
			}
			if (equals(builder.getSelected())) {
				g.setColor(color);
				g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 3);
			}
		} else {
			if (equals(builder.getSelected())) {
				backgroundHover.draw(getX(), getY(), getWidth(), getHeight(), colorOriginal);
			} else {
				background.draw(getX(), getY(), getWidth(), getHeight());
			}
			if (isBuyable()) {
				unit.getImgAvatar().draw(getX() + UnitShortcut.BORDER, getY() + UnitShortcut.BORDER, getWidth() - UnitShortcut.BORDER * 2, getHeight() - UnitShortcut.BORDER * 2, unit.getPlayer().getColor());
			} else {
				unit.getImgAvatar().draw(getX() + UnitShortcut.BORDER, getY() + UnitShortcut.BORDER, getWidth() - UnitShortcut.BORDER * 2, getHeight() - UnitShortcut.BORDER * 2, clrNotAvailable);
			}
			if (equals(builder.getSelected())) {
				g.setColor(color);
				g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 3);
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
