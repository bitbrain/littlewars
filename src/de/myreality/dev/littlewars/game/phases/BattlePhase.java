package de.myreality.dev.littlewars.game.phases;

import org.newdawn.slick.GameContainer;

import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.ki.Player;

public class BattlePhase extends BasicGamePhase {

	public BattlePhase(IngameState game) {
		super(game);
	}

	@Override
	public void update(GameContainer gc, int delta) {
		Player currentPlayer = game.getCurrentPlayer();
		
		if (!currentPlayer.hasAvailableUnits()) {
			game.setPhase(IngameState.INIT);
			currentPlayer.activateUnits();
			Player next = game.getNextPlayer();
			next.getMoney().addCredits(500);				
			game.setCurrentPlayer(next, gc);
			if (next.isClientPlayer()) {
				game.getTracker().record();
			}
		}
		if (currentPlayer.isCPU()) {
			currentPlayer.doBattle(delta);
		} else {
			// Client Player
			if (game.getTopMenu().getBtnPhaseQuit().onClick()) {
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
