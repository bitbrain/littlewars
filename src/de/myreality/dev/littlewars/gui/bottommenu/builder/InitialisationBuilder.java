package de.myreality.dev.littlewars.gui.bottommenu.builder;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.components.Pair;
import de.myreality.dev.littlewars.gui.GUIObject;
import de.myreality.dev.littlewars.gui.bottommenu.BottomMenu;
import de.myreality.dev.littlewars.ki.Player;
import de.myreality.dev.littlewars.objects.ArmyUnit;

public class InitialisationBuilder extends BasicUnitBuilder {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InitialisationBuilder(GUIObject left, GUIObject right, BottomMenu parent, Player player,
			GameContainer gc) {
		super(left, right, parent, player, gc);
		for (Pair<ArmyUnit, Integer> pair : generator.generateDefaultUnits()) {
			addPreview(pair.getFirst(), pair.getSecond());
		}	
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
	}

	@Override
	public void update(int delta) {
		super.update(delta);
	}

	@Override
	protected void clickAction(float x, float y) {
		if (player.getMoney().reduceCredits(selected.getCredits())) {
			ArmyUnit unit = generator.generateUnitByID(selected.getUnit().getID(), x, y);
			unit.playRandomSound("onClick");
			unit.setX(x);
			unit.setY(y);
			player.addArmyUnit(unit);
			selected = null;
		}		
	}
	
	


}
