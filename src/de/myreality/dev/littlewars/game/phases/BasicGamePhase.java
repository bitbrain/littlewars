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

import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.ki.Player;

public abstract class BasicGamePhase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected IngameState game;
	private String name;
	
	public BasicGamePhase(IngameState game, String name) {
		this.game = game;
		this.name = name;
	}	
	
	public String getName() {
		return name;
	}
	
	public void setDone(boolean value) {
	}
	
	public abstract void update(GameContainer gc, StateBasedGame sbg, int delta);
	
	public void nextPlayerTurn(Player current, GameContainer gc) {
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
