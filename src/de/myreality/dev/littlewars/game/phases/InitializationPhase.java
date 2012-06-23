package de.myreality.dev.littlewars.game.phases;

import org.newdawn.slick.GameContainer;

import de.myreality.dev.littlewars.components.helpers.FlashHelper;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.world.Difficulty;
import de.myreality.dev.littlewars.world.Player;

public class InitializationPhase extends BasicGamePhase {

	public InitializationPhase(IngameState game) {
		super(game);

	}

	@Override
	public void update(GameContainer gc, int delta) {
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
			// Check difficulty
			switch (currentPlayer.getDifficulty().getState()) {
				case Difficulty.EASY:
					break;
				case Difficulty.MEDIUM:
					break;
				case Difficulty.HARD:
					break;
				case Difficulty.EXTREME:
					break;
			}
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
