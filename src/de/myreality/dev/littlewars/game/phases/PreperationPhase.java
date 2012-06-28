/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Preperation phase
 * 
 * @version 	0.4.5
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.game.phases;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.helpers.FlashHelper;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.ki.Player;

public class PreperationPhase extends BasicGamePhase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Player> preparedPlayers;

	public PreperationPhase(IngameState game) {
		super(game);
		preparedPlayers = new ArrayList<Player>();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		Player currentPlayer = game.getCurrentPlayer();	
		
		if (isDone()) {
			for (Player player : preparedPlayers) {
				player.activateUnits();
			}
			FlashHelper.getInstance().flash("Phase: Initialisierung", 1000, gc);
			game.setPhase(IngameState.INIT);
			game.getTracker().record();
		}
		
		if (currentPlayer.isCPU()) {
			game.getTopMenu().getBtnPhaseQuit().setEnabled(false);
			if (currentPlayer.isPrepared() && !isDone()) { // TODO: Implement KI
				preparedPlayers.add(currentPlayer);
				game.setCurrentPlayer(game.getNextPlayer(), gc);
			}
			currentPlayer.doPreperation(delta);
		} else {
			// Client Player
			if (game.getBottomMenu().getPreperationBuilder().size() == 0 && !isDone()) {
				preparedPlayers.add(currentPlayer);
				game.setCurrentPlayer(game.getNextPlayer(), gc);
			}			
		}
	}
	
	private boolean isDone() {
		return preparedPlayers.size() == game.getPlayers().size();
	}

}
