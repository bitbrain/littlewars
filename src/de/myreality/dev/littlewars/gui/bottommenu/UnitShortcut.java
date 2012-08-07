package de.myreality.dev.littlewars.gui.bottommenu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import de.myreality.dev.littlewars.components.helpers.PopupHelper;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.gui.GUIObject;
import de.myreality.dev.littlewars.gui.GameText;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.objects.CommandoCenter;
import de.myreality.dev.littlewars.world.GameWorld;

public class UnitShortcut extends GUIObject {
	
	/**
	 * 
	 */
	public static int BORDER = 3;
	private static final long serialVersionUID = 1L;
	private ArmyUnit sibling;
	private GameWorld world;
	private GUIObject menu;
	private ValueBar lifeBar;
	private ValueBar expBar;
	private GameText rankText;
	@SuppressWarnings("unused")
	private Sound hoverSound;
	private Image background, backgroundHover;

	public UnitShortcut(GUIObject menu, GameWorld world, ArmyUnit sibling, int x, int y, int width, int height, GameContainer gc) {
		super(x, y, gc);
		this.width = width;
		this.height = height;
		area = new Rectangle(getX(), getY(), getWidth(), getHeight());	
		this.sibling = sibling;
		this.world = world;
		this.menu = menu;
		// Life bar
		lifeBar = new ValueBar(BORDER - 1, 0, getWidth() - BORDER * 2 + 2, 7, gc);
		lifeBar.attachTo(this);		
		lifeBar.setBorder(1);
		lifeBar.setColor(ResourceManager.getInstance().getColor("UNIT_LIFE_FULL"));
		// Exp bar
		expBar  = new ValueBar(BORDER - 2, lifeBar.getHeight() - lifeBar.getBorder(), getWidth() - BORDER * 2 + 2, 4, gc);
		expBar.attachTo(lifeBar);
		expBar.setColor(ResourceManager.getInstance().getColor("UNIT_EXP"));
		expBar.setBorder(1);
		// Rank text
		rankText = new GameText(BORDER, height, "0",	ResourceManager.getInstance().getFont("FONT_TINY"), gc);
		rankText.setY(height - rankText.getHeight());
		rankText.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		rankText.attachTo(this);
		
		int offset = getHeight() - (lifeBar.getHeight() + expBar.getHeight());
		lifeBar.setY(offset);
		rankText.setY(offset - rankText.getHeight());
		rankText.setColor(ResourceManager.getInstance().getColor("COLOR_LEVEL"));
		hoverSound = ResourceManager.getInstance().getSound("SOUND_HOVER");
		PopupHelper.getInstance().addPopup(this, sibling.getName(), gc);
		background = ResourceManager.getInstance().getImage("GUI_BOTTOM_SHORTCUT");
		backgroundHover = ResourceManager.getInstance().getImage("GUI_BOTTOM_SHORTCUT_HOVER");
	}
	
	public void finalize() {
		PopupHelper.getInstance().removePopup(this);
	}

	@Override
	public void draw(Graphics g) {
		
		boolean drawActive = false;
		
		drawActive = isMouseOver();
		
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
		boolean isCenterInBattle = sibling.getGame().getPhaseID() == IngameState.BATTLE && sibling instanceof CommandoCenter && sibling.getPlayer().isCurrentPlayer();
		
		if (sibling.getRemainingSpeed() < 1 && (!(sibling instanceof CommandoCenter)) || isCenterInBattle) {
			drawColor = Color.gray;
		}

		if (isMouseOver() && !sibling.equals(world.getFocusObject())) {
			backgroundHover.draw(getX(), getY(), getWidth(), getHeight());
			sibling.getImgAvatar().draw(getX() + BORDER, getY() + BORDER, getWidth() - BORDER * 2, getHeight() - BORDER * 2, drawColor);
		} else if (sibling.equals(world.getFocusObject())) {
			Color colorOriginal = ResourceManager.getInstance().getColor("COLOR_MAIN");
			Color color = new Color(colorOriginal.r, colorOriginal.g, colorOriginal.b, (float) 0.2);
			backgroundHover.draw(getX(), getY(), getWidth(), getHeight(), colorOriginal);
			g.setColor(color);
			g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 3);
			sibling.getImgAvatar().draw(getX() + BORDER, getY() + BORDER, getWidth() - BORDER * 2, getHeight() - BORDER * 2, drawColor);
		} else {
			background.draw(getX(), getY(), getWidth(), getHeight());
			sibling.getImgAvatar().draw(getX() + BORDER, getY() + BORDER, getWidth() - BORDER * 2, getHeight() - BORDER * 2, drawColor);
		}
		
		if (!sibling.isDead()) {
			lifeBar.draw(g);
			expBar.draw(g);			
		}
		rankText.draw(g);
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		if (onMouseClick()) {
			world.focusCameraOnObject(sibling, gc);
		}
		
		if (onMouseOver()) {
			// TODO: Find new sound
			//hoverSound.play();
		}
		
		if (isMouseOver()) {
			world.setHoverObject(sibling);
		}  else if (menu.isMouseOver()) {
			if (world.getHoverObject() != null) {
				if (world.getHoverObject().equals(sibling)) {
					world.setHoverObject(null);
				}
			}
		}
		
		if (onMouseClick()) {
			world.setClickedObject(sibling);
			sibling.setOnClicked(true);
		}  else if (menu.isMouseOver()) {
			if (world.getClickedObject() != null) {
				if (world.getClickedObject().equals(sibling)) {
					world.setClickedObject(null);					
				}
			}
		}
		
		
		// Fade popup out
		if (onMouseClick() && sibling instanceof CommandoCenter && world.getParentGame().getPhaseID() != IngameState.BATTLE) {
			setVisible(false);
		} else {
			setVisible(true);
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
			rankText.setColor(ResourceManager.getInstance().getColor("COLOR_LEVEL"));
		} else {
			rankText.setText("d");
			rankText.setColor(Color.red);
		}
	}
	
	
}
