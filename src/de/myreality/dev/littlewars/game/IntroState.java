/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Provides a gamestate of showing the game's introduction
 * 
 * @version 	0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.fading.FadeShow;
import de.myreality.dev.littlewars.components.resources.ResourceManager;


public class IntroState extends CustomGameState {

	// Fade show
	FadeShow fadeShow;
	
	/**
	 * Constructor of IntroState
	 * 
	 * @param stateID GameState ID
	 * @throws SlickException
	 */
	public IntroState(int stateID) throws SlickException {
		super(stateID);	
	}
		
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {		
		super.init(gc, sbg);
		// Load and initialize data
		fadeShow = new FadeShow(0, 0, gc.getWidth(), gc.getHeight(), gc);		
		fadeShow.addSequence(ResourceManager.getInstance().getImage("INTRO_JAVA_LOGO"), 2000);
		fadeShow.addSequence(ResourceManager.getInstance().getImage("INTRO_SLICK_LOGO"), 2000);
		fadeShow.addSequence(ResourceManager.getInstance().getImage("INTRO_MYREALITY_LOGO"), 5000);
		fadeShow.setFadeColor(new Color(0, 0, 0, 255));
		fadeShow.play();		
	}

	@Override
	public void renderContent(GameContainer gc, StateBasedGame sbg, Graphics g) {	
		// Render output
		fadeShow.draw(g);
		g.setColor(Color.white);
	}

	@Override
	public void updateContent(GameContainer gc, StateBasedGame sbg, int delta) {		
		// Update elements
		fadeShow.update(delta);
		if (fadeShow.done() || gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
			sbg.enterState(LittleWars.MAINMENU_STATE);
		}
	}
}
