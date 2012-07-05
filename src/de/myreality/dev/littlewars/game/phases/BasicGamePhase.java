/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * General game phase
 * 
 * @version 	0.4.5
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.game.phases;

import java.io.Serializable;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.helpers.FlashHelper;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.ki.Player;

public abstract class BasicGamePhase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected IngameState game;
	
	public BasicGamePhase(IngameState game) {
		this.game = game;
	}	
	
	public void setDone(boolean value) {
	}
	
	public abstract void update(GameContainer gc, StateBasedGame sbg, int delta);
	
	public void nextPlayerTurn(Player current, GameContainer gc) {
		FlashHelper.getInstance().flash("Phase: " + ResourceManager.getInstance().getText("TXT_GAME_PHASE_INITIALISATION"), 500, gc);
		game.setPhase(IngameState.INIT);
		current.activateUnits();
		Player next = game.getNextPlayer(current);
		next.addPeriodMoney();	
		game.setCurrentPlayer(next, gc);
		if (next.isClientPlayer()) {
			game.getTracker().record();
		}
	}
}
