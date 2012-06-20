/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Provides a game state of showing the game's settings
 * 
 * @version 	0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.game;

import java.io.IOException;
import java.util.Map.Entry;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.config.Configuration;
import de.myreality.dev.littlewars.components.config.Location;
import de.myreality.dev.littlewars.components.config.Resolution;
import de.myreality.dev.littlewars.components.helpers.FlashHelper;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.Button;
import de.myreality.dev.littlewars.gui.DescriptionField;
import de.myreality.dev.littlewars.gui.GameText;
import de.myreality.dev.littlewars.gui.StateButton;
import de.myreality.dev.littlewars.gui.ZoomButton;

/**
 * Shows some settings for the player
 * 
 * @version 0.1
 * @author Miguel Gonzalez
 *
 */
public class SettingsState extends CustomGameState {

	// background image
	private Image backgroundImage;
	private Button btnBack, btnSave;
	
	// button fields
	private DescriptionField fieldMusic, fieldSound, fieldResolution, fieldLocation, fieldVsync;
	
	// Location button
	StateButton<String> btnLocation;
	
	// Resolution button
	StateButton<Resolution> btnResolution;
	
	// Button for setup the vertical synchronisation
	StateButton<Boolean> btnVsync;
	
	// Button for setup the music
	StateButton<Boolean> btnMusic;
	
	// Button for setup the sound
	StateButton<Boolean> btnSound;
	
	// state caption
	private GameText caption;
	
	
	
	
	
