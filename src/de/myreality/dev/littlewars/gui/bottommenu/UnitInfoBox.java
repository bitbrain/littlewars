package de.myreality.dev.littlewars.gui.bottommenu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.GameText;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.objects.GUIObject;
import de.myreality.dev.littlewars.world.GameWorld;

public class UnitInfoBox extends GUIObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GameWorld targetWorld;
	private GameText txtLife, txtExp, txtStrength, txtDefense, txtSpeed, txtStrengthAdd, txtDefenseAdd, txtSpeedAdd;
	private GameText txtLifeDescription, txtExpDescription, txtStrengthDescription, txtDefenseDescription, txtSpeedDescription;
	

	public UnitInfoBox(GUIObject parent, GUIObject bottomMenu, GameContainer gc) {
		super(-200, 0, gc);
		attachTo(parent);
		width = 190;
		height = parent.getHeight();
		
		int distance = 80;
		int rowPadding = 3;
		
		// Life
		txtLifeDescription = new GameText(10, 5, "Life", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		txtLifeDescription.attachTo(this);
		txtLife = new GameText(distance, 0, "-", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		txtLife.attachTo(txtLifeDescription);
		// Experience
		txtExpDescription = new GameText(0, txtLifeDescription.getHeight() + rowPadding, "EXP", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		txtExpDescription.attachTo(txtLifeDescription);
		txtExp  = new GameText(distance, 0, "-", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		txtExp.attachTo(txtExpDescription);
		// Strength
		txtStrengthDescription = new GameText(0, txtExpDescription.getHeight() + rowPadding, "Str", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		txtStrengthDescription.attachTo(txtExpDescription);
		txtStrength = new GameText(distance, 0, "-", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		txtStrength.attachTo(txtStrengthDescription);
		txtStrengthAdd = new GameText(0, 0, "", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		txtStrengthAdd.attachTo(txtStrength);
		txtStrengthAdd.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		// Defense
		txtDefenseDescription = new GameText(0, txtStrengthDescription.getHeight() + rowPadding, "Def", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		txtDefenseDescription.attachTo(txtStrengthDescription);
		txtDefense = new GameText(distance, 0, "-", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		txtDefense.attachTo(txtDefenseDescription);
		txtDefenseAdd = new GameText(0, 0, "", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		txtDefenseAdd.attachTo(txtDefense);
		txtDefenseAdd.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		// Speed
		txtSpeedDescription = new GameText(0, txtDefenseDescription.getHeight() + rowPadding, "Speed", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		txtSpeedDescription.attachTo(txtDefenseDescription);
		txtSpeed = new GameText(distance, 0, "-", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		txtSpeed.attachTo(txtSpeedDescription);
		txtSpeedAdd = new GameText(0, 0, "", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		txtSpeedAdd.attachTo(txtSpeed);
		txtSpeedAdd.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		
	}
	
	

	@Override
	public void update(int delta) {
		super.update(delta);
		if (targetWorld != null) {
			ArmyUnit unit = (ArmyUnit) targetWorld.getHoverObject();
			if (unit == null) {
				unit = (ArmyUnit) targetWorld.getFocusObject();				
			}
			if (unit != null) {
				// Life
				if (unit.getLifePercent() > 60) {
					txtLife.setColor(ResourceManager.getInstance().getColor("UNIT_LIFE_FULL"));
				} else if (unit.getLifePercent() > 30) {
					txtLife.setColor(ResourceManager.getInstance().getColor("UNIT_LIFE_MEDIUM"));
				} else {
					txtLife.setColor(ResourceManager.getInstance().getColor("UNIT_LIFE_EMPTY"));
				}
				txtLife.setText(String.valueOf(unit.getCurrentLife()) + "/" + String.valueOf(unit.getLife()));
				// EXP
				txtExp.setColor(ResourceManager.getInstance().getColor("UNIT_EXP"));
				txtExp.setText(String.valueOf(unit.getCurrentExperience()) + "/" + String.valueOf(unit.getTotalExperience()));
				// Strength
				txtStrength.setText(String.valueOf(unit.getStrength() - unit.getStrengthAdd()));
				if (unit.getStrengthAdd() > 0) {
					txtStrengthAdd.setText("+" + String.valueOf(unit.getStrengthAdd()));
					txtStrengthAdd.setX(txtStrength.getWidth());
				} else {
					txtStrengthAdd.setText("");
				}
				// Defense
				txtDefense.setText(String.valueOf(unit.getDefense() - unit.getDefenseAdd()));
				if (unit.getDefenseAdd() > 0) {
					txtDefenseAdd.setText("+" + String.valueOf(unit.getDefenseAdd()));
					txtDefenseAdd.setX(txtDefense.getWidth());
				} else {
					txtDefenseAdd.setText("");
				}
				// Speed
				txtSpeed.setText(String.valueOf(unit.getSpeed() - unit.getSpeedAdd()));
				if (unit.getSpeedAdd() > 0) {
					txtSpeedAdd.setText("+" + String.valueOf(unit.getSpeedAdd()));
					txtSpeedAdd.setX(txtSpeed.getWidth());
				}else {
					txtSpeedAdd.setText("");
				}
			} else {
				// Life
				txtLife.setText("-");
				txtLife.setColor(Color.white);
				// EXP
				txtExp.setText("-");
				txtExp.setColor(Color.white);
				// Strength
				txtStrength.setText("-");
				txtStrengthAdd.setText("");
				// Defense
				txtDefense.setText("-");
				txtDefenseAdd.setText("");
				// Speed
				txtSpeed.setText("-");
				txtSpeedAdd.setText("");
			}
		}
		
	}



	@Override
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 5);
		// Life
		txtLifeDescription.draw(g);
		txtLife.draw(g);
		// EXP
		txtExpDescription.draw(g);
		txtExp.draw(g);
		// Strength
		txtStrengthDescription.draw(g);
		txtStrength.draw(g);
		txtStrengthAdd.draw(g);
		// Defense
		txtDefenseDescription.draw(g);
		txtDefense.draw(g);
		txtDefenseAdd.draw(g);
		// Speed
		txtSpeedDescription.draw(g);
		txtSpeed.draw(g);
		txtSpeedAdd.draw(g);
	}



	public GameWorld getTargetWorld() {
		return targetWorld;
	}



	public void setTargetWorld(GameWorld targetWorld) {
		this.targetWorld = targetWorld;
	}

}
