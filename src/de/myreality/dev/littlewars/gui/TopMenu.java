/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * @version 	0.5.4
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import de.myreality.dev.littlewars.components.helpers.FlashHelper;
import de.myreality.dev.littlewars.components.helpers.PopupHelper;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.game.IngameState;

public class TopMenu extends GUIObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Background image
	private Image background;
	
	// Save game button
	private Button btnSave;
		
	// Load game button
	private Button btnLoad;
		
	// Quit game button
	private Button btnQuit;
	
	// Quit phase game button
	private Button btnQuitPhase;
		
	// Ingame state
	private IngameState game;
	
	// Game name text
	private GameText txtMapName;
	
	// Player name text
	private GameText txtPlayerName;
	
	// Credits of the player
	private GameText txtPlayerCredits;
	
	// Gametime
	private GameText txtTime;	
	
	private static int padding = 7;

	public TopMenu(int height, IngameState game, GameContainer gc) {
		super(0, 0, gc);
		
		int size = 25;
		
		this.game = game;
		this.width = gc.getWidth();
		this.height = height;
		area = new Rectangle(getX(), getY(), getWidth(), getHeight());
		background = ResourceManager.getInstance().getImage("GUI_TOP_BACKGROUND");
		txtMapName = new GameText(padding, padding, "Welt 1", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		txtPlayerName = new GameText(padding * 2 + size, padding, "-", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		txtPlayerCredits = new GameText((int) (txtPlayerName.getX() + txtPlayerName.getWidth() + padding * 4), padding, "-", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		txtPlayerCredits.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		txtMapName.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		txtTime = new GameText(txtPlayerName.getWidth() + padding, padding, "", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);		
		try {			
			btnQuit = new IconButton(getWidth() - size - padding, padding, size, ResourceManager.getInstance().getImage("ICON_QUIT"), gc);
			btnLoad = new IconButton(getWidth() - 2 * size - 2 * padding, padding, size, ResourceManager.getInstance().getImage("ICON_LOAD"), gc);
			btnSave = new IconButton(getWidth() - 3 * size - 3 * padding, padding, size, ResourceManager.getInstance().getImage("ICON_SAVE"), gc);			
			btnQuitPhase = new IconButton(padding, padding, size, ResourceManager.getInstance().getImage("ICON_QUIT"), gc);
			btnSave.attachTo(this);
			PopupHelper.getInstance().addPopup(btnSave, ResourceManager.getInstance().getText("TXT_INFO_SAVEGAME"), gc);
			btnLoad.attachTo(this);
			PopupHelper.getInstance().addPopup(btnLoad, ResourceManager.getInstance().getText("TXT_INFO_LOADGAME"), gc);
			btnQuit.attachTo(this);
			PopupHelper.getInstance().addPopup(btnQuit, ResourceManager.getInstance().getText("TXT_INFO_QUITGAME"), gc);
			PopupHelper.getInstance().addPopup(btnQuitPhase, ResourceManager.getInstance().getText("TXT_INFO_QUITPHASE"), gc);
			btnLoad.setEnabled(false);
			btnSave.setFont(ResourceManager.getInstance().getFont("FONT_SMALL"));
			btnLoad.setFont(ResourceManager.getInstance().getFont("FONT_SMALL"));
			btnQuit.setFont(ResourceManager.getInstance().getFont("FONT_SMALL"));
			btnSave.setPadding(2);
			btnLoad.setPadding(2);
			btnQuit.setPadding(2);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void draw(Graphics g) {		
		if (background != null) {
			background.draw(getX(), getY(), getWidth(), getHeight());
		}
		
		btnSave.draw(g);
		btnQuit.draw(g);
		btnLoad.draw(g);
		txtMapName.draw(g);
		txtPlayerName.draw(g);
		txtPlayerCredits.draw(g);
		txtTime.draw(g);
		btnQuitPhase.draw(g);
	}
	
	
	
	
	
	@Override
	public void update(int delta) {
		super.update(delta);
		btnSave.update(delta);
		btnQuit.update(delta);
		btnLoad.update(delta);		
		btnQuitPhase.update(delta);
		
		if (game.getWorld() != null) {
			txtMapName.setText(game.getWorld().getName());
			txtMapName.setX(btnSave.getX() - txtMapName.getWidth() - padding);
			
			txtTime.setText(game.getWorld().getDaytime().toString());
			txtTime.setX(txtMapName.getX() - padding - txtTime.getWidth());
		}
		
		if (game.getClientPlayer() != null) {
			// TODO: Create player name selection
			txtPlayerName.setText(game.getClientPlayer().getName());
			txtPlayerName.setColor(game.getClientPlayer().getColor());	
			
			txtPlayerCredits.setX((int) (txtPlayerName.getX() + txtPlayerName.getWidth() + padding * 4));
			txtPlayerCredits.setText(String.valueOf(game.getClientPlayer().getMoney().getCredits()) + "$");
		}
		
		if (btnSave.onMouseClick()) {
			if (game.saveToFile("save.dat")) {
				FlashHelper.getInstance().flash("Game has been saved successfully.", 1000, gc);
			} else {
				FlashHelper.getInstance().flash("Error occured while saving the game.", 1000, gc);
			}
		}
	}

	public Button getBtnSave() {
		return btnSave;
	}


	public Button getBtnLoad() {
		return btnLoad;
	}


	public Button getBtnQuit() {
		return btnQuit;
	}
	
	public Button getBtnPhaseQuit() {
		return btnQuitPhase;
	}
}