	/**
	 * Constructor of SettingsState
	 * 
	 * @param stateID
	 */
	public SettingsState(int stateID) {
		super(stateID);
	}	
	
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		
		// Set Configuration to GUI		
		btnLocation.setState(Configuration.getInstance().getLocale());
		btnResolution.setState(Configuration.getInstance().getResolution());
		btnMusic.setState(Configuration.getInstance().isMusicOn());
		btnSound.setState(Configuration.getInstance().isSoundOn());
		btnVsync.setState(Configuration.getInstance().isVsync());
	}



	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		super.init(gc, sbg);
		backgroundImage = ResourceManager.getInstance().getImage("MENU_BACKGROUND");
		btnBack = new ZoomButton(60, gc.getHeight() - 110, 250, 70, ResourceManager.getInstance().getText("TXT_GAME_BACK") , gc);
		btnSave = new ZoomButton(gc.getWidth() - 310, gc.getHeight() - 110, 250, 70, ResourceManager.getInstance().getText("TXT_GAME_SAVE") , gc);
		caption = new GameText(60, 50, ResourceManager.getInstance().getText("TXT_MENU_PREFERENCES"),
				   ResourceManager.getInstance().getFont("FONT_CAPTION"), gc);
		
		
		int width = gc.getWidth() - 370;
		
		fieldResolution = new DescriptionField(60, 150, width, ResourceManager.getInstance().getText("TXT_SETTINGS_RESOLUTION"), gc);
		fieldVsync      = new DescriptionField(60, 220, width, ResourceManager.getInstance().getText("TXT_SETTINGS_VSYNC"), gc);			
		fieldLocation   = new DescriptionField(60, 290, width, ResourceManager.getInstance().getText("TXT_SETTINGS_LOCATION"), gc);
		fieldMusic      = new DescriptionField(60, 360, width, ResourceManager.getInstance().getText("TXT_SETTINGS_MUSIC"), gc);
		fieldSound      = new DescriptionField(60, 430, width, ResourceManager.getInstance().getText("TXT_SETTINGS_SOUND"), gc);
		
		// Config-Elements
		
		// Music
		btnMusic = new StateButton<Boolean>(0, 0, 250, 50, gc);
		btnMusic.addState(ResourceManager.getInstance().getText("TXT_GAME_TRUE"), true);
		btnMusic.addState(ResourceManager.getInstance().getText("TXT_GAME_FALSE"), false);	
		fieldMusic.append(btnMusic);
		
		// Sound
		btnSound = new StateButton<Boolean>(0, 0, 250, 50, gc);
		btnSound.addState(ResourceManager.getInstance().getText("TXT_GAME_TRUE"), true);
		btnSound.addState(ResourceManager.getInstance().getText("TXT_GAME_FALSE"), false);	
		fieldSound.append(btnSound);
		
		// Vsync
		btnVsync = new StateButton<Boolean>(0, 0, 250, 50, gc);
		btnVsync.addState(ResourceManager.getInstance().getText("TXT_GAME_TRUE"), true);
		btnVsync.addState(ResourceManager.getInstance().getText("TXT_GAME_FALSE"), false);
		fieldVsync.append(btnVsync);
		
		// Resolution
		btnResolution = new StateButton<Resolution>(0, 0, 250, 50, gc);
		//btnResolution.setEnabled(false);
		
		for (Resolution res : Resolution.getAvailableResolutions()) {
			btnResolution.addState(res.toString(), res);
		}

		// Append
		fieldResolution.append(btnResolution);
		
		// Language
		btnLocation = new StateButton<String>(0, 0, 250, 50, gc);
		
		for (Entry<String, String> code : Location.getInstance().getCodes().entrySet()) {
			btnLocation.addState(Location.getInstance().getLocationName(code.getKey()), code.getKey());
		}
		
		fieldLocation.append(btnLocation);
		
		
	}

	@Override
	public void renderContent(GameContainer gc, StateBasedGame sbg, Graphics g) {
		backgroundImage.draw(0, 0, gc.getWidth(), gc.getHeight());
		btnBack.draw(g);
		btnSave.draw(g);
		g.setColor(Color.white);
		caption.draw(g);
		fieldMusic.draw(g);
		fieldResolution.draw(g);
		fieldLocation.draw(g);
		fieldVsync.draw(g);
		fieldSound.draw(g);
	}

	@Override
	public void updateContent(GameContainer gc, StateBasedGame sbg, int delta) {
		btnSave.setEnabled(!areSettingsSynchronized());
		btnBack.update(delta);			
		fieldMusic.update(delta);
		fieldResolution.update(delta);
		fieldLocation.update(delta);
		fieldVsync.update(delta);
		fieldSound.update(delta);
		btnSave.update(delta);		
		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE) || btnBack.onClick()) {
			gc.getInput().clearMousePressedRecord();
			sbg.enterState(LittleWars.MAINMENU_STATE);
		}
		
		if (btnSave.onClick()) {
			// Apply configuration
			Configuration config = Configuration.getInstance();
			try {
				config.setLocale(btnLocation.getCurrentValue())
				      .setResolution(btnResolution.getCurrentValue())
				      .setVsync(btnVsync.getCurrentValue())
				      .setMusicOn(btnMusic.getCurrentValue())
				      .setSoundOn(btnSound.getCurrentValue())
				      .apply((AppGameContainer) gc);
			} catch (SlickException e1) {
				e1.printStackTrace();
			}
			try {
				ResourceManager.getInstance().loadResources("res/text/" + Configuration.getInstance().getLocale() + "/text.xml");
				Location.reload();
				sbg.addState(new MainMenuState(LittleWars.MAINMENU_STATE));
				sbg.addState(new CreditsState(LittleWars.CREDITS_STATE));
				sbg.addState(new SettingsState(LittleWars.SETTINGS_STATE));	
				sbg.addState(new MapSetupState(LittleWars.MAP_SETUP_STATE));		
				sbg.addState(new PlayerSetupState(LittleWars.PLAYER_SETUP_STATE));	
				sbg.getState(LittleWars.MAINMENU_STATE).init(gc, sbg);
				sbg.getState(LittleWars.CREDITS_STATE).init(gc, sbg);
				sbg.getState(LittleWars.SETTINGS_STATE).init(gc, sbg);
				sbg.getState(LittleWars.MAP_SETUP_STATE).init(gc, sbg);
				sbg.getState(LittleWars.PLAYER_SETUP_STATE).init(gc, sbg);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SlickException e) {
				e.printStackTrace();
			}
			// TODO: Implement language dependence
			FlashHelper.getInstance().flash("Settings have been saved.", 1000, gc);
			sbg.enterState(LittleWars.SETTINGS_STATE);
		}		
	}
	
	/**
	 * Returns true, when current settings are synchronized
	 */
	public boolean areSettingsSynchronized() {
		
		try {
			if (!btnLocation.getCurrentValue().equals(Configuration.getInstance().getLocale())) {
				return false;
			}
			if (!btnMusic.getCurrentValue().equals(Configuration.getInstance().isMusicOn())) {
				return false;
			}
			if (!btnResolution.getCurrentValue().equals(Configuration.getInstance().getResolution())) {
				return false;
			}
			if (!btnSound.getCurrentValue().equals(Configuration.getInstance().isSoundOn())) {
				return false;
			}
			if (!btnVsync.getCurrentValue().equals(Configuration.getInstance().isVsync())) {
				return false;
			}
		} catch (SlickException e) {
						e.printStackTrace();
		}
		
		return true;
	}

}
