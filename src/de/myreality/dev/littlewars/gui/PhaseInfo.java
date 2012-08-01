/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Phase description GUI
 * 
 * @version 	0.6.15
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.game.phases.BasicGamePhase;
import de.myreality.dev.littlewars.ki.Player;

public class PhaseInfo extends GUIObject {

	private static final long serialVersionUID = 554301985006753430L;
	
	// Game Texts
	GameText txtMain, txtPhase, txtMessage;

	public PhaseInfo(Player player, BasicGamePhase phase, String customMessage, GameContainer gc) {
		super(0, 0, gc);
		width = gc.getWidth() / 3 * 2;
		
		int rowPadding1 = 15;
		int rowPadding2 = 15;
		
		// Main Text
		txtMain = new GameText(0, 0, player.getName(), ResourceManager.getInstance().getFont("FONT_CAPTION"), gc);
		txtMain.setColor(player.getColor());
		txtMain.attachTo(this);
		txtMain.setX(getWidth() / 2 - txtMain.getWidth() / 2);
		txtMain.setY(0);
		
		// Phase Text
		txtPhase = new GameText(0, 0, "Phase: " + phase.getName(), ResourceManager.getInstance().getFont("FONT_MENU"), gc);
		txtPhase.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		txtPhase.attachTo(this);
		txtPhase.setX(getWidth() / 2 - txtPhase.getWidth() / 2);
		txtPhase.setY(txtMain.getHeight() + rowPadding1);
	
		// Message
		int messageHeight = 0;
		if (customMessage != "") {
			txtMessage = new GameText(0, 0, customMessage, ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
			txtMessage.setColor(Color.white);
			txtMessage.attachTo(this);
			txtMessage.setX(getWidth() / 2 - txtMessage.getWidth() / 2);
			txtMessage.setY(txtMain.getHeight() + txtPhase.getHeight() + rowPadding1 + rowPadding2);
			messageHeight = txtMessage.getHeight();
		}
		
		height = rowPadding1 + rowPadding2 + txtMain.getHeight() + txtPhase.getHeight() + messageHeight;
	}
	
	public PhaseInfo(Player player, BasicGamePhase phase, GameContainer gc) {
		this(player, phase, "", gc);
	}

	@Override
	public void draw(Graphics g) {
		txtMain.draw(g);
		txtPhase.draw(g);
		if (txtMessage != null) {
			txtMessage.draw(g);
		}
	}

	@Override
	public void setOpacity(float opacity) {
		super.setOpacity(opacity);
		txtMain.setOpacity(opacity);
		txtPhase.setOpacity(opacity);
		if (txtMessage != null) {
			txtMessage.setOpacity(opacity);
		}
	}
	
	

}
