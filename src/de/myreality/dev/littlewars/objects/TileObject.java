/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * TileObject contains animations
 * 
 * @version 	0.2
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.objects;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.pathfinding.Mover;

import de.myreality.dev.littlewars.components.Vector2d;
import de.myreality.dev.littlewars.components.resources.SpriteAnimationData;
import de.myreality.dev.littlewars.world.GameWorld;

/**
 * TileObjects that exist only in a tileworld
 * 
 * @version 0.1
 * @author Miguel Gonzalez
 *
 */
public abstract class TileObject extends GameObject implements Movable, Mover {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Animations
	protected Animation[][] animations;
	
	// Tile color
	protected Color color;	
	
	// Vectors of moving
	protected Vector2d veloVector, targetPos;	
	
	// Current velocity
	protected float velocity;
	
	// Layer on the tiledmap
	protected int layer = -1;
	
	// target tiledmap
	protected GameWorld map;

	// current direction
	protected int currentDirection;
	
	// Avatar image
	protected Image imgAvatar;
	protected String imgAvatarID;
	
	protected float lastX, lastY;
	protected boolean moveRequest;
			
	public int getLayer() {
		return layer;
	}
	

	public void setLayer(int layer) {
		this.layer = layer;
	}

	// TODO Create javadoc here
	TileObject(int x, int y, GameContainer gc, Camera cam, GameWorld map) throws SlickException {
		super(x, y, gc, cam);
		color = new Color(255, 255, 255, 255);
		this.map = map;
		this.veloVector = new Vector2d(0f, 0f);
		this.targetPos = new  Vector2d(-1f, -1f);
		this.velocity = 0.0f;
		this.currentDirection = Movable.TOP;
		width = map.getTileWidth();
		height = map.getTileHeight();
		lastX = x;
		lastY = y;
		targetPos.x = getX();
		targetPos.y = getY();
	}

	
	// TODO Create javadoc here
	protected void setAnimation(SpriteAnimationData data) {
		animations = data.generateAnimations();	
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	
	public int getTileX() {
		float value = getX() / map.getTileWidth();		
		
		return (int) (Math.round(value));
	}
	
	public int getTileY() {
		float value = getY() / map.getTileHeight();		
		
		return (int) (Math.round(value));
	}
	
	public int getLastTileX() {
		float value = lastX / map.getTileWidth();		
		
		return (int) (Math.round(value));
	}
	
	public int getLastTileY() {
		float value = lastY / map.getTileHeight();		
		
		return (int) (Math.round(value));
	}
	

	@Override
	public boolean isHover() {
		return area.contains(camera.getX() + gc.getInput().getMouseX(), camera.getY() + gc.getInput().getMouseY());
	}
	
	
	
	public float getTargetX() {
		return targetPos.x;
	}
	
	public float getTargetY() {
		return targetPos.y;
	}


	@Override
	public void move(int direction, int delta) {
		
		currentDirection = direction;
		
		if (targetPos.x == -1f && targetPos.y == -1f) {
			targetPos.x = getX();
			targetPos.y = getY();
		}
		
		switch (direction) {
			case Movable.TOP:					
				veloVector.x = 0f;
				veloVector.y = -getTileVelocity(delta);		
				targetPos.x = map.tileIndexX(getX()) * map.getTileWidth();
				targetPos.y = map.tileIndexY(getY()) * map.getTileHeight() - map.getTileHeight();		
				moveRequest = true;
				break;
			case Movable.BOTTOM:
				veloVector.x = 0f;
				veloVector.y = getTileVelocity(delta);
				targetPos.x = map.tileIndexX(getX()) * map.getTileWidth();
				targetPos.y = map.tileIndexY(getY()) * map.getTileHeight() + map.getTileHeight();
				moveRequest = true;
				break;
			case Movable.LEFT:
				veloVector.x = -getTileVelocity(delta);
				veloVector.y = 0f;
				targetPos.x = map.tileIndexX(getX()) * map.getTileWidth() - map.getTileHeight();
				targetPos.y = map.tileIndexY(getY()) * map.getTileHeight();
				moveRequest = true;
				break;
			case Movable.RIGHT:
				veloVector.x = getTileVelocity(delta);
				veloVector.y = 0f;
				targetPos.x = map.tileIndexX(getX()) * map.getTileWidth() + map.getTileHeight();
				targetPos.y =map.tileIndexY(getY()) * map.getTileHeight();
				moveRequest = true;
				break;
			}	
			
		
		if (targetPos.x < 0) {
			targetPos.x = 0;
		}
		
		if (targetPos.y < 0) {
			targetPos.y = 0;
		}
	}
	
	// TODO Create javadoc here
	public boolean isTargetArrived() {
		return (int)Math.floor(targetPos.x) == (int)Math.floor(getX()) && (int)Math.floor(targetPos.y) == (int)Math.floor(getY());
	}
	
	// TODO Create javadoc here
	public void stop() {
		
		if (isTargetArrived()) {
			if (veloVector.x != 0f) {
				veloVector.x = 0f;
			}
			
			if (veloVector.y != 0f) {
				veloVector.y = 0f;
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		if (isTargetArrived()) {
			if (animations[SpriteAnimationData.DEFAULT][currentDirection] != null) {
				animations[SpriteAnimationData.DEFAULT][currentDirection].draw(-camera.getX() + getX(), -camera.getY() + getY(), getWidth(), getHeight(), color);
			}
		} else {
			if (animations[SpriteAnimationData.MOVE][currentDirection] != null) {
				animations[SpriteAnimationData.MOVE][currentDirection].draw(-camera.getX() + getX(), -camera.getY() + getY(), getWidth(), getHeight(), color);
			}
		}
	}

	@Override
	public void update(int delta)
	{		
		super.update(delta);
		if (isTargetArrived()) {
			x = targetPos.x;
			y = targetPos.y;
			if (animations[SpriteAnimationData.DEFAULT][currentDirection] != null) {
				animations[SpriteAnimationData.DEFAULT][currentDirection].update(delta);
			}		
			moveRequest = false;
		} else {
			if (moveRequest) {
				lastX = x;
				lastY = y;
				moveRequest = false;
			}
			x += veloVector.x;
			y += veloVector.y;			
		} 
	}
	
	public float getLastX() {
		return lastX;
	}


	public float getLastY() {
		return lastY;
	}


	public Image getImgAvatar() {
		return imgAvatar;
	}


	@Override
	public void setX(float x) {
		lastX = this.x;
		super.setX(x);
		targetPos.x = x;
	}


	@Override
	public void setY(float y) {
		lastY = this.y;
		super.setY(y);
		targetPos.y = y;
	}
	
	
	public void setTargetX(float x) {
		targetPos.x = x;
	}
	
	public void setTargetY(float y) {
		targetPos.y = y;
	}
	
	
	
	/**
	 * Set the current position to target
	 */
	public void setToTarget() {
		lastX = x;
		lastY = y;
		x = targetPos.x;
		y = targetPos.y;
		moveRequest = false;
		veloVector.x = 0;
		veloVector.y = 0;
	}
	
	
	
	/**
	 * Function that calculates a correct velocity in order to move
	 * from one tile to another. Additionally, the frame's delta is
	 * considered.
	 * 
	 * @param delta frame's delta
	 */
	public abstract float getTileVelocity(int delta); 
	
	
	public boolean hasMoved() {
		return lastX != x || lastY != y;
	}
	
	
	
	public float distanceTo(TileObject object) {
		float x1 = getX();
		float y1 = getY();
		float x2 = object.getX();
		float y2 = object.getY();
		return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
	
	public int getCurrentDirection() {
		return currentDirection;
	}

}
