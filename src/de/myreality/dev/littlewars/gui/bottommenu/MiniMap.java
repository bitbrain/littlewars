/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Minimap of the game
 * 
 * @version 	0.3.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.gui.bottommenu;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.GUIObject;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.objects.Camera;
import de.myreality.dev.littlewars.world.GameWorld;


public class MiniMap extends GUIObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Target game world to render
	private GameWorld targetWorld;
	
	// Map subcoordinates
	float mapX, mapY;
	int mapWidth, mapHeight;
	
	// Map Content
	private Image content, background;
	
	// Map graphics
	//private Graphics mapGraphics;
	
	private static int borderPadding = 12;
	
	public MiniMap(BottomMenu parent, GameContainer gc) {
		super(borderPadding, borderPadding, gc);
		attachTo(parent);
		setPadding(2);
		width = parent.getHeight() - borderPadding * 2;
		height = width;			
		area = new Rectangle(getX() + padding, getY() + padding, getWidth() - padding * 2, getHeight() - padding * 2);
		background = ResourceManager.getInstance().getImage("GUI_BOTTOM_SEPERAT_BACKGROUND_DARK");
	}
	
	private void init() throws SlickException {
		if (targetWorld != null) {
			float scaleFactor = getScaleFactor();
			mapX = getX();
			mapY = getY();
			mapWidth = targetWorld.getWidth() * targetWorld.getTileWidth();
			mapHeight = targetWorld.getHeight() * targetWorld.getTileHeight();	
			mapHeight = (int) Math.ceil(scaleFactor * mapHeight) - padding * 2;
			mapWidth = (int) Math.ceil(scaleFactor * mapWidth) - padding * 2;	
			mapX += (getWidth() / 2 - mapWidth / 2);
			mapY += (getHeight() / 2 - mapHeight / 2);
			
			content = targetWorld.getWorldImage();
		}
	}

	@Override
	public void draw(Graphics g) {		
		background.draw(getX(), getY(), getWidth(), getHeight());
		
		if (targetWorld != null) {
			// Map background
			float scaleFactor = getScaleFactor();
			
			if (content != null) {
				content.draw(mapX, mapY, mapWidth, mapHeight);
			} else {
				g.setColor(Color.darkGray);
				g.fillRect(mapX, mapY, mapWidth, mapHeight);
			}	
			
			// Units
			List<ArmyUnit> units = targetWorld.getAllArmyUnits();
			
			for (ArmyUnit unit : units) {
				g.setColor(unit.getPlayer().getColor());
				// TODO: Position bug
				g.fillRect(mapX + unit.getX() * scaleFactor - padding, 
						   mapY + unit.getY() * (scaleFactor * 0.98f) - padding, 
						   (float)unit.getWidth() * scaleFactor, (float)unit.getHeight() * scaleFactor);
			}
			
			// Camera Rectangle
			float camX = targetWorld.getCamera().getX();
			float camY = targetWorld.getCamera().getY();
			int camWidth = targetWorld.getCamera().getWidth();
			int camHeight = targetWorld.getCamera().getHeight();
			camX *= scaleFactor;
			camY *= scaleFactor;
			camWidth *= scaleFactor;
			camHeight *= scaleFactor;
			g.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
			g.drawRect(mapX + camX, mapY + camY, camWidth - padding * 2, camHeight - padding * 2);	
		}
	}
	
	
	
	public float getScaleFactor() {
		float scaleFactor = 1.0f;
		float mapWidth = targetWorld.getWidth() * targetWorld.getTileWidth();
		float mapHeight = targetWorld.getHeight() * targetWorld.getTileHeight();
		
		if (targetWorld.getWidth() > targetWorld.getHeight()) {
			scaleFactor = (float) getWidth() / mapWidth;				
		} else {								
			scaleFactor = (float) getHeight() / mapHeight;				
		} 
		
		return scaleFactor;
	}

	public GameWorld getTargetWorld() {
		return targetWorld;
	}

	public void setTargetWorld(GameWorld targetWorld) {
		this.targetWorld = targetWorld;
		try {
			init();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(int delta) {
		super.update(delta);

		if (targetWorld != null && isMouseOver() && gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			
			// Initialize variables
			float scaleFactor = getScaleFactor();
			float newX = gc.getInput().getMouseX();			
			float newY = gc.getInput().getMouseY();			
			int camWidth = targetWorld.getCamera().getWidth();
			int camHeight = targetWorld.getCamera().getHeight();
			
			// Calculate new position
			newX -= mapX;
			newY -= mapY;
			camWidth *= scaleFactor;
			camHeight *= scaleFactor;			
			newX -= camWidth / 2;
			newY -= camHeight / 2;			
			newX /= scaleFactor;
			newY /= scaleFactor;
			
			// Repair out-bounding positions
			if (newX < 0) {
				newX = 0;
			}
			
			if (newY < 0) {
				newY = 0;
			}
			
			// Calculate size back
			camWidth = targetWorld.getCamera().getWidth();
			camHeight = targetWorld.getCamera().getHeight();
			
			if (newX + camWidth > targetWorld.getWidth() * targetWorld.getTileWidth()) {
				newX = targetWorld.getWidth() * targetWorld.getTileWidth() - camWidth;
			}
			
			if (newY + camHeight > targetWorld.getHeight() * targetWorld.getTileHeight()) {
				newY = targetWorld.getHeight() * targetWorld.getTileHeight() - camHeight;
			}
			
			// Set new position
			Camera camera = targetWorld.getCamera();
			camera.setX(newX);
			camera.setY(newY);	
		}
	}
	
	
	

}
