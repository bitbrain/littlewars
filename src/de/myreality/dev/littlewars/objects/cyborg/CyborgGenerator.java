package de.myreality.dev.littlewars.objects.cyborg;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import de.myreality.dev.littlewars.components.UnitGenerator;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.world.Fraction;
import de.myreality.dev.littlewars.world.Player;

public class CyborgGenerator extends UnitGenerator {
	
	public CyborgGenerator(GameContainer gc, IngameState game, Player player) {
		super(new Fraction(Fraction.CYBORG), gc, game, player);
		
	}

	@Override
	public ArmyUnit generateUnitByID(int id) {
		ArmyUnit unit = null;
		try {
			switch (id) {
				case UNIT_1:		
					unit = new Annihilator(0, 0, gc, game.getWorld().getCamera(), game);
					unit.setPlayer(player);
					break;
				case UNIT_CENTER:					
					unit = new CyborgCommandoCenter(0, 0, gc, game.getWorld().getCamera(), game);
					unit.setPlayer(player);					
					break;
			}
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return unit;
	}
}
