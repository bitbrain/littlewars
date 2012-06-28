package de.myreality.dev.littlewars.gui.bottommenu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import de.myreality.dev.littlewars.components.helpers.PopupHelper;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.gui.GameText;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.objects.CommandoCenter;
import de.myreality.dev.littlewars.objects.GUIObject;
import de.myreality.dev.littlewars.world.GameWorld;

public class UnitShortcut extends GUIObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArmyUnit sibling;
	private GameWorld world;
	private Color clrHover, clrFocus;
	private GUIObject menu;
	private ValueBar lifeBar;
	private ValueBar expBar;
	private GameText rankText;
	@SuppressWarnings("unused")
	private Sound hoverSound;

	public UnitShortcut(GUIObject menu, GameWorld world, ArmyUnit sibling, int x, int y, int width, int height, GameContainer gc) {
		super(x, y, gc);
		this.width = width;
		this.height = height;
		area = new Rectangle(getX(), getY(), getWidth(), getHeight());	
		this.sibling = sibling;
		this.world = world;
		clrHover = Color.black;
		clrFocus = ResourceManager.getInstance().getColor("COLOR_MAIN");
		this.menu = menu;
		// Life bar
		lifeBar = new ValueBar(0, 0, getWidth(), 7, gc);
		lifeBar.attachTo(this);		
		lifeBar.setBorder(1);
		lifeBar.setColor(ResourceManager.getInstance().getColor("UNIT_LIFE_FULL"));
		// Exp bar
		expBar  = new ValueBar(0, lifeBar.getHeight() - lifeBar.getBorder(), getWidth(), 4, gc);
		expBar.attachTo(lifeBar);
		expBar.setColor(ResourceManager.getInstance().getColor("UNIT_EXP"));
		expBar.setBorder(1);
		// Rank text
		rankText = new GameText(0, height, "0",	ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		rankText.setY(height - rankText.getHeight());
		rankText.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		rankText.attachTo(this);
		
		int offset = getHeight() - (lifeBar.getHeight() + expBar.getHeight());
		lifeBar.setY(offset);
		rankText.setY(offset - rankText.getHeight());
		hoverSound = ResourceManager.getInstance().getSound("SOUND_HOVER");
		PopupHelper.getInstance().addPopup(this, sibling.getName(), gc);
	}
	
	public void finalize() {
		PopupHelper.getInstance().removePopup(this);
	}

	@Override
	public void draw(Graphics g) {
		
		boolean drawActive = false;
		
		drawActive = isHover();
		
		if (!drawActive) {
			if (world.getFocusObject() != null) {
				if (world.getFocusObject().equals(sibling)) {
					drawActive = true;
				}
			}
		}
		
		// Check, if the current sibling can move
		Color drawColor = sibling.getPlayer().getColor();
		
		// Exceptions are Commando Centers, because they've to be generally gray
		boolean isCenterInBattle = sibling.getGame().getPhase() == IngameState.BATTLE && sibling instanceof CommandoCenter;
		
		if (sibling.getRemainingSpeed() < 1 && (!(sibling instanceof CommandoCenter)) || isCenterInBattle) {
			drawColor = Color.gray;
		}

		if (isHover() && !sibling.equals(world.getFocusObject())) {
			g.setColor(clrHover);
			g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 2);
			sibling.getImgAvatar().draw(getX() + 1, getY() + 1, getWidth() - 2, getHeight() - 2, drawColor);
		} else if (sibling.equals(world.getFocusObject())) {
			g.setColor(Color.black);
			g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 2);
			g.setColor(clrFocus);
			g.fillRoundRect(getX() + 1, getY() + 1, getWidth() - 2, getHeight() - 2, 2);
			sibling.getImgAvatar().draw(getX() + 2, getY() + 2, getWidth() - 4, getHeight() - 4, drawColor);
		} else {
			g.setColor(Color.black);
			g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 2);
			sibling.getImgAvatar().draw(getX() + 2, getY() + 2, getWidth() - 4, getHeight() - 4, drawColor);
		}
		
		if (!sibling.isDead()) {
			expBar.draw(g);
			lifeBar.draw(g);
		}
		rankText.draw(g);
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		if (onClick()) {
			world.focusCameraOnObject(sibling, gc);
		}
		
		if (onHover()) {
			// TODO: Find new sound
			//hoverSound.play();
		}
		
		if (isHover()) {
			world.setHoverObject(sibling);
		}  else if (menu.isHover()) {
			if (world.getHoverObject() != null) {
				if (world.getHoverObject().equals(sibling)) {
					world.setHoverObject(null);
				}
			}
		}
		
		if (onClick()) {
			world.setClickedObject(sibling);
			sibling.setOnClicked(true);
		}  else if (menu.isHover()) {
			if (world.getClickedObject() != null) {
				if (world.getClickedObject().equals(sibling)) {
					world.setClickedObject(null);					
				}
			}
		}
		
		lifeBar.update(delta);
		lifeBar.setPercent(sibling.getLifePercent());
		if (!sibling.isDead()) {
			if (sibling.getLifePercent() > 60) {
				lifeBar.setColor(ResourceManager.getInstance().getColor("UNIT_LIFE_FULL"));
			} else if (sibling.getLifePercent() > 30) {
				lifeBar.setColor(ResourceManager.getInstance().getColor("UNIT_LIFE_MEDIUM"));
			} else {
				lifeBar.setColor(ResourceManager.getInstance().getColor("UNIT_LIFE_EMPTY"));
			}
		}
		
		expBar.setPercent(sibling.getExperiencePercent());
		if (!sibling.isDead()) {
			rankText.setText(String.valueOf(sibling.getRank()));
			rankText.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		} else {
			rankText.setText("d");
			rankText.setColor(Color.red);
		}
	}
	
	
}
