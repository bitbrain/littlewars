/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Provides a gamestate of showing the ingame setup
 * 
 * @version 	0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.GameSettings;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.Button;
import de.myreality.dev.littlewars.gui.GameText;
import de.myreality.dev.littlewars.gui.PlayerSettings;
import de.myreality.dev.littlewars.gui.ZoomButton;

public class PlayerSetupState extends CustomGameState {

	// background image
	private Image backgroundImage;
	
	// buttons for going back order switch to the next menu
	private Button btnBack, btnNext;
	
	// menu caption
	private GameText caption;
	
	// Player state box
	PlayerSettings settings;
	
	/**
	 * Constructor of PlayerSetupSTate
	 * 
	 * @param stateID game state id
	 */
	public PlayerSetupState(int stateID) {
		super(stateID);
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		super.init(gc, sbg);
		// Initialize all elements
		backgroundImage = ResourceManager.getInstance().getImage("MENU_BACKGROUND");	
		caption         = new GameText(60, 50, ResourceManager.getInstance().getText("TXT_MENU_SETUP_PLAYER"), ResourceManager.getInstance().getFont("FONT_CAPTION"), gc);
		btnBack         = new ZoomButton(60, gc.getHeight() - 110, 250, 70, ResourceManager.getInstance().getText("TXT_GAME_BACK") , gc);
		btnNext         = new ZoomButton(gc.getWidth() - 310, gc.getHeight() - 110, 250, 70, ResourceManager.getInstance().getText("TXT_GAME_START") , gc);
		btnNext.setEnabled(false);
		settings = new PlayerSettings(60, 140, gc);	
	}	
	
	
	

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		settings.clear();
		for (int i = 0; i < Integer.parseInt(GameSettings.getInstance().getMapConfig().getPlayerCount()); ++i) {
			settings.addPlayer();
		}
	}

	@Override
	public void renderContent(GameContainer gc, StateBasedGame sbg, Graphics g) {
		backgroundImage.draw(0, 0, gc.getWidth(), gc.getHeight());
		btnBack.draw(g);
		btnNext.draw(g);
		caption.draw(g);
		settings.draw(g);
	}

	@Override
	public void updateContent(GameContainer gc, StateBasedGame sbg, int delta) {
		settings.update(delta);
		btnBack.update(delta);
		btnNext.update(delta);
		if (gc.getInput().isKeyDown(Input.KEY_ESCAPE) || btnBack.onMouseClick()) {
			gc.getInput().clearKeyPressedRecord();
			gc.getInput().clearMousePressedRecord();
			settings.reset();
			GameSettings.getInstance().setPlayers(settings.getPlayers());
			sbg.enterState(LittleWars.MAP_SETUP_STATE);
		}		
		
		if (settings.isReady()) {
			btnNext.setEnabled(true);
		} else {
			btnNext.setEnabled(false);
		}
		
		if (btnNext.onMouseClick()) {
			GameSettings.getInstance().setPlayers(settings.getPlayers());
			sbg.addState(new LoadingState(LittleWars.LOADING_STATE));
			try {
				sbg.getState(LittleWars.LOADING_STATE).init(gc, sbg);
			} catch (SlickException e) {
				e.printStackTrace();
			}
			sbg.enterState(LittleWars.LOADING_STATE);
		}
	}

}
