/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Contains multiple settings for players
 * 
 * @version 	0.1.6
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.ki.CPU;
import de.myreality.dev.littlewars.ki.ManualPlayer;
import de.myreality.dev.littlewars.ki.Player;
import de.myreality.dev.littlewars.objects.GUIObject;
import de.myreality.dev.littlewars.world.Difficulty;
import de.myreality.dev.littlewars.world.Fraction;

public class PlayerSettings extends GUIObject {

	// All Settings
	private Map<Integer, SingleSetting> settings;
	
	// Current Player ID
	private int playerID;
	
	// Caption settings of the player
	private List<GUIObject> captions;	
	private GameText txtName, txtFraction, txtDifficulty, txtColor;	
	
	private static int captionHeight = 60;
	private static int captionWidth = 205;
	
	/**
	 * Constructor of PlayerSettings
	 * 
	 * @param x x-coordinate on the screen
	 * @param y y-coordinate on the screen
	 * @param gc GameContainer
	 */
	public PlayerSettings(int x, int y, GameContainer gc) {
		super(x, y, gc);
		// Initialize elements
		settings = new TreeMap<Integer, SingleSetting>();
		captions = new ArrayList<GUIObject>();
		playerID = Player.PL1;
		
		// Set captions		
		Font font = ResourceManager.getInstance().getFont("FONT_SMALL");
		Color captionColor = ResourceManager.getInstance().getColor("COLOR_MAIN");
		txtName = new GameText(0, 0, ResourceManager.getInstance().getText("TXT_SETUP_NAME"), font, gc);
		txtFraction = new GameText(captionWidth, 0, ResourceManager.getInstance().getText("TXT_SETUP_FRACTION"), font, gc);
		txtDifficulty = new GameText(captionWidth * 2, 0, ResourceManager.getInstance().getText("TXT_SETUP_DIFFICULTY"), font, gc);
		txtColor = new GameText(captionWidth * 3, 0, ResourceManager.getInstance().getText("TXT_SETUP_COLOR"), font, gc);
		
		txtName.attachTo(this);
		txtName.setColor(captionColor);
		txtFraction.attachTo(this);
		txtFraction.setColor(captionColor);
		txtDifficulty.attachTo(this);
		txtDifficulty.setColor(captionColor);
		txtColor.attachTo(this);
		txtColor.setColor(captionColor);
		
		captions.add(txtName);
		captions.add(txtFraction);
		captions.add(txtDifficulty);
		captions.add(txtColor);
	}
	
	
	/**
	 * @return The last state entry
	 */
	private Entry<Integer, SingleSetting> getLastEntry() {
		int count = 0;
		for (Entry<Integer, SingleSetting> entry : settings.entrySet()) {
			count++;
			if (count == settings.entrySet().size()) {
				return entry;
			}
		}
		
		return null;
	}
	
	
	
	/**
	 * Adds a new player setting
	 * 
	 */
	public void addPlayer() {		
		if (settings.get(playerID) == null) {
			int xPos = 0, yPos = captionHeight;
			Entry<Integer, SingleSetting> last = getLastEntry();
			
			if (last != null) {
				for (int i = 0; i < settings.size(); ++i) {
					yPos += (int) (last.getValue().getHeight()) + 20;
				}
			}
			
			boolean isCPU = !settings.isEmpty();
			
			SingleSetting single = new SingleSetting(xPos, yPos, playerID, isCPU, gc);
			single.attachTo(this);			
			settings.put(playerID, single);
			
			++playerID;
		}
	}
	
	public void clear() {
		settings.clear();
		playerID = Player.PL1;
	}

	@Override
	public void draw(Graphics g) {
		if (isVisible()) {
			for (GUIObject obj : captions) {
				obj.draw(g);
			}
			
			for (Entry<Integer, SingleSetting> entry : settings.entrySet()) {
				entry.getValue().draw(g);
			}
		}
	}
	
	
	
	@Override
	public void update(int delta) {
		super.update(delta);
		
		for (GUIObject obj : captions) {
			obj.update(delta);
		}

		for (Entry<Integer, SingleSetting> entry : settings.entrySet()) {			
			entry.getValue().update(delta);
			
			// Color update	
			if (entry.getValue().getColorButton().onClick()) {
				Color lastColor = entry.getValue().getColorButton().getLastColor();	
				for (Entry<Integer, SingleSetting> target : settings.entrySet()) {
					try {
						if (!entry.equals(target) && lastColor != null && target.getValue().getColorButton().getCurrentValue().equals(
						    entry.getValue().getColorButton().getCurrentValue())) {
							target.getValue().getColorButton().setState(lastColor);
							target.getValue().update(delta);
							break;
						}
					} catch (SlickException e) {
						e.printStackTrace();
					}
				}
			}
			
						
		}
	}
	
	
	public void reset() {
		for (Entry<Integer, SingleSetting> entry : settings.entrySet()) {
			entry.getValue().reset();
		}
	}
	
	public List<Player> getPlayers() {
		List<Player> plList = new ArrayList<Player>();
		for (Entry<Integer, SingleSetting> entry : settings.entrySet()) {	
				plList.add(entry.getValue().getPlayer());
		}
		
		return plList;
	}
	
	public boolean isReady() {
		
		for (Entry<Integer, SingleSetting> entry : settings.entrySet()) {	
			if (!entry.getValue().isReady()) {
				return false;
			}
		}
		
		return true;
	}

	public class SingleSetting extends GUIObject {

		// Player state
		Player player;
		
		// Name of the player
		GameText txtPlayerName;
		GameText edtPlayerName;
		//EditText editPlayerName; TODO: Fix EditText Bug
		
