/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Single spawn area of a player. Contains an array with valid spawn cells
 * 
 * @version 	0.6.1
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.world;

import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.objects.Camera;
import de.myreality.dev.littlewars.objects.CommandoCenter;

public class SpawnArea {

	private boolean area[][];
	
	private boolean visible;
	
	private IngameState game;
	
	public SpawnArea(IngameState game) {
		area = new boolean[game.getWorld().getWidth()][game.getWorld().getHeight()];
		this.game = game;
		visible = false;
	}
	
	public void writeArea(CommandoCenter center) {
		if (area != null) {
			for (int x = 0; x < area.length; ++x) {
				for (int y = 0; y < area[x].length; ++y) {
					area[x][y] = x % 2 == 0 && y % 2 == 0; // TODO: Create writing algorithm
				}
			}
		}
	}
	
	public void removeArea(CommandoCenter center) {
		
	}
	
	public void clear() {
		area = new boolean[game.getWorld().getWidth()][game.getWorld().getHeight()];
	}
	
	public boolean isInRange(int tileX, int tileY) {
		return area[tileX][tileY];
	}
	
	public void draw(Graphics g) {
		if (isVisible()) {
			GameWorld world = game.getWorld();
			Camera camera = world.getCamera();
			for (int x = 0; x < area.length; ++x) {
				for (int y = 0; y < area[x].length; ++y) {
					if (area[x][y]) {
						g.fillRect(x * world.getTileWidth() - camera.getX(), 
								   y * world.getTileHeight() - camera.getY(), world.getTileWidth(), world.getTileHeight());
					}
				}
			}
		}
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void update(int delta) {
		// TODO: Update spawn area
	}
}
