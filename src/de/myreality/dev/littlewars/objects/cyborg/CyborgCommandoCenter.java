package de.myreality.dev.littlewars.objects.cyborg;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import de.myreality.dev.littlewars.components.Debugger;
import de.myreality.dev.littlewars.components.UnitEmitter;
import de.myreality.dev.littlewars.components.resources.SpriteAnimationData;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.objects.Camera;
import de.myreality.dev.littlewars.objects.CommandoCenter;

public class CyborgCommandoCenter extends CommandoCenter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CyborgCommandoCenter(int x, int y, GameContainer gc,
			Camera cam, IngameState game) throws SlickException {
		super("COMMANDO_CENTER_CYBORG", x, y, gc, cam, game);	
		addUnitEmitter(SpriteAnimationData.DAMAGED, "EXPLOSION_SMALL");
		addUnitEmitter(SpriteAnimationData.DIE, "EXPLOSION_BIG");
		UnitEmitter damageEmitter = getUnitEmitter(SpriteAnimationData.DAMAGED);
		if (damageEmitter != null) {
			damageEmitter.setOffsetX(getWidth() / 2);
			damageEmitter.setOffsetY(getHeight() / 2);
			damageEmitter.setEnabled(false);
		} else {
			Debugger.getInstance().write("Damage emitter does not exist");
		}
		
		UnitEmitter dieEmitter = getUnitEmitter(SpriteAnimationData.DIE);
		dieEmitter.setEnabled(false);
	}

	@Override
	protected int getRankStrength(int rank) {
		return rank - 1;
	}

	@Override
	protected int getRankLife(int rank) {
		return 10000 * rank;
	}

	@Override
	protected int getRankDefense(int rank) {
		return rank - 1;
	}

	@Override
	protected int getRankExperience(int rank) {
		return 1000  + 50 * rank;
	}
}
