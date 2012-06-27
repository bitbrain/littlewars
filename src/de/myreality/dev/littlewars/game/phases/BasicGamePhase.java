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

import de.myreality.dev.littlewars.game.IngameState;

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
	
	public abstract void update(GameContainer gc, int delta);
}
