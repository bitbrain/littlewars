package de.myreality.dev.littlewars.gui.unit;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.GameText;
import de.myreality.dev.littlewars.gui.bottommenu.ValueBar;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.objects.Camera;
import de.myreality.dev.littlewars.objects.GUIObject;

public class UnitTileInfo extends GUIObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArmyUnit unit;
	private ValueBar lifeBar;
	private ValueBar expBar;
	private GameText rankText;

	public UnitTileInfo(ArmyUnit unit, Camera camera, int x, int y, GameContainer gc) {
		super(x, y, gc);
		this.camera = camera;
		this.unit = unit;
		// Life bar
		lifeBar = new ValueBar(0, -10, unit.getWidth(), 7, gc, camera);
		lifeBar.attachTo(this);		
		lifeBar.setBorder(1);
		lifeBar.setColor(ResourceManager.getInstance().getColor("UNIT_LIFE_FULL"));
		// Exp bar
		expBar  = new ValueBar(0, lifeBar.getHeight() - lifeBar.getBorder(), unit.getWidth(), 4, gc, camera);
		expBar.attachTo(lifeBar);
		expBar.setColor(ResourceManager.getInstance().getColor("UNIT_EXP"));
		expBar.setBorder(1);
		width = 32;
		height = 32;
		area = new Rectangle(x, y, 32, 32);
		// Rank text
		rankText = new GameText(0, height, "0",	ResourceManager.getInstance().getFont("FONT_SMALL"), gc, camera);
		rankText.setY(height - rankText.getHeight());
		rankText.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		rankText.attachTo(this);
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		lifeBar.update(delta);
		lifeBar.setPercent(unit.getLifePercent());
		if (!unit.isDead()) {
			if (unit.getLifePercent() > 60) {
				lifeBar.setColor(ResourceManager.getInstance().getColor("UNIT_LIFE_FULL"));
			} else if (unit.getLifePercent() > 30) {
				lifeBar.setColor(ResourceManager.getInstance().getColor("UNIT_LIFE_MEDIUM"));
			} else {
				lifeBar.setColor(ResourceManager.getInstance().getColor("UNIT_LIFE_EMPTY"));
			}
		}
		
		expBar.setPercent(unit.getExperiencePercent());
		if (!unit.isDead()) {
			rankText.setText(String.valueOf(unit.getRank()));
			rankText.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		} else {
			rankText.setText("d");
			rankText.setColor(Color.red);
		}
	}

	@Override
	public void draw(Graphics g) {
		
		if (!unit.isDead()) {
			expBar.draw(g);
			lifeBar.draw(g);
		}
		rankText.draw(g);
	}
	
	
	public ArmyUnit getUnit() {
		return unit;
	}

}
