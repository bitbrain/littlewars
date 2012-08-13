package de.myreality.dev.littlewars.gui.bottommenu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.GUIObject;
import de.myreality.dev.littlewars.objects.ArmyUnit;

public class UnitStateBar extends GUIObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArmyUnit unit;
	private ValueBar lifeBar, expBar;

	public UnitStateBar(UnitAvatarInfo parent, int lifeHeight, int expHeight, GameContainer gc) {
		super(0, parent.getHeight() - (lifeHeight + expHeight), gc);		
		width = parent.getWidth();
		this.height = lifeHeight + expHeight;
		attachTo(parent);
		lifeBar = new ValueBar(1, getHeight() - lifeHeight - expHeight - 1, getWidth() - 2, lifeHeight, gc);
		expBar = new ValueBar(1, getHeight() - (expHeight + lifeBar.getBorder()), getWidth() - 2, expHeight + lifeBar.getBorder(), gc);
		lifeBar.attachTo(this);
		lifeBar.setColor(ResourceManager.getInstance().getColor("UNIT_LIFE_FULL"));
		expBar.attachTo(this);
		expBar.setColor(ResourceManager.getInstance().getColor("UNIT_EXP"));		
		lifeBar.setBorder(1);
		expBar.setBorder(1);
	}

	@Override
	public void draw(Graphics g) {		
		if (unit != null) {
			lifeBar.draw(g);
			expBar.draw(g);
		}
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		lifeBar.update(delta);
		expBar.update(delta);
		
		if (unit != null) {
			if (!unit.isDead()) {
				if (unit.getLifePercent() > 60) {
					lifeBar.setColor(ResourceManager.getInstance().getColor("UNIT_LIFE_FULL"));
				} else if (unit.getLifePercent() > 30) {
					lifeBar.setColor(ResourceManager.getInstance().getColor("UNIT_LIFE_MEDIUM"));
				} else {
					lifeBar.setColor(ResourceManager.getInstance().getColor("UNIT_LIFE_EMPTY"));
				}
			}		
			lifeBar.setPercent(unit.getLifePercent());
			expBar.setPercent(unit.getExperiencePercent());
		}
	}

	public ArmyUnit getUnit() {
		return unit;
	}

	public void setUnit(ArmyUnit unit) {
		this.unit = unit;
	}

}
