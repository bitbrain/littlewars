/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Provides a game state of showing the game's main menu
 * 
 * @version 	0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.BouncingImage;
import de.myreality.dev.littlewars.components.GameSettings;
import de.myreality.dev.littlewars.components.config.Configuration;
import de.myreality.dev.littlewars.components.helpers.ContextMenuHelper;
import de.myreality.dev.littlewars.components.helpers.ContextMenuHelper.ContextMenuEvent;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.Button;
import de.myreality.dev.littlewars.gui.GameInfo;
import de.myreality.dev.littlewars.gui.ZoomButton;

public class MainMenuState extends CustomGameState {
	
	// Background music
	Music music;
	
	// Background image	
	Image backgroundImage;
	
	// Header image
	BouncingImage headerImage;
	
	// Menu buttons
	Button btnEndGame, btnCredits, btnPreferences, btnNewGame;
	
	// Info on the bottom
	GameInfo gameInfo;
	

	/**
	 * Constructor of MainMenuState
	 * 
	 * @param stateID
	 */
	public MainMenuState(int stateID) {
		super(stateID);
	}
	
	
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		if (!music.playing() && Configuration.getInstance().isMusicOn()) {
			music.loop();	
		}		
	}

	
	


	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.leave(container, game);
	}



	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		super.init(gc, sbg);
		// Load background images
		backgroundImage = ResourceManager.getInstance().getImage("MENU_BACKGROUND");
		Image tmpImage = ResourceManager.getInstance().getImage("MENU_HEADER");
		headerImage = new BouncingImage(gc.getWidth() / 2 - tmpImage.getWidth() / 2, 50, tmpImage, gc);
		// Music
		music = ResourceManager.getInstance().getMusic("MENU_MUSIC");
		music.stop();
		// Configure buttons
		int marginTop = 10;
		int btnWidth = 300;
		int btnHeight = 70;
		btnNewGame = new ZoomButton(0, 200, btnWidth, btnHeight, ResourceManager.getInstance().getText("TXT_MENU_SINGLEPLAYER"), gc);		
		btnPreferences = new ZoomButton(0, (int)btnNewGame.getY() + btnNewGame.getHeight() + marginTop, btnWidth, btnHeight, ResourceManager.getInstance().getText("TXT_MENU_PREFERENCES"), gc);
		btnCredits = new ZoomButton(0, (int)btnPreferences.getY() + btnPreferences.getHeight() + marginTop, btnWidth, btnHeight, ResourceManager.getInstance().getText("TXT_MENU_CREDITS"), gc);
		btnEndGame = new ZoomButton(0, (int)btnCredits.getY() + btnCredits.getHeight() + marginTop, btnWidth, btnHeight, ResourceManager.getInstance().getText("TXT_MENU_CLOSE"), gc);
		btnNewGame.setX(gc.getWidth() / 2 - btnNewGame.getWidth() / 2);
		btnPreferences.setX(gc.getWidth() / 2 - btnPreferences.getWidth() / 2);
		btnCredits.setX(gc.getWidth() / 2 - btnCredits.getWidth() / 2);
		btnEndGame.setX(gc.getWidth() / 2 - btnEndGame.getWidth() / 2);		
	
		gameInfo = new GameInfo(gc.getWidth() / 2 - GameInfo.getAuthorTextWidth() / 2, gc.getHeight() - 60, gc);
		
	}

	@Override
	public void renderContent(GameContainer gc, StateBasedGame sbg, Graphics g) {		
		backgroundImage.draw(0, 0, gc.getWidth(), gc.getHeight());
		headerImage.draw(g);
		
		// Buttons
		btnNewGame.draw(g);
		btnPreferences.draw(g);
		btnCredits.draw(g);
		btnEndGame.draw(g);
		g.setColor(Color.white);
		gameInfo.draw(g);
	}

	@Override
	public void updateContent(GameContainer gc, StateBasedGame sbg, int delta) {
		btnNewGame.update(delta);
		btnPreferences.update(delta);
		btnCredits.update(delta);
		btnEndGame.update(delta);
		
		if (btnNewGame.onClick()) {
			// Delete event
			gc.getInput().clearKeyPressedRecord();
			gc.getInput().clearMousePressedRecord();
			GameSettings.getInstance().clear();
			sbg.enterState(LittleWars.MAP_SETUP_STATE);
		} else if (btnPreferences.onClick()) {
			// Delete event
			gc.getInput().clearKeyPressedRecord();
			gc.getInput().clearMousePressedRecord();
			sbg.enterState(LittleWars.SETTINGS_STATE);
		} else if (btnCredits.onClick()) {
			music.stop();
			// Delete event
			gc.getInput().clearKeyPressedRecord();
			gc.getInput().clearMousePressedRecord();
			sbg.enterState(LittleWars.CREDITS_STATE);
		} else if (btnEndGame.onClick()) {
			ContextMenuHelper.getInstance().show(gc, "Achtung", "Sie verlassen nun das Spiel.", new ContextMenuEvent() {

				@Override
				public void onAbort(GameContainer gc, StateBasedGame sbg,
						int delta) {
					
				}

				@Override
				public void onAccept(GameContainer gc, StateBasedGame sbg,
						int delta) {
					music.stop();
					gc.getInput().clearKeyPressedRecord();
					gc.getInput().clearMousePressedRecord();
					gc.exit();
				}
				
			});
		}
				
		headerImage.update(delta);		
		gameInfo.update(delta);
	}

}
