package de.myreality.dev.littlewars.gui.bottommenu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.GameText;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.objects.GUIObject;
import de.myreality.dev.littlewars.world.GameWorld;

public class UnitAvatarInfo extends GUIObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GameWorld targetWorld;
	private static int borderPadding = 12;
	private GameText unitName;
	private UnitStateBar unitState;
	private Image topShadow;
	private GameText unitRank;

	public UnitAvatarInfo(BottomMenu parent, GameContainer gc) {
		super(borderPadding + parent.getWidth() - parent.getHeight(), borderPadding, gc);
		attachTo(parent);
		width = parent.getHeight() - borderPadding * 2;
		height = width;	
		unitName = new GameText(5, 2, "Name", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		unitName.attachTo(this);
		unitState = new UnitStateBar(this, 15, 8, gc);
		topShadow = ResourceManager.getInstance().getImage("GUI_TOPSHADOW");
		unitRank  = new GameText(5, 0, ResourceManager.getInstance().getText("UNIT_RANK"), ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		unitRank.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		unitRank.attachTo(this);
		unitRank.setY(getHeight() - unitState.getHeight() - unitRank.getHeight() - 2);
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		unitState.update(delta);
		if (targetWorld != null) {
			ArmyUnit unit = (ArmyUnit) targetWorld.getHoverObject();
			if (unit == null) {
				unit = (ArmyUnit) targetWorld.getFocusObject();
			}
			
			if (unit != null) {
				
				unitName.setText(unit.getName());
				unitState.setUnit(unit);
				
				if (!unit.isDead()) {
					unitRank.setText(ResourceManager.getInstance().getText("UNIT_RANK") + " " + String.valueOf(unit.getRank()));
					unitRank.setColor(ResourceManager.getInstance().getColor("COLOR_LEVEL"));
				} else {
					unitRank.setColor(Color.red);
					unitRank.setText(ResourceManager.getInstance().getText("UNIT_DEAD"));
				}
			} else {
				unitName.setText("");
				unitState.setUnit(null);
				unitRank.setText("");
			}
		} else {
			unitName.setText("");
			unitRank.setText("");
			unitState.setUnit(null);
		}
		
	}

	@Override
	public void draw(Graphics g) {
		if (targetWorld != null) {
			ArmyUnit unit = (ArmyUnit) targetWorld.getHoverObject();
			if (unit == null) {
				unit = (ArmyUnit) targetWorld.getFocusObject();
			}
			g.setColor(Color.black);
			g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 5);
			
			if (unit != null) {
				unit.getImgAvatar().draw(getX() + 2, getY() + 2, getWidth() - 4, getHeight() - 4, unit.getPlayer().getColor());	
				topShadow.draw(getX() + 2, getY() + 2, getWidth() - 4, unitName.getHeight() + 20);
			} 
		}
		
		unitRank.draw(g);
		unitName.draw(g);
		unitState.draw(g);
	}

	public GameWorld getTargetWorld() {
		return targetWorld;
	}

	public void setTargetWorld(GameWorld targetWorld) {
		this.targetWorld = targetWorld;
	}

}
