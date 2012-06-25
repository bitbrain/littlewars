/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Bottom game menu that contains the minimap, player info and buttons
 * 
 * @version 	0.3.1
 * @author 		Miguel Gonzalez		
 */


package de.myreality.dev.littlewars.gui.bottommenu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.gui.bottommenu.builder.BasicUnitBuilder;
import de.myreality.dev.littlewars.gui.bottommenu.builder.InitialisationBuilder;
import de.myreality.dev.littlewars.gui.bottommenu.builder.PreperationBuilder;
import de.myreality.dev.littlewars.ki.Player;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.objects.GUIObject;
import de.myreality.dev.littlewars.world.GameWorld;

public class BottomMenu extends GUIObject {
	
	// Background image
	private Image background;
	
	// Minimap
	private MiniMap miniMap;
	
	// Right unit box
	private UnitAvatarInfo unitInfo;
	
	// Unit info box
	private UnitInfoBox unitInfoBox;
	
	// Client player
	private Player player;
	
	// Unit overview
	private UnitOverview unitOverview;
	boolean foundFocusObject;
	
	private BasicUnitBuilder preperationBuilder, initialisationBuilder;
	
	private IngameState game;

	public BottomMenu(int height, IngameState game, GameContainer gc) {
		super(0, gc.getHeight() - height, gc);
		this.height = height;
		width = gc.getWidth();
		area = new Rectangle(getX(), getY(), getWidth(), getHeight());
		background = ResourceManager.getInstance().getImage("GUI_BOTTOM_BACKGROUND");
		miniMap = new MiniMap(this, gc);
		unitInfo = new UnitAvatarInfo(this, gc);
		unitInfoBox = new UnitInfoBox(unitInfo, this, gc);		
		unitOverview = new UnitOverview(miniMap, unitInfoBox, this, gc);
		this.game = game;
		foundFocusObject = false;
	}
	
	public void setGameWorld(GameWorld world) {
		miniMap.setTargetWorld(world);
		unitInfo.setTargetWorld(world);
		unitInfoBox.setTargetWorld(world);
		unitOverview.setTargetWorld(world);
	}


	@Override
	public void draw(Graphics g) {
		if (isVisible()) {
			if (background != null) {
				background.draw(getX(), getY(), getWidth(), getHeight());
			}
			
			miniMap.draw(g);		
			unitInfoBox.draw(g);
			unitInfo.draw(g);
			
			if (player != null && player.isCurrentPlayer() && game.getPhase() != IngameState.BATTLE && (game.getPhase() == IngameState.INIT || preperationBuilder.size() > 0) && foundFocusObject) {
				
				if (game.getPhase() == IngameState.PREPERATION) {
					if (preperationBuilder != null) {
						game.setPreviewSelected(preperationBuilder.hasSelectedPreview());
						preperationBuilder.setVisible(true);
						initialisationBuilder.setVisible(false);
						preperationBuilder.draw(g);
					}
				} else {
					if (initialisationBuilder != null) {
						game.setPreviewSelected(initialisationBuilder.hasSelectedPreview());
						preperationBuilder.setVisible(false);
						initialisationBuilder.setVisible(true);
						initialisationBuilder.draw(g);
					}
				}
			} else {
				game.setPreviewSelected(false);
				preperationBuilder.setVisible(false);
				initialisationBuilder.setVisible(false);
				unitOverview.draw(g);
			}
		}
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		miniMap.update(delta);		
		unitInfo.update(delta);
		unitInfoBox.update(delta);		
		foundFocusObject = false;
		for (ArmyUnit center : game.getClientPlayer().getCommandoCenters()) {
			if (game.getWorld().getFocusObject() != null && game.getWorld().getFocusObject().equals(center)) {
				foundFocusObject = true;
				break;
			}
		}
		if (player != null && player.isCurrentPlayer() && game.getPhase() != IngameState.BATTLE && (game.getPhase() == IngameState.INIT || preperationBuilder.size() > 0) && foundFocusObject) {
			if (game.getPhase() == IngameState.PREPERATION) {
				if (preperationBuilder != null) {
					preperationBuilder.update(delta);
				}
			} else {
				if (initialisationBuilder != null) {
					initialisationBuilder.update(delta);
				}
			}
		} else {	
			unitOverview.update(delta);		
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
		unitOverview.setPlayer(player);
		preperationBuilder = new PreperationBuilder(miniMap, unitInfoBox, this, player, gc);
		initialisationBuilder = new InitialisationBuilder(miniMap, unitInfoBox, this, player, gc);
	}
	
	public IngameState getGame() {
		return game;
	}

	public BasicUnitBuilder getPreperationBuilder() {
		return preperationBuilder;
	}

	public BasicUnitBuilder getInitialisationBuilder() {
		return initialisationBuilder;
	}
	
	
}
