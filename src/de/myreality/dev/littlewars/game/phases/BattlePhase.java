package de.myreality.dev.littlewars.game.phases;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.helpers.FlashHelper;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.ki.Player;

public class BattlePhase extends BasicGamePhase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BattlePhase(IngameState game) {
		super(game);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		Player currentPlayer = game.getCurrentPlayer();
		
	
		if (!currentPlayer.hasAvailableUnits() && !currentPlayer.isUnitMoving()) {
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
		
		/*for (Player p: game.getPlayers()) {
			if (p.isDefeated()) {
				FlashHelper.getInstance().flash(p.getName() + " has been defeated!", 1000, gc);
				Player next = game.getNextPlayer();
				next.getMoney().addCredits(500);
				game.removePlayer(currentPlayer);
				game.setCurrentPlayer(next, gc);
				if (next.isClientPlayer()) {
					game.getTracker().record();
				}
			}
			
			if (game.getPlayers().size() == 1) {
				game.endGame(sbg, gc, p);
			}
		}*/
		// TODO: Create a general winning procedure
	}

}
