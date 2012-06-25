/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Main class containing the main function as well as the GameContainer
 * 
 * @version 	0.0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.game;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.Debugger;
import de.myreality.dev.littlewars.components.Updater;
import de.myreality.dev.littlewars.components.config.Configuration;
import de.myreality.dev.littlewars.components.config.Resolution;
import de.myreality.dev.littlewars.components.resources.ResourceManager;

/**
 * Main game class
 * 
 * @version 0.1
 * @author Miguel Gonzalez
 *
 */
public class LittleWars extends StateBasedGame {

	// Game-States
	public static final int INTRO_STATE          = 0;    
    public static final int CREDITS_STATE        = 1;
    public static final int SETTINGS_STATE       = 2;
    public static final int MAINMENU_STATE       = 3;
    public static final int MAP_SETUP_STATE      = 4;
    public static final int PLAYER_SETUP_STATE   = 5;
    public static final int INGAME_STATE         = 6;
    public static final int LOADING_STATE        = 7;
    public static final int UPDATING_STATE       = 8;
    public static final int STATISTIC_STATE      = 9;
    public static final int BUG_REPORT_STATE     = 10;
    
    /**
     * Constructor of LittleWars
     * 
     * @throws SlickException
     */
	public LittleWars() throws SlickException {
		super("");		
		// Init states		
		this.addState(new UpdatingState(LittleWars.UPDATING_STATE));		
		this.addState(new MainMenuState(LittleWars.MAINMENU_STATE));
		this.addState(new CreditsState(LittleWars.CREDITS_STATE));
		this.addState(new SettingsState(LittleWars.SETTINGS_STATE));	
		this.addState(new MapSetupState(LittleWars.MAP_SETUP_STATE));		
		this.addState(new PlayerSetupState(LittleWars.PLAYER_SETUP_STATE));	
	}
	
	
	
	/**
	 * Main method of the game
	 * 
	 * @param args application arguments
	 * @throws SlickException
	 * @throws IOException
	 * @throws LWJGLException
	 */
	public static void main(String[] args) throws SlickException, IOException, LWJGLException {		
		// Update resources if possible
		Updater.updateTemporary();
		
		// Load game settings and start game
		Resolution res = Configuration.getInstance().getResolution();		
		AppGameContainer container = new AppGameContainer(new LittleWars());		
		container.setDisplayMode(res.getX(), res.getY(), false);
		container.setClearEachFrame(false);
		container.setMinimumLogicUpdateInterval(10);		
		container.setUpdateOnlyWhenVisible(true);	
		Configuration.getInstance().apply(container);
		container.setShowFPS(false);
		//Debugger.getInstance().setEnabled(false);	
		container.start();		
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {		
		// Load Resources before initialization
		ResourceManager.getInstance().loadDefaultResources();
		AppGameContainer appgc = (AppGameContainer) gc;
		appgc.setTitle(Debugger.getInstance().getFullGameInfo());
		gc.setMouseCursor(new Image("res/cursors/pointer.png"), 0, 0);
		gc.setIcons(new String[] {"res/icon/32x32.tga", "res/icon/24x24.tga", "res/icon/16x16.tga"});
		Debugger.getInstance().write("Starting Game " + Debugger.getInstance().getFullGameInfo() + "..");
		// TODO: Implement emitters
		//ResourceManager.getInstance().loadResources("res/particles/particles.xml", true);   // Emitters
	}	
}
