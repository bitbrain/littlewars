/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Camera class of the game
 * 
 * @version 	0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.objects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import de.myreality.dev.littlewars.components.Vector2d;
import de.myreality.dev.littlewars.gui.GUIObject;
import de.myreality.dev.littlewars.world.GameWorld;

public class Camera extends GameObject implements Movable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Parent tiled map
	private GameWorld world;
	
	// Mouse border
	private int border = 10;
	
	// velocity of the camera
	private float velocity;
	
	private GUIObject bottomMenu;
	private GUIObject topMenu;
	
	// X and Y of the new position
	private float targetX, targetY;
	private Vector2d distance;
	
	// State of moving
	boolean moveSmooth;
	
	// Prevent shake density
	private static int antiShake = 8;
	
	/**
	 * Constructor of Camera
	 * 
	 * @param parent
	 * @param container
	 */
	public Camera(GameWorld world, GameContainer container) {
		super(container);
		this.world = world;
		this.velocity = 0.7f;
		area = new Rectangle(0, 0, container.getWidth(), container.getHeight());		
		targetX = 0; targetY = 0;
		distance = new Vector2d(0, 0);
	}
	
	
	/**
	 * Constructor of Camera
	 * 
	 * @param container
	 * @param parent
	 * @param target
	 */
	public Camera(GameContainer container, GameWorld world, GameObject target) {
		super(container);
		attachTo(target);
		this.world = world;
		targetX = target.getX(); targetY = target.getY();
		distance = new Vector2d(0, 0);
	}
	
	
	/**
	 * Constructor of Camera
	 * 
	 * @param x
	 * @param y
	 * @param parent
	 * @param container
	 */
	public Camera(int x, int y, GameWorld world, GameContainer container) {
		super(x, y, container);
		this.world = world;
		this.velocity = 0.2f;
		targetX = getX();
		targetY = getY();
		distance = new Vector2d(0, 0);
	}
	
	public int getBorder() {
		return border;
	}

	public void setBorder(int border) {
		this.border = border;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	@Override
	public int getWidth() {
		return gc.getWidth();
	}

	@Override
	public int getHeight() {
		if (bottomMenu != null) {
			return gc.getHeight() - bottomMenu.getHeight();			
		} else {
			return gc.getHeight();
		}
	}

	@Override
	public void draw(Graphics g) {
		
	}
	
	public void correctWorldPosition() {
		if (getX() < 0) {
			setX(0);
		}
		if (getY() < 0) {
			setY(0);
		}
		if (getX() + getWidth() > world.getWidth() * world.getTileWidth()) {
			setX(world.getWidth() * world.getTileWidth() - getWidth());
		}
		
		if (getY() > world.getHeight() * world.getTileHeight() - getHeight()) {
			setY(world.getHeight() * world.getTileHeight() - getHeight());
		}
	}	

	@Override
	public float getX() {
		return super.getX();
	}


	@Override
	public float getY() {
		// TODO: Fix Top menu bug (camera doesn't move when value has been edited)
		//if (getTopMenu() != null) {
		//	return super.getY() - getTopMenu().getHeight();
		//} else {
			return super.getY();
		//}
	}

	@Override
	public void update(int delta) {

		if (isSmoothMoving()) {					
			distance.x = targetX - getX();
			distance.y = targetY - getY();
			Vector2d unit = distance.getUnitVector();
			x += unit.x * delta * Math.ceil(distance.getLength() / 200);
			y += unit.y * delta * Math.ceil(distance.getLength() / 200);
			if (x < 0) {
				x = 0;
				targetX = 0;
			}
			if (y < 0) {
				y = 0;
				targetY = 0;
			}

			distance.x = targetX - getX();
			distance.y = targetY - getY();

			// Remove display shaking
			if (distance.getLength() < antiShake) {
				x = targetX;
				y = targetY;
			}
			
			// Check, if elements are equal
			moveSmooth = Math.ceil(getX()) != Math.ceil(targetX) || Math.ceil(getY()) != Math.ceil(targetY);
		}
		correctWorldPosition();
	}

	@Override
	public void move(int direction, int delta) {
		lastX = x;
		lastY = y;
		if (!isSmoothMoving()) {
			switch (direction) {
				case Movable.TOP:
					y -= delta * velocity;
					break;
				case Movable.BOTTOM:
					y += delta * velocity;
					break;
				case Movable.LEFT:
					x -= delta * velocity;
					break;
				case Movable.RIGHT:
					x += delta * velocity;
					break;
				default:
					// Do nothing
			}
		}		
	}


	public GUIObject getBottomMenu() {
		return bottomMenu;
	}


	public void setBottomMenu(GUIObject bottomMenu) {
		this.bottomMenu = bottomMenu;
	}

	
	public void moveTo(float x, float y) {
		targetX = x;
		targetY = y;
		moveSmooth = true;
		correctWorldPosition();
	}
	
	
	
	
	@Override
	public void setX(float x) {
		super.setX(x);
		targetX = x;
	}


	@Override
	public void setY(float y) {
		super.setY(y);
		targetY = y;
	}


	public boolean isSmoothMoving() {
		return moveSmooth;
	}


	public GUIObject getTopMenu() {
		return topMenu;
	}


	public void setTopMenu(GUIObject topMenu) {
		this.topMenu = topMenu;
	}
}
