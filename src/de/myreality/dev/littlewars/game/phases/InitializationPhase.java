package de.myreality.dev.littlewars.game.phases;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.helpers.FlashHelper;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.ki.Player;

public class InitializationPhase extends BasicGamePhase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InitializationPhase(IngameState game) {
		super(game);

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		Player currentPlayer = game.getCurrentPlayer();
		if (currentPlayer.isUnitMoving()) {
			FlashHelper.getInstance().flash("Phase: Kampf", 500, gc);
			game.setPhase(IngameState.BATTLE);
		} 
		
		
		
		if (currentPlayer.isCPU()) {
			game.getTopMenu().getBtnPhaseQuit().setEnabled(false);
			// TODO: Implement KI
			FlashHelper.getInstance().flash("Phase: Initialisierung", 500, gc);
			game.setPhase(IngameState.INIT);
			currentPlayer.activateUnits();			
			Player next = game.getNextPlayer();
			next.getMoney().addCredits(500);
			game.setCurrentPlayer(next, gc);
			if (next.isClientPlayer()) {
				game.getTracker().record();
			}
			currentPlayer.doInitialisation(delta);
		} else {
			game.getTopMenu().getBtnPhaseQuit().setEnabled(true);
			// Client Player
			if (game.getTopMenu().getBtnPhaseQuit().onClick()) {
				FlashHelper.getInstance().flash("Phase: Initialisierung", 500, gc);
				game.setPhase(IngameState.INIT);
				currentPlayer.activateUnits();
				Player next = game.getNextPlayer();
				next.getMoney().addCredits(500);
				game.setCurrentPlayer(next, gc);
				
				if (next.isClientPlayer()) {
					game.getTracker().record();
				}
			}
		}
	}

}
