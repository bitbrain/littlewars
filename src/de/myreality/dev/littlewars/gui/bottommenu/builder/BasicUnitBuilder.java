/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Represents a tool to build units
 * 
 * @version 	0.5
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.gui.bottommenu.builder;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import de.myreality.dev.littlewars.components.UnitGenerator;
import de.myreality.dev.littlewars.components.helpers.PopupHelper;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.gui.bottommenu.BottomMenu;
import de.myreality.dev.littlewars.ki.Player;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.objects.GUIObject;
import de.myreality.dev.littlewars.objects.cyborg.CyborgGenerator;
import de.myreality.dev.littlewars.world.Fraction;

public abstract class BasicUnitBuilder extends GUIObject {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Player player;
	protected IngameState game;
	protected UnitGenerator generator;
	protected List<BuyPreview> previews;
	protected boolean changedSize;
	protected boolean popupsUnset;
	protected BuyPreview selected;

	public BasicUnitBuilder(GUIObject left, GUIObject right, BottomMenu parent, Player player, GameContainer gc) {
		super((int) (left.getX() + left.getWidth()) + 10, 12, gc);
		selected = null;
		width = (int) (right.getX() - left.getX() - getX());
		height = right.getHeight();		
		attachTo(parent);
		this.changedSize = false;
		this.player = player;
		popupsUnset = false;
		game = parent.getGame();
		previews = new ArrayList<BuyPreview>();
		switch (player.getFraction().getType()) {
			case Fraction.CYBORG:
				generator = new CyborgGenerator(gc, game, player);
				break;
			case Fraction.HUMAN:
				// TODO: Implement human generator
				//generator = new HumanGenerator(gc, game, player);
				break;
		}
	}
	
	public int size() {
		return previews.size();
	}

	@Override
	public void draw(Graphics g) {
		if (isVisible()) {
			g.setColor(Color.black);
			g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 5);
			
			for (BuyPreview preview : previews) {
				preview.draw(g);
			}
			
			if (selected != null) {
				selected.getUnit().draw(g);
			}
		}
	}
	
	
	
	
	@Override
	public void setVisible(boolean value) {
		if (isVisible() && !value) {
			for (BuyPreview preview : previews) {
				preview.setVisible(false);
			}
		} else if (!isVisible() && value) {
			for (BuyPreview preview : previews) {
				preview.setVisible(true);
			}
		}
			
		super.setVisible(value);		
	}

	
	
	/**
	 * Adds a new preview to the bottom panel
	 * 
	 * @param unit Target unit
	 * @param credits credits of the unit
	 */
	public void addPreview(ArmyUnit unit, int credits) {
		BuyPreview preview = new BuyPreview(this, unit, credits, gc);
		
		if (credits > 0) {
			PopupHelper.getInstance().addPopup(preview, unit.getName(), " " + String.valueOf(credits) + "$", gc);
		} else {
			PopupHelper.getInstance().addPopup(preview, unit.getName(), " Click to place", gc);
		}
		previews.add(preview);
		changedSize = true;
	}
	
	public void removePreview(BuyPreview preview) {		
		previews.remove(preview);
		changedSize = true;
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		
		for (BuyPreview preview : previews) {
			preview.update(delta);
			if (preview.onClick()) {
				selected = preview;
			}
		}
		
		/*if (gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
			selected = null;
		} */
		
		
		if (selected != null) {
			selected.getUnit().setX(game.getWorld().getTotalMouseX(gc));
			selected.getUnit().setY(game.getWorld().getTotalMouseY(gc));	
			int curTotalX = game.getWorld().tileIndexX(game.getWorld().getCamera().getX() + gc.getInput().getMouseX());
			int curTotalY = game.getWorld().tileIndexY(game.getWorld().getCamera().getY() + gc.getInput().getMouseY());
			if (!game.getBottomMenu().isHover() && 
			    !game.getWorld().collisionExists(curTotalX, curTotalY) &&
			     game.getCurrentPlayer().getSpawnArea().isInRange(curTotalX, curTotalY) // TODO: Fix tile position here
			    && gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))	{
				// TODO: Fix collision bug while spawning a new unit
				clickAction(game.getWorld().getTotalMouseX(gc), game.getWorld().getTotalMouseY(gc));
			}
		}
		
		if (changedSize) {
			changedSize = false;
			realignPreviews(2);
		}
		
		if (selected != null) {
			if (!selected.isBuyable()) {
				selected = null;
			}
		}		
	}
	
	
	protected abstract void clickAction(float x, float y);
	
	private void realignPreviews(int rows) {
		
		for (BuyPreview preview : previews) {
			removeChild(preview);
		}

		int padding = 10;
		int posX = padding; int posY = padding;

		for (BuyPreview preview : previews) {			
			
			preview.attachTo(this);
			preview.setX(posX);
			preview.setY(posY);
			preview.resize((int) Math.ceil(getHeight() / rows), (int) Math.ceil(getHeight() / rows));
			posX += preview.getWidth() + padding;			
						
			if (getWidth() - posX < preview.getWidth()) {
				posY += preview.getHeight() + padding;
				posX = padding;
			}
			
			if (posY + preview.getHeight() > getHeight()) {				
				realignPreviews(++rows);
				break;
			}
		}
	}
	
	public boolean hasSelectedPreview() {
		return selected != null;
	}
	
	
	public BuyPreview getNextPreview() {
		if (!previews.isEmpty()) {
			return previews.get(0);
		} else return null;
	}
}
