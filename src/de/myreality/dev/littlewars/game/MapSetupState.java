/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Provides a game state of showing the ingame setup
 * 
 * @version 	0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.game;

import java.io.FileNotFoundException;

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
import de.myreality.dev.littlewars.gui.MapSelector;
import de.myreality.dev.littlewars.gui.ZoomButton;
import de.myreality.dev.littlewars.world.Weather;

public class MapSetupState extends CustomGameState {
	
	// background image
	private Image backgroundImage;	
	
	// buttons for going back order switch to the next menu
	private Button btnBack, btnNext;
	
	// menu caption
	private GameText caption;
	
	// selected map text
	private GameText textSelected;
	
	// Additional map info
	private GameText textMapInfo;
	
	// Map selection
	MapSelector mapSelector;
	
	/**
	 * Constructor of MapSetupState
	 * 
	 * @param stateID
	 */
	public MapSetupState(int stateID) {
		super(stateID);
	}
	
	
	
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		
		if (mapSelector.getSelected() == null) {
			textSelected.setText("");
		}
		
		if (GameSettings.getInstance().getMapConfig() == null) {
			mapSelector.reset();
		}
	}




	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		super.init(gc, sbg);
		// Initialize GUI
		backgroundImage = ResourceManager.getInstance().getImage("MENU_BACKGROUND");
		btnBack         = new ZoomButton(60, gc.getHeight() - 110, 250, 70, ResourceManager.getInstance().getText("TXT_GAME_BACK") , gc);
		btnNext         = new ZoomButton(gc.getWidth() - 310, gc.getHeight() - 110, 250, 70, ResourceManager.getInstance().getText("TXT_GAME_NEXT") , gc);
		caption         = new GameText(60, 50, ResourceManager.getInstance().getText("TXT_MENU_SETUP_MAP"), ResourceManager.getInstance().getFont("FONT_CAPTION"), gc);
		textSelected    = new GameText((int) btnNext.getX(), (int) (btnNext.getY() - 65), "", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);		
		textMapInfo     = new GameText((int) btnNext.getX(), (int) (btnNext.getY() - 35), "", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		mapSelector     = new MapSelector(60, 230, gc);
		btnNext.setEnabled(false);
		textSelected.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		
		try {
			mapSelector.load("res/sectors/trial_sector.xml");
		} catch (FileNotFoundException e) {
			System.out.println("Error: Can't find the world file");
		}
		
	}

	@Override
	public void renderContent(GameContainer gc, StateBasedGame sbg, Graphics g) {
		backgroundImage.draw(0, 0, gc.getWidth(), gc.getHeight());
		btnBack.draw(g);
		btnNext.draw(g);
		caption.draw(g);
		textSelected.draw(g);
		textMapInfo.draw(g);
		mapSelector.draw(g);
	}

	@Override
	public void updateContent(GameContainer gc, StateBasedGame sbg, int delta) {
		btnBack.update(delta);
		btnNext.update(delta);
		mapSelector.update(delta);
				
		if (mapSelector.getSelected() != null) {
			if (!btnNext.isEnabled()) {
				btnNext.setEnabled(true);
			}
			
			if (!textSelected.getText().equals(mapSelector.getSelected().getMapName())) {
				textSelected.setText(mapSelector.getSelected().getMapName());
				textMapInfo.setText("Planet: " + mapSelector.getSelected().getWorldConfig().getName());
			} 
		} else {
			if (btnNext.isEnabled()) {
				btnNext.setEnabled(false);
			}
			textSelected.setText("");
			textMapInfo.setText("");
		}				
		
		if (gc.getInput().isKeyDown(Input.KEY_ESCAPE) || btnBack.onMouseClick()) {
			GameSettings.getInstance().clear();
			mapSelector.reset();
			sbg.enterState(LittleWars.MAINMENU_STATE);
		}
		
		if (btnNext.onMouseClick()) {			
			GameSettings.getInstance().setMapConfig(mapSelector.getSelected());
			// TODO: Add map weather here
			GameSettings.getInstance().setWeather(Weather.normal);
			sbg.enterState(LittleWars.PLAYER_SETUP_STATE);
		}
	}

}
