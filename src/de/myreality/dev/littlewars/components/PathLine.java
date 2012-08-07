/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://littlewars.my-reality.de
 * 
 * Finds a correct Cell in the game
 * 
 * @version 	0.6.4
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.components;

import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.ki.Player;
import de.myreality.dev.littlewars.objects.TileObject;
import de.myreality.dev.littlewars.world.GameWorld;

public class PathLine {

	private IngameState game;
	
	private Player owner;
	
	private Vector2d start, direction;
	
	public PathLine(IngameState game, Player owner, float x1, float y1, float x2, float y2) {
		start = new Vector2d(x1, y1);
		direction = new Vector2d(x2 - x1, y2 - y1);
		this.game = game;
		this.owner = owner;
		
	}
	
	public PathLine(IngameState game, Player owner, TileObject obj1, TileObject obj2) {
		this(game, owner, obj1.getTileX(), obj1.getTileY(), obj2.getTileX(), obj2.getTileY());
	}
	
	public void findPathPosition(TileObject object) {
		float param = 0.0f;
		while (param < 1.0f) {
			param += 0.001;
			int tileX = (int) (start.x + param * direction.x);
			int tileY = (int) (start.y + param * direction.y);
			// Check position
			GameWorld world = game.getWorld();

			if (!world.collisionExists(tileX, tileY) && owner.getSpawnArea().isInRange(tileX, tileY)) {				
				object.setX(tileX * world.getTileWidth());
				object.setY(tileY * world.getTileHeight());
			}
		}
	}
}
