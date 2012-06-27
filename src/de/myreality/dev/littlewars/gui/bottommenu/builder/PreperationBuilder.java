package de.myreality.dev.littlewars.gui.bottommenu.builder;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.components.Pair;
import de.myreality.dev.littlewars.gui.bottommenu.BottomMenu;
import de.myreality.dev.littlewars.ki.Player;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.objects.GUIObject;

public class PreperationBuilder extends BasicUnitBuilder {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PreperationBuilder(GUIObject left, GUIObject right, BottomMenu parent, Player player, GameContainer gc) {
		super(left, right, parent, player, gc);

		for (Pair<ArmyUnit, Integer> pair : generator.generateStartUnits()) {
			addPreview(pair.getFirst(), 0);
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
		player.addArmyUnit(selected.getUnit());
		previews.remove(selected);
		selected = getNextPreview();
		changedSize = true;		
	}
}
