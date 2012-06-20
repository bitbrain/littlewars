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

import java.net.UnknownHostException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.helpers.MailHelper;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.Button;
import de.myreality.dev.littlewars.gui.GameText;
import de.myreality.dev.littlewars.gui.TextBox;
import de.myreality.dev.littlewars.gui.ZoomButton;

public class BugReportState extends CustomGameState {

	private int lastID;
	
	// Background image
	private Image backgroundImage;
	
	// Caption text on the top
	private GameText caption, infoText;
	
	// Button in order to navigating back
	private Button backButton, sendButton;
	
	// TextBox of the Task description
	private TextBox description;
	
	// Email-Target
	//private static final String emailTarget = "info@my-reality.de";
	
	public BugReportState(int id, int lastID) {
		super(id);
		this.lastID = lastID;
	}
	
	

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		super.init(gc, sbg);
		backButton = new ZoomButton(60, gc.getHeight() - 110, 250, 70, ResourceManager.getInstance().getText("TXT_GAME_BACK") , gc);	
		sendButton = new ZoomButton(gc.getWidth() - 310, gc.getHeight() - 110, 250, 70, ResourceManager.getInstance().getText("TXT_GAME_SEND") , gc);
		backgroundImage = ResourceManager.getInstance().getImage("MENU_BACKGROUND");
		// TODO: Make text multi language
		caption = new GameText(60, 50, ResourceManager.getInstance().getText("TXT_MENU_BUGREPORT"),
				   ResourceManager.getInstance().getFont("FONT_CAPTION"), gc);
		infoText  = new GameText(60, 160, ResourceManager.getInstance().getText("TXT_GAME_BUGREPORT_INFO") + ":",
				   ResourceManager.getInstance().getFont("FONT_MENU"), gc);
		infoText.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		description = new TextBox(60, 220, gc.getWidth() - 122, 230, gc);
		description.setFocused();
	}



	@Override
	protected void renderContent(GameContainer gc, StateBasedGame sbg,
			Graphics g) {
		backgroundImage.draw(0, 0, gc.getWidth(), gc.getHeight());
		backButton.draw(g);
		sendButton.draw(g);
		caption.draw(g);
		description.draw(g);
		infoText.draw(g);
	}

	@Override
	protected void updateContent(GameContainer gc, StateBasedGame sbg,
			int delta) {
		backButton.update(delta);
		sendButton.update(delta);
		description.update(delta);
		infoText.update(delta);
		if (backButton.onClick()) {
			sbg.enterState(lastID);
		}
		
		sendButton.setEnabled(!description.isEmpty() && description.size() > 20);
		
		if (sendButton.onClick()) {	
			try {
				MailHelper.getInstance().sendMail("info@my-reality.de", "Blaaaa!!!", "aaahja");
			} catch (AddressException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
}
