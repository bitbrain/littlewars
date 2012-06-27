/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Contains game information like version and author
 * 
 * @version 	0.1.3
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.objects.GUIObject;

public class GameInfo extends GUIObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	// texts
	private GameText authorText, versionText;
	
	
	// String resources
	static private String authorResource, versionResource;
	
	static {
		// Load the resources at the beginning
		authorResource = ResourceManager.getInstance().getText("TXT_GAME_AUTHOR");
		versionResource = "Version " + ResourceManager.getInstance().getText("TXT_GAME_VER") + " " + ResourceManager.getInstance().getText("TXT_GAME_PHASE");
	}
	
	
	
	/**
	 * Constructor of GameInfo
	 * 
	 * @param x x-coordinate on the screen
	 * @param y y-coordinate on the screen
	 * @param gc GameContainer
	 */
	public GameInfo(int x, int y, GameContainer gc) {
		super(x, y, gc);
		versionText = new GameText(getAuthorTextWidth() / 2 - getVersionTextWidth() / 2, 0, versionResource, ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		versionText.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		authorText = new GameText(0, versionText.getHeight() + 10, authorResource, ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		
		authorText.attachTo(this);
		versionText.attachTo(this);
	}

	@Override
	public void draw(Graphics g) {
		authorText.draw(g);
		versionText.draw(g);
	}
	
	static public int getAuthorTextWidth() {
		return ResourceManager.getInstance().getFont("FONT_SMALL").getWidth(authorResource);
	}
	
	static public int getVersionTextWidth() {
		return ResourceManager.getInstance().getFont("FONT_SMALL").getWidth(versionResource);
	}

}
