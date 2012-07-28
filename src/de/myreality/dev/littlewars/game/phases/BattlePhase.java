package de.myreality.dev.littlewars.game.phases;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.ki.Player;
import de.myreality.dev.littlewars.objects.ArmyUnit;

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
	
		if (!currentPlayer.hasAvailableUnits() && !ArmyUnit.isUnitMoving()) {
			nextPlayerTurn(currentPlayer, gc);	
		} else {
			if (currentPlayer.isCPU()) {
				currentPlayer.doBattle(delta);
			} else {
				// Client Player
				if (game.getTopMenu().getBtnPhaseQuit().onClick()) {
					nextPlayerTurn(currentPlayer, gc);
				}
			}
		}
	}

}