		// Fraction of the player
		StateButton<Fraction> btnFraction;
		
		// Difficulty of the player
		StateButton<Difficulty> btnDifficulty;
		
		// Color of the player
		ColorButton btnColor;		
		
		
		/**
		 * Constructor of SingleSetting
		 * 
		 * @param x x-coordinate on the screen
	     * @param y y-coordinate on the screen
	     * @param gc GameContainer
		 */
		public SingleSetting(int x, int y, int playerID, boolean isCPU, GameContainer gc) {
			super(x, y, gc);
			height = 50;
			try {
				// Name of the player
				String name = "";
				
				if (isCPU) {
					name = "CPU " + (playerID - 1);
					player = new CPU(playerID, gc);					
				} else {
					name = ResourceManager.getInstance().getText("TXT_SETUP_PLAYER");
					player = new ManualPlayer(playerID, gc);		
				}
				
				player.setName(name);
				
				txtPlayerName = new GameText(0, 0, name, ResourceManager.getInstance().getFont("FONT_MENU"), gc);
				txtPlayerName.attachTo(this);
				
				edtPlayerName = new GameText(0, 0, name, ResourceManager.getInstance().getFont("FONT_MENU"), gc);
				edtPlayerName.attachTo(this);
				
				// FractionButton
				btnFraction = new StateButton<Fraction>(0, 0, captionWidth - 20, height, gc);
				btnFraction.setX(captionWidth + txtFraction.getWidth() / 2 - btnFraction.getWidth() / 2);
				btnFraction.attachTo(this);
				
				// TODO: Implement human fraction
				//for (Fraction fraction : Fraction.getAsList()) {
				Fraction fraction = new Fraction(Fraction.CYBORG);
				btnFraction.addState(fraction.getName(), fraction);
				//}
				
				
				// DifficultyButton
				btnDifficulty = new StateButton<Difficulty>(0, 0, captionWidth - 20, height, gc);
				btnDifficulty.setX(captionWidth * 2 + txtDifficulty.getWidth() / 2 - btnDifficulty.getWidth() / 2);
				btnDifficulty.attachTo(this);
				
				if (isCPU) {				
					for (Difficulty difficulty : Difficulty.getAsCPUList()) {
						btnDifficulty.addState(difficulty.getName(), difficulty);
					}
				} else {
					Difficulty difficulty = new Difficulty(Difficulty.PLAYER);
					btnDifficulty.addState(difficulty.getName(), difficulty);					
				}
				
				if (!isCPU) {
					btnDifficulty.setEnabled(false);
				}
				
				// ColorButton
				btnColor = new ColorButton(0, 0, height, height, gc);
				btnColor.setX(captionWidth * 3 + txtColor.getWidth() / 2 - btnColor.getWidth() / 2);
				btnColor.attachTo(this);
				btnColor.addState("", Player.getDefaultColor(Player.PL1));
				btnColor.addState("", Player.getDefaultColor(Player.PL2));
				btnColor.addState("", Player.getDefaultColor(Player.PL3));
				btnColor.addState("", Player.getDefaultColor(Player.PL4));
				btnColor.addState("", Player.getDefaultColor(Player.PL5));
				btnColor.addState("", Player.getDefaultColor(Player.PL6));
				btnColor.setState(Player.getDefaultColor(player.getId()));
				
				player.setFraction(btnFraction.getCurrentValue());
				player.setDifficulty(btnDifficulty.getCurrentValue());
				player.setColor(btnColor.getCurrentValue());
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void draw(Graphics g) {	
			if (!player.isUndefined()) {
				if (player.isCPU()) {
					txtPlayerName.draw(g);
				} else {
					edtPlayerName.draw(g);
				}
			}
			btnFraction.draw(g);
			btnDifficulty.draw(g);
			btnColor.draw(g);			
		}
		
		@Override
		public void update(int delta) {
			super.update(delta);
			if (!player.isUndefined()) {
				if (player.isCPU()) {
					txtPlayerName.update(delta);
				} else {
					edtPlayerName.update(delta);
				}
			}
			btnFraction.update(delta);
			btnDifficulty.update(delta);
			btnColor.update(delta);
			
			// Customize the GUI			
			try {				
				if (btnFraction.onClick()) {
					player.setFraction(btnFraction.getCurrentValue());
				}
				
				if (btnDifficulty.onClick()) {
					player.setDifficulty(btnDifficulty.getCurrentValue());
				}
				
				if (btnColor.onClick() || btnColor.getCurrentValue() != player.getColor()) {
					player.setColor(btnColor.getCurrentValue());					
				}
				
				if (!txtPlayerName.getColor().equals(btnColor.getCurrentValue())) {
					txtPlayerName.setColor(btnColor.getCurrentValue());					
				}
				
				if (!edtPlayerName.getColor().equals(btnColor.getCurrentValue())) {
					edtPlayerName.setColor(btnColor.getCurrentValue());
				}
			} catch (SlickException e) {
				e.printStackTrace();
			}
						
		}

		public Player getPlayer() {
			return player;
		}
		
		public void reset() {
			btnColor.setState(Player.getDefaultColor(player.getId()));
		}
		
		
		public void setPlayerColor(Color color) {
			btnColor.setState(color);
		}
		
		public ColorButton getColorButton() {
			return btnColor;
		}
		
		public boolean isReady() {
			
			boolean ready = true;
			
			// Check Difficulty
			try {
				if (btnDifficulty.isEnabled() && btnDifficulty.getCurrentValue().getState() == Difficulty.NONE) {
					ready = false;
				}
			} catch (SlickException e) {
				e.printStackTrace();
			}
			return ready;
		}
		
	}

}
