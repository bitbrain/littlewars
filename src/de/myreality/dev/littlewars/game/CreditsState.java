/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Provides a gamestate of showing the game's credits
 * 
 * @version 	0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.game;

import java.io.FileNotFoundException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.CreditsShow;
import de.myreality.dev.littlewars.components.config.Configuration;
import de.myreality.dev.littlewars.components.fading.FadeShow;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.Button;
import de.myreality.dev.littlewars.gui.GameText;
import de.myreality.dev.littlewars.gui.ZoomButton;

public class CreditsState extends CustomGameState {	
	
	// Background image
	Image backgroundImage;
	
	// Credits show
	CreditsShow credits;
	
	// Fade show
	FadeShow animatedImages;
	
	// Caption text on the top
	GameText caption;
	
	// Background music
	Music music;
	
	// Button in order to navigating back
	Button backButton;
	
	
	
	/**
	 * Constructor of CreditsState
	 * 
	 * @param stateID GameState ID
	 */
	public CreditsState(int stateID) {
		super(stateID);
	}
	
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		if (!music.playing() && Configuration.getInstance().isMusicOn())
			music.loop();
	}

	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		super.init(gc, sbg);
		// Load and Initialize data
		backButton = new ZoomButton(60, gc.getHeight() - 110, 250, 70, ResourceManager.getInstance().getText("TXT_GAME_BACK") , gc);		
		backgroundImage = ResourceManager.getInstance().getImage("MENU_BACKGROUND");
		int fadeHeight = 250;
		animatedImages = new FadeShow(60, gc.getHeight() / 2 - fadeHeight / 2, fadeHeight, fadeHeight, gc);
		animatedImages.addSequence(ResourceManager.getInstance().getImage("ARTWORK_01"), 9000);
		animatedImages.addSequence(ResourceManager.getInstance().getImage("ARTWORK_02"), 9000);
		animatedImages.addSequence(ResourceManager.getInstance().getImage("ARTWORK_03"), 9000);		
		animatedImages.setLoop(true);
		animatedImages.play();	
		credits = new CreditsShow(gc.getWidth() - 320, 100, 100, 100, gc);
		music = ResourceManager.getInstance().getMusic("CREDITS_MUSIC");
		music.stop();
		try {
			credits.load("res/credits/credits.xml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		caption = new GameText(60, 50, ResourceManager.getInstance().getText("TXT_MENU_CREDITS"),
				   ResourceManager.getInstance().getFont("FONT_CAPTION"), gc);
	}

	@Override
	public void renderContent(GameContainer gc, StateBasedGame sbg, Graphics g) {
		// Render the output
		backgroundImage.draw(0, 0, gc.getWidth(), gc.getHeight());		
		credits.draw(g);
		animatedImages.draw(g);		
		g.setColor(Color.white);
		caption.draw(g);
		backButton.draw(g);
	}

	@Override
	public void updateContent(GameContainer gc, StateBasedGame sbg, int delta) {
		// Update the elements
		animatedImages.update(delta);
		backButton.update(delta);
		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE) || backButton.onClick()) {
			gc.getInput().clearMousePressedRecord();	
			sbg.enterState(LittleWars.MAINMENU_STATE);
		}
		
		credits.update(delta);
		caption.update(delta);
	}
}
