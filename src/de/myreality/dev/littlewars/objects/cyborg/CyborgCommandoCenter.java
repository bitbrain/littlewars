package de.myreality.dev.littlewars.objects.cyborg;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

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
		return 500 * rank;
	}
}
