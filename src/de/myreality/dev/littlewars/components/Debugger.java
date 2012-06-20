/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Implements an debugger for debug output and in order to drawing things
 * 
 * @version 	0.0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.components;

import java.util.Calendar;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.components.resources.ResourceManager;

public class Debugger {

	private static Debugger _instance;
	private boolean debug;
	private static int slickBuild = GameContainer.getBuildVersion();
	
	static {
		_instance = new Debugger();
	}
	
	public Debugger() {
		debug = true;
	}
	
	public void setEnabled(boolean value) {
		debug = value;
	}
	
	public boolean isEnabled() {
		return debug;
	}

	public static Debugger getInstance() {
		return _instance;
	}
	
	
	/**
	 * Display a message on the console with additional information
	 * 
	 * @param message Message to display
	 */
	public void write(String message) {
		if (debug) {
			String timeInfo = String.valueOf(Calendar.getInstance().getTime());
			System.out.println(timeInfo + " DEBUG:" + message);
		}
	}
	
	
	public String getFullGameInfo() {
		return ResourceManager.getInstance().getText("TXT_GAME_NAME") + " v. " + 
	             ResourceManager.getInstance().getText("TXT_GAME_VER") + " " +
	             ResourceManager.getInstance().getText("TXT_GAME_PHASE");
	}
	
	
	public void drawGameInfo(GameContainer gc, Graphics g) {
		if (debug) {
			g.setColor(Color.white);
			g.drawString(getFullGameInfo(), 20, 20);
			g.drawString("Slick build " + slickBuild, 20, 40);
			g.drawString("FPS: " + String.valueOf(gc.getFPS()), 20, 60);
			g.drawString("MODE: " + String.valueOf(gc.getWidth()) + "x" + String.valueOf(gc.getHeight()), 20, 80);
			g.drawString("Cursor: " + String.valueOf(gc.getInput().getMouseX()) + " | " + String.valueOf(gc.getInput().getMouseY()), 20, gc.getHeight() - 35);
		}
	}
}
