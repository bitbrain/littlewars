/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Shows a radius above the target commando center to spawn units inside
 * 
 * @version 	0.5.14
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.gui.unit;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.objects.CommandoCenter;
import de.myreality.dev.littlewars.objects.GUIObject;
import de.myreality.dev.littlewars.world.GameWorld;

public class SpawnRadius extends GUIObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Commando Center
	private CommandoCenter center;
	
	// Game World
	private GameWorld world;
	
	// Draw Area
	private boolean radius[][];
	
	// Color of the radius
	private Color color;

	public SpawnRadius(CommandoCenter center, GameWorld world, GameContainer gc) {
		super(0, 0, gc);
		this.center = center;
		this.world = world;
		attachTo(center);
		x = -(world.getTileWidth() * center.getSpawnRange());
		y = -(world.getTileHeight() * center.getSpawnRange());
		width = world.getTileWidth() * center.getSpawnRange() * 2 + world.getTileWidth();
		height = world.getTileWidth() * center.getSpawnRange() * 2 + world.getTileWidth();
		area = new Rectangle(getX(), getY(), getWidth(), getHeight());		
		updateRadiusContent();
		color = new Color(0, 0, 0, 140);
		color.r = ResourceManager.getInstance().getColor("COLOR_MAIN").r;
		color.g = ResourceManager.getInstance().getColor("COLOR_MAIN").g;
		color.b = ResourceManager.getInstance().getColor("COLOR_MAIN").b;
	}

	@Override
	public void draw(Graphics g) {
		// TODO: Move draw method into world layer rendering
		if (radius != null && isVisible()) {
			g.setColor(color);
			for (int x = 0; x < radius.length; ++x) {
				for (int y = 0; y < radius[x].length; ++y) {
					if (radius[x][y]) {
						g.fillRect(getX() + world.getTileWidth() * x - world.getCamera().getX(), 
								   getY() + world.getTileHeight() * y - world.getCamera().getY(),  
								   world.getTileWidth(), world.getTileHeight());
					}
				}
			}
		}
	}
	
	
	/**
	 * Recursive function in order to calculate a radial structure
	 * 
	 * @param range Range of the circle
	 */
	public void updateRadiusContent(int range) {
		if (range > 0) {			
			int x0 = center.getSpawnRange();
			int y0 = center.getSpawnRange();
	
			int f = 1 - range;
		    int ddF_x = 1;
		    int ddF_y = -2 * range;
		    int x = 0;
		    int y = range;
		 
		    radius[x0][y0 + range] = true;
		    radius[x0][y0 - range] = true;
		    radius[x0 + range][y0] = true;
		    radius[x0 - range][y0] = true;
		 
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
		      radius[x0 + x][y0 + y] = true;
		      radius[x0 - x][y0 + y] = true;
		      radius[x0 + x][y0 - y] = true;
		      radius[x0 - x][y0 - y] = true;
		      radius[x0 + y][y0 + x] = true;
		      radius[x0 - y][y0 + x] = true;
		      radius[x0 + y][y0 - x] = true;
		      radius[x0 - y][y0 - x] = true;
		  }
		  updateRadiusContent(--range);
		}		
	}
	
	public void updateRadiusContent() {
		radius = new boolean[center.getSpawnRange() * 2 + 1][center.getSpawnRange() * 2 + 1];
		updateRadiusContent(center.getSpawnRange());
		
		// Remove collision cells
		for (int x = 0; x < radius.length; ++x) {
			for (int y = 0; y < radius[x].length; ++y) {
				if (radius[x][y]) {
					radius[x][y] = !world.collisionExists(x + world.tileIndexX(getX()), y + world.tileIndexY(getY()));
				}
			}
		}
	}

}
