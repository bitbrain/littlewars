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

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.objects.Camera;
import de.myreality.dev.littlewars.objects.CommandoCenter;

public class SpawnArea {

	private SpawnCell area[][];
	
	private boolean visible;
	
	private IngameState game;
	
	private Color spawnColor, forbiddenColor;
	
	public SpawnArea(IngameState game, Player owner) {
		area = new SpawnCell[game.getWorld().getWidth()][game.getWorld().getHeight()];
		this.game = game;
		visible = false;
		spawnColor = new Color(100, 255, 100, 80);
		forbiddenColor = new Color(0, 0, 0, 100);
		// Fill the area array with data
		GameWorld world = game.getWorld();
		for (int x = 0; x < world.getWidth(); ++x) {
			for (int y = 0; y < world.getHeight(); ++y) {
				area[x][y] = new SpawnCell();
			}
		}
	}
	
	public void writeArea(CommandoCenter center) {
		if (area != null) {
			// Delete the old one
			queryRadiusRange(center.getLastTileX(), center.getLastTileY(), center, true);
			// Write the new one
			queryRadiusRange(center.getTileX(), center.getTileY(), center);
		}
	}
	
	public void removeArea(CommandoCenter center) {
		if (area != null && center.getPlayer() == null) {
			// TODO: Remove area
		}
	}
	
	public boolean isInRange(int tileX, int tileY) {
		if (tileX > 0 && tileX < game.getWorld().getWidth() &&
			tileY > 0 && tileY < game.getWorld().getHeight()) {
			return area[tileX][tileY].hasOwner() && !game.getWorld().collisionExists(tileX, tileY);
		} else {
			return false;
		}
	}
	
	public void draw(Graphics g) {
		if (isVisible()) {			
			GameWorld world = game.getWorld();
			Camera camera = world.getCamera();
			int tileStartX = world.tileIndexX(camera.getX());
			int tileStartY = world.tileIndexY(camera.getY());
			int tileEndX = world.tileIndexX(camera.getX() + camera.getWidth());
			int tileEndY = world.tileIndexY(camera.getY() + camera.getHeight());
			int tileOffsetX = (int) (camera.getX() % world.getTileWidth());
		    int tileOffsetY = (int) (camera.getY() % world.getTileHeight());
			for (int x = tileStartX; x < tileEndX; ++x) {
				for (int y = tileStartY; y < tileEndY; ++y) {
					if (area[x][y].hasOwner() && !game.getWorld().collisionExists(x, y)) {
						g.setColor(spawnColor);
					} else {
						g.setColor(forbiddenColor);
					}
					int tileWidth = world.getTileWidth();
					int tileHeight = world.getTileHeight();
					
					if (x == tileEndX - 1) {
						tileWidth += tileOffsetX;
					}
					
					if (y == tileEndY - 1) {
						tileHeight += tileOffsetY;
					}
					
					g.fillRect(x * world.getTileWidth() - camera.getX(), 
							   y * world.getTileHeight() - camera.getY(), tileWidth, tileHeight);
					
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
	
	// Single Spawn Cell
	class SpawnCell {
		
		private List<CommandoCenter> owners;
		
		public SpawnCell() {
			owners = new ArrayList<CommandoCenter>();
		}
		
		public void addOwner(CommandoCenter center) {
			owners.add(center);
		}
		
		public void removeOwner(CommandoCenter center) {
			owners.add(center);
		}
		
		public boolean hasOwner() {
			return !owners.isEmpty();
		}
	}
	
	private void radiusCall(int tileX, int tileY, CommandoCenter center, boolean deleting) {
		// TODO: Create pathfinding algorithm
		area[tileX][tileY].addOwner(center);		
	}
	
	/**
	 * Recursive function in order to calculate a radial structure
	 * 
	 * @param range Range of the circle
	 */
	private void queryRadiusRange(int tileX, int tileY, CommandoCenter center) {
		queryRadiusRange(tileX, tileY, center, false);
	}
	private void queryRadiusRange(int tileX, int tileY, CommandoCenter center, boolean deleting) {
		int range = center.getSpawnRange();
		if (range > 0) {			
			int x0 = range;
			int y0 = range;
	
			int f = 1 - range;
		    int ddF_x = 1;
		    int ddF_y = -2 * range;
		    int x = 0;
		    int y = range;
		 
		    radiusCall(x0,y0 + range, center, deleting);
		    radiusCall(x0,y0 - range, center, deleting);
		    radiusCall(x0 + range,y0, center, deleting);
		    radiusCall(x0 - range,y0, center, deleting);
		 
		    while(x < y)
		    {
		      // ddF_x == 2 * x + 1;
		      // ddF_y == -2 * y;
		      // f == x*x + y*y - radius*radius + 2*x - y + 1;
		      if(f >= 0) 
		      {
		        y--;
		        ddF_y += 2;
		        f += ddF_y;
		      }
		      x++;
		      ddF_x += 2;
		      f += ddF_x;    
		      radiusCall(x0 + x, y0 + y, center, deleting);
		      radiusCall(x0 - x, y0 + y, center, deleting);
		      radiusCall(x0 + x, y0 - y, center, deleting);
		      radiusCall(x0 - x, y0 - y, center, deleting);
		      radiusCall(x0 + y, y0 + x, center, deleting);
		      radiusCall(x0 - y, y0 + x, center, deleting);
		      radiusCall(x0 + y, y0 - x, center, deleting);
		      radiusCall(x0 - y, y0 - x, center, deleting);
		  }
		}		
	}
}
