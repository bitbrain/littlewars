/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Provides a gamestate of showing the bug report screen
 * 
 * @version 	0.5.4
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.game;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.helpers.FlashHelper;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.Button;

public class BugReportState extends CustomGameState {

	private int lastID;
	
	// Background image
	private Image backgroundImage;

	// Button in order to navigating back
	private Button backButton, sendButton;
	
	public BugReportState(int id, int lastID) {
		super(id);
		this.lastID = lastID;
	}
	
	

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		super.init(gc, sbg);
		int padding = 40;
		sendButton = new Button(padding, padding, gc.getWidth() - padding * 2, 300, ResourceManager.getInstance().getText("TXT_GAME_SEND") , gc);
		backButton = new Button(padding, padding * 2 + sendButton.getHeight(), sendButton.getWidth(), gc.getHeight() - padding * 3 - sendButton.getHeight(), ResourceManager.getInstance().getText("TXT_GAME_BACK") , gc);	
		sendButton.setFont(ResourceManager.getInstance().getFont("FONT_CAPTION"));
		backButton.setFont(ResourceManager.getInstance().getFont("FONT_CAPTION"));
		backgroundImage = ResourceManager.getInstance().getImage("MENU_BACKGROUND");		
	}



	@Override
	protected void renderContent(GameContainer gc, StateBasedGame sbg,
			Graphics g) {
		backgroundImage.draw(0, 0, gc.getWidth(), gc.getHeight());
		backButton.draw(g);
		sendButton.draw(g);
	}

	@Override
	protected void updateContent(GameContainer gc, StateBasedGame sbg,
			int delta) {
		backButton.update(delta);
		sendButton.update(delta);
		if (backButton.onMouseClick()) {
			sbg.enterState(lastID);
		}
		
		if (sendButton.onMouseClick()) {	
			if(java.awt.Desktop.isDesktopSupported() ) {
	              java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
	       
	              if(desktop.isSupported(java.awt.Desktop.Action.BROWSE) ) {
	            	  URI uri;
					try {
						uri = new URI("http://myreality.lighthouseapp.com/projects/97663-little-wars/overview");
						desktop.browse(uri);
						FlashHelper.getInstance().flash("Take a look in your web browser", 1000, gc);
					} catch (URISyntaxException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
	                  
	              } else {
	            	  FlashHelper.getInstance().flash("Can't open the report page", 1000, gc);
	              }
	       }
		   sbg.enterState(lastID);
		}
	}
}
